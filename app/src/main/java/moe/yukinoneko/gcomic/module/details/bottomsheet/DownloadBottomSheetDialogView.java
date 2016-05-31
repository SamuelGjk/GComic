/*
 * Copyright (C) 2016  SamuelGjk <samuel.alva@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package moe.yukinoneko.gcomic.module.details.bottomsheet;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.data.ComicData;
import moe.yukinoneko.gcomic.database.model.DownloadTaskModel;
import moe.yukinoneko.gcomic.download.DownloadTasksManager;
import moe.yukinoneko.gcomic.network.GComicApi;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SamuelGjk on 2016/5/13.
 */
public class DownloadBottomSheetDialogView implements View.OnClickListener {
    public static final String TAG = "DownloadBSDView";

    private Context context;
    private int comicId;
    private String comicTitle;
    private String comicCover;
    private String firstLetter;
    private List<ComicData.ChaptersBean.ChapterBean> chapters;

    private ChapterAdapter mAdapter;

    private CompositeSubscription mCompositeSubscription;

    // @formatter:off
    public DownloadBottomSheetDialogView(Context context, int comicId, String comicTitle, String comicCover, String firstLetter, List<ComicData.ChaptersBean.ChapterBean> chapters) {
        this.context = context;
        this.comicId = comicId;
        this.comicTitle = comicTitle;
        this.comicCover = comicCover;
        this.firstLetter = firstLetter;
        this.chapters = chapters;

        mCompositeSubscription = new CompositeSubscription();
    }

    // @formatter:on
    public void show() {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onBottomSheetDismissListener.onBottomSheetDismiss();
            }
        });

        View view = LayoutInflater.from(context).inflate(R.layout.download_bottom_sheet_dialog, null);

        AppCompatButton buttonDownloadAll = (AppCompatButton) view.findViewById(R.id.button_download_all);
        buttonDownloadAll.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.download_chapter_chooser);
        mAdapter = new ChapterAdapter();
        recyclerView.setAdapter(mAdapter);

        dialog.setContentView(view);
        dialog.show();
    }

    public void unsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onClick(View v) {
        final List<DownloadTaskModel> models = new ArrayList<>();
        for (ComicData.ChaptersBean.ChapterBean chapter : chapters) {
            if (!chapter.isDownloaded) {
                final DownloadTaskModel taskModel = new DownloadTaskModel(-1, comicId, comicTitle, comicCover, chapter.chapterId, chapter.chapterTitle, firstLetter, chapters.indexOf(chapter), chapters.size(), GComicApi.getInstance().generateDownloadUrl(firstLetter, comicId, chapter.chapterId), null);
                models.add(taskModel);
            }
        }

        Subscription subscription = DownloadTasksManager.getInstance(context)
                                                        .addTasks(models)
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(new Action1<Integer>() {
                                                            @Override
                                                            public void call(Integer integer) {
                                                                if (integer == models.size()) {
                                                                    DownloadTasksManager.getInstance(context).startTasks(models);
                                                                    for (ComicData.ChaptersBean.ChapterBean chapter : chapters) {
                                                                        chapter.isDownloaded = true;
                                                                    }
                                                                    mAdapter.notifyDataSetChanged();
                                                                }
                                                            }
                                                        });
        mCompositeSubscription.add(subscription);
    }

    class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_grid_chapter, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ComicData.ChaptersBean.ChapterBean chapter = chapters.get(position);
            if (chapter.isDownloaded) {
                holder.itemView.setBackgroundResource(R.color.colorPrimary);
                ((AppCompatTextView) holder.itemView).setTextColor(0xFFFFFFFF);
            } else {
                holder.itemView.setBackgroundResource(R.drawable.bg_cyan_stroke);
                ((AppCompatTextView) holder.itemView).setTextColor(0xFF00BCD4);
            }

            ((AppCompatTextView) holder.itemView).setText(chapter.chapterTitle);
        }

        @Override
        public int getItemCount() {
            return chapters.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ViewHolder(View itemView) {
                super(itemView);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                final ComicData.ChaptersBean.ChapterBean chapter = chapters.get(getAdapterPosition());

                if (chapter.isDownloaded) {
                    Log.d(TAG, "downloaded.");
                    return;
                }

                final DownloadTaskModel taskModel = new DownloadTaskModel(-1, comicId, comicTitle, comicCover, chapter.chapterId, chapter.chapterTitle, firstLetter, getAdapterPosition(), chapters.size(), GComicApi.getInstance().generateDownloadUrl(firstLetter, comicId, chapter.chapterId), null);
                Subscription subscription = DownloadTasksManager.getInstance(context)
                                                                .addTask(taskModel)
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe(new Action1<Long>() {
                                                                    @Override
                                                                    public void call(Long aLong) {
                                                                        if (aLong != -1) {
                                                                            DownloadTasksManager.getInstance(context).startTask(taskModel);
                                                                            itemView.setBackgroundResource(R.color.colorPrimary);
                                                                            ((AppCompatTextView) itemView).setTextColor(0xFFFFFFFF);
                                                                            chapter.isDownloaded = true;
                                                                        }
                                                                    }
                                                                });
                mCompositeSubscription.add(subscription);
            }
        }
    }

    private OnBottomSheetDismissListener onBottomSheetDismissListener;

    public void setOnBottomSheetDismissListener(OnBottomSheetDismissListener onBottomSheetDismissListener) {
        this.onBottomSheetDismissListener = onBottomSheetDismissListener;
    }

    public interface OnBottomSheetDismissListener {
        void onBottomSheetDismiss();
    }
}
