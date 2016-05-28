package moe.yukinoneko.gcomic.module.details;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.data.ComicData;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public class ChapterGridAdapter extends RecyclerView.Adapter<ChapterGridAdapter.ViewHolder> {
    private final String TAG = "ChapterGridAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ComicData.ChaptersBean.ChapterBean> mData;

    private List<Integer> mDownloadedChapters;

    private OnChapterClickListener onChapterClickListener;

    public ChapterGridAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
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

    void replaceAll(List<ComicData.ChaptersBean.ChapterBean> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
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
            onChapterClickListener.onChapterClick(new ArrayList<>(mData), getAdapterPosition());
        }
    }

    interface OnChapterClickListener {
        void onChapterClick(ArrayList<ComicData.ChaptersBean.ChapterBean> chapters, int position);
    }

    public void setOnChapterClickListener(OnChapterClickListener listener) {
        this.onChapterClickListener = listener;
    }
}
