package moe.yukinoneko.gcomic.module.details.bottomsheet;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class DownloadBottomSheetDialogView {
    public static final String TAG = "DownloadBSDView";

    private Context context;
    private int comicId;
    private String comicTitle;
    private String comicCover;
    private String firstLetter;
    private List<ComicData.ChaptersBean.ChapterBean> chapters;
    private List<Integer> mDownloadedChapters;

    private CompositeSubscription mCompositeSubscription;

    // @formatter:off
    public DownloadBottomSheetDialogView(Context context, int comicId, String comicTitle, String comicCover, String firstLetter, List<ComicData.ChaptersBean.ChapterBean> chapters, List<Integer> downloadedChapters) {
        this.context = context;
        this.comicId = comicId;
        this.comicTitle = comicTitle;
        this.comicCover = comicCover;
        this.firstLetter = firstLetter;
        this.chapters = chapters;
        this.mDownloadedChapters = downloadedChapters;

        mCompositeSubscription = new CompositeSubscription();
    }

    // @formatter:on
    public void show() {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        View view = LayoutInflater.from(context).inflate(R.layout.download_bottom_sheet_dialog, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.download_chapter_chooser);
        recyclerView.setAdapter(new ChapterAdapter());

        dialog.setContentView(view);
        dialog.show();
    }

    public void unsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
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
            if (mDownloadedChapters.contains(chapters.get(position).chapterId)) {
                holder.itemView.setBackgroundResource(R.color.colorPrimary);
                ((AppCompatTextView) holder.itemView).setTextColor(0xFFFFFFFF);
            }
            ((AppCompatTextView) holder.itemView).setText(chapters.get(position).chapterTitle);
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
                ComicData.ChaptersBean.ChapterBean chapter = chapters.get(getAdapterPosition());

                if (mDownloadedChapters.contains(chapter.chapterId)) {
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
                                                                            mDownloadedChapters.add(taskModel.chapterId);
                                                                        }
                                                                    }
                                                                });
                mCompositeSubscription.add(subscription);
            }
        }
    }
}
