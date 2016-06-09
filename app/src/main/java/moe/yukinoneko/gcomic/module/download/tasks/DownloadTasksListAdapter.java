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

package moe.yukinoneko.gcomic.module.download.tasks;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liulishuo.filedownloader.model.FileDownloadStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.data.ComicData;
import moe.yukinoneko.gcomic.database.model.DownloadTaskModel;
import moe.yukinoneko.gcomic.download.DownloadTasksManager;
import moe.yukinoneko.gcomic.module.gallery.GalleryActivity;

/**
 * Created by SamuelGjk on 2016/5/17.
 */
public class DownloadTasksListAdapter extends RecyclerView.Adapter<DownloadTasksListAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<DownloadTaskModel> mData;
    private DownloadTasksManager mDownloadTasksManager;

    private List<ComicData.ChaptersBean.ChapterBean> mFullChapters;

    private Set<DownloadTaskModel> mSelectedTasks;

    private OnItemLongClickListener onItemLongClickListener;

    interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    private ActionMode mActionMode;

    public void setActionMode(ActionMode actionMode) {
        this.mActionMode = actionMode;
    }

    public ActionMode getActionMode() {
        return mActionMode;
    }

    public void quitMultiselectionMode() {
        mActionMode = null;
        mSelectedTasks.clear();
        notifyDataSetChanged();
    }

    public void selectAll() {
        mSelectedTasks.addAll(mData);
        notifyDataSetChanged();
    }

    public void unselectAll() {
        mSelectedTasks.clear();
        notifyDataSetChanged();
    }

    public List<DownloadTaskModel> deleteSelectedTasks() {
        mData.removeAll(mSelectedTasks);
        return new ArrayList<>(mSelectedTasks);
    }

    public DownloadTasksListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        this.mDownloadTasksManager = DownloadTasksManager.getInstance(context);
        this.mSelectedTasks = new HashSet<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_download_task, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.taskModel = mData.get(position);

        holder.update(holder.taskModel.id, position);
        holder.taskName.setText(holder.taskModel.chapterTitle);

        mDownloadTasksManager.updateViewHolder(holder.id, holder);

        holder.taskActionBtn.setEnabled(true);

        if (mDownloadTasksManager.isReady()) {
            final int status = mDownloadTasksManager.getStatus(holder.taskModel.id);
            if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started || status == FileDownloadStatus.connected) {
                // start task, but file not created yet
                holder.updateDownloading(status, mDownloadTasksManager.getSoFar(holder.taskModel.id), mDownloadTasksManager.getTotal(holder.taskModel.id));
            } else if (!mDownloadTasksManager.isExist(holder.taskModel.path)) {
                // not exist file
                holder.updateNotDownloaded(status, 0, 0);
            } else if (mDownloadTasksManager.isDownloaded(status)) {
                // already downloaded and exist
                holder.updateDownloaded();
            } else if (status == FileDownloadStatus.progress) {
                // downloading
                holder.updateDownloading(status, mDownloadTasksManager.getSoFar(holder.taskModel.id), mDownloadTasksManager.getTotal(holder.taskModel.id));
            } else {
                // not start
                holder.updateNotDownloaded(status, mDownloadTasksManager.getSoFar(holder.taskModel.id), mDownloadTasksManager.getTotal(holder.taskModel.id));
            }
        } else {
            holder.taskStatus.setText(R.string.download_tasks_status_loading);
            holder.taskActionBtn.setEnabled(false);
        }

        if (mSelectedTasks.contains(holder.taskModel)) {
            holder.itemView.setBackgroundColor(0x66FF4081);
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.download_task_background_color));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    List<DownloadTaskModel> getAllTasks() {
        return mData;
    }

    void replaceAll(List<DownloadTaskModel> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    void setFullChapters(List<ComicData.ChaptersBean.ChapterBean> chapters) {
        this.mFullChapters = chapters;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, DownloadTasksManager.ITaskViewHolder {

        @BindView(R.id.task_name) AppCompatTextView taskName;
        @BindView(R.id.task_progress_bar) MaterialProgressBar taskPb;
        @BindView(R.id.btn_task_action) AppCompatImageButton taskActionBtn;
        @BindView(R.id.task_status) AppCompatTextView taskStatus;

        DownloadTaskModel taskModel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            taskActionBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_task_action:
                    CharSequence status = taskStatus.getText();
                    if (status.equals(v.getResources().getString(R.string.download_tasks_status_started)) || status.equals(v.getResources().getString(R.string.download_tasks_status_pending))
                            || status.equals(v.getResources().getString(R.string.download_tasks_status_connected)) || status.equals(v.getResources().getString(R.string.download_tasks_status_progress))) {
                        // to pause
                        mDownloadTasksManager.pauseTask(id);
                    } else if (status.equals(v.getResources().getString(R.string.download_tasks_status_paused)) || status.equals(v.getResources().getString(R.string.download_tasks_status_error))
                            || status.equals(v.getResources().getString(R.string.download_tasks_status_not_downloaded))) {
                        // to start
                        final DownloadTaskModel model = mData.get(position);
                        mDownloadTasksManager.startTask(model);

                        mDownloadTasksManager.updateViewHolder(id, ViewHolder.this);
                    }
                    break;

                default:
                    if (mActionMode != null) {
                        if (mSelectedTasks.contains(taskModel)) {
                            mSelectedTasks.remove(taskModel);
                        } else {
                            mSelectedTasks.add(taskModel);
                        }
                        notifyItemChanged(position);
                    } else {
                        ArrayList<ComicData.ChaptersBean.ChapterBean> chapters = new ArrayList<>();
                        int chapterPosition;
                        if (mFullChapters != null && !mFullChapters.isEmpty()) {
                            chapters.addAll(mFullChapters);
                            chapterPosition = mData.get(position).position + (mFullChapters.size() - mData.get(position).chaptersSize);
                        } else {
                            for (DownloadTaskModel task : mData) {
                                ComicData.ChaptersBean.ChapterBean chapter = new ComicData.ChaptersBean.ChapterBean();
                                chapter.chapterId = task.chapterId;
                                chapter.chapterTitle = task.chapterTitle;
                                chapters.add(chapter);
                            }
                            chapterPosition = position;
                        }
                        Intent intent = new Intent(mContext, GalleryActivity.class);
                        intent.putExtra(GalleryActivity.GALLERY_CMOIC_ID, mData.get(0).comicId);
                        intent.putExtra(GalleryActivity.GALLERY_CHAPTER_POSITION, chapterPosition);
                        intent.putExtra(GalleryActivity.GALLERY_CMOIC_FIRST_LETTER, mData.get(0).firstLetter);
                        intent.putParcelableArrayListExtra(GalleryActivity.GALLERY_CHAPTER_LIST, chapters);
                        mContext.startActivity(intent);
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            onItemLongClickListener.onItemLongClick(v, position);
            return false;
        }

        /**
         * download id
         */
        private int id;

        /**
         * ViewHolder position
         */
        private int position;

        public void update(final int id, final int position) {
            this.id = id;
            this.position = position;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public void updateDownloaded() {
            taskPb.setProgress(100);

            taskStatus.setText(R.string.download_tasks_status_completed);
            taskActionBtn.setEnabled(false);
            taskActionBtn.setImageResource(R.mipmap.ic_done_black);
        }

        @Override
        public void updateNotDownloaded(final int status, final long sofar, final long total) {
            if (sofar > 0 && total > 0) {
                final float percent = sofar / (float) total;
                taskPb.setProgress((int) (percent * 100));
            } else {
                taskPb.setProgress(0);
            }

            switch (status) {
                case FileDownloadStatus.error:
                    taskStatus.setText(R.string.download_tasks_status_error);
                    break;
                case FileDownloadStatus.paused:
                    taskStatus.setText(R.string.download_tasks_status_paused);
                    break;
                default:
                    taskStatus.setText(R.string.download_tasks_status_not_downloaded);
                    break;
            }

            taskActionBtn.setImageResource(R.mipmap.ic_play_arrow_white);
        }

        @Override
        public void updateDownloading(final int status, final long sofar, final long total) {
            final float percent = sofar / (float) total;
            taskPb.setProgress((int) (percent * 100));

            switch (status) {
                case FileDownloadStatus.pending:
                    taskStatus.setText(R.string.download_tasks_status_pending);
                    break;
                case FileDownloadStatus.started:
                    taskStatus.setText(R.string.download_tasks_status_started);
                    break;
                case FileDownloadStatus.connected:
                    taskStatus.setText(R.string.download_tasks_status_connected);
                    break;
                case FileDownloadStatus.progress:
                    taskStatus.setText(R.string.download_tasks_status_progress);
                    break;
            }

            taskActionBtn.setImageResource(R.mipmap.ic_pause_white);
        }
    }
}
