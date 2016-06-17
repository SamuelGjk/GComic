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

package moe.yukinoneko.gcomic.module.details;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.data.ComicData;
import moe.yukinoneko.gcomic.database.model.ReadHistoryModel;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public class ChapterGridAdapter extends RecyclerView.Adapter<ChapterGridAdapter.ViewHolder> {
    private final String TAG = "ChapterGridAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ComicData.ChaptersBean.ChapterBean> mData;

    private List<Integer> mDownloadedChapters;

    private ReadHistoryModel mReadHistory;
    private int mHistoryChapterPosition;
    private int mHistoryBrowsePosition;

    private OnChapterClickListener onChapterClickListener;

    public ChapterGridAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        this.mHistoryBrowsePosition = 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_grid_chapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ComicData.ChaptersBean.ChapterBean chapter = mData.get(position);
        if (chapter.isDownloaded || mDownloadedChapters.contains(chapter.chapterId)) {
            chapter.isDownloaded = true;
            holder.itemView.setBackgroundResource(R.color.colorPrimary);
            ((AppCompatTextView) holder.itemView).setTextColor(0xFFFFFFFF);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_stroke);
            ((AppCompatTextView) holder.itemView).setTextColor(ContextCompat.getColor(mContext, R.color.chapter_text_color));
        }

        if (mReadHistory != null && chapter.chapterId == mReadHistory.chapterId) {
            mHistoryChapterPosition = holder.getAdapterPosition();
            mHistoryBrowsePosition = mReadHistory.browsePosition;
            holder.itemView.setBackgroundResource(R.color.colorAccent);
            ((AppCompatTextView) holder.itemView).setTextColor(0xFFFFFFFF);
        }

        ((AppCompatTextView) holder.itemView).setText(chapter.chapterTitle);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void setDownloadedChapters(List<Integer> chapters) {
        this.mDownloadedChapters = chapters;
    }

    void setReadHistory(ReadHistoryModel readHistory) {
        this.mReadHistory = readHistory;
    }

    void replaceAll(List<ComicData.ChaptersBean.ChapterBean> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    ArrayList<ComicData.ChaptersBean.ChapterBean> getAllChapters() {
        return new ArrayList<>(mData);
    }

    int getHistoryChapterPosition() {
        return mHistoryChapterPosition;
    }

    int getHistoryBrowsePosition() {
        return mHistoryBrowsePosition;
    }

    List<ComicData.ChaptersBean.ChapterBean> getData() {
        return mData;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onChapterClickListener.onChapterClick(getAdapterPosition());
        }
    }

    interface OnChapterClickListener {
        void onChapterClick(int position);
    }

    public void setOnChapterClickListener(OnChapterClickListener listener) {
        this.onChapterClickListener = listener;
    }
}
