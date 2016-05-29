package moe.yukinoneko.gcomic.module.download.tasks;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.liulishuo.filedownloader.FileDownloadConnectListener;

import java.util.List;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.ToolBarActivity;
import moe.yukinoneko.gcomic.data.ComicData;
import moe.yukinoneko.gcomic.database.model.DownloadTaskModel;
import moe.yukinoneko.gcomic.download.DownloadTasksManager;
import moe.yukinoneko.gcomic.utils.SnackbarUtils;
import moe.yukinoneko.gcomic.widget.DividerItemDecoration;

/**
 * Created by SamuelGjk on 2016/5/13.
 */
public class DownloadTasksActivity extends ToolBarActivity<DownloadTasksPresenter> implements IDownloadTasksView, DownloadTasksListAdapter.OnItemLongClickListener, ActionMode.Callback {
    public static final String DOWMLOADED_COMIC_ID = "DOWMLOADED_COMIC_ID";
    public static final String DOWMLOADED_COMIC_TITLE = "DOWMLOADED_COMIC_TITLE";

    @BindView(R.id.download_task_list) RecyclerView mTaskList;

    private DownloadTasksListAdapter mAdapter;

    private FileDownloadConnectListener fileDownloadConnectListener = new FileDownloadConnectListener() {

        @Override
        public void connected() {
            postNotifyDataChanged();
        }

        @Override
        public void disconnected() {
            postNotifyDataChanged();
        }
    };

    public void postNotifyDataChanged() {
        if (mAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_download_tasks;
    }

    @Override
    protected void initPresenter() {
        presenter = new DownloadTasksPresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        int comicId = getIntent().getIntExtra(DOWMLOADED_COMIC_ID, -1);
        String comicTitle = getIntent().getStringExtra(DOWMLOADED_COMIC_TITLE);

        mToolbar.setTitle(getString(R.string.download_tasks_label, comicTitle));
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskList.smoothScrollToPosition(0);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTaskList.setLayoutManager(layoutManager);
        mTaskList.setHasFixedSize(true);
        mTaskList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mAdapter = new DownloadTasksListAdapter(this);
        mAdapter.setOnItemLongClickListener(this);
        mTaskList.setAdapter(mAdapter);

        DownloadTasksManager.getInstance(this).addServiceConnectListener(fileDownloadConnectListener);

        presenter.fetchDownloadTasks(comicId);
        presenter.fetchComicFullChapters(comicId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_tasks, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_start_all:
                DownloadTasksManager.getInstance(this).startAllTasks(mAdapter.getAllTasks());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.menu_pause_all:
                DownloadTasksManager.getInstance(this).pauseAllTasks(mAdapter.getAllTasks());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        DownloadTasksManager.getInstance(this).removeServiceConnectListener(fileDownloadConnectListener);
        super.onDestroy();
    }

    @Override
    public void showMessageSnackbar(String message) {
        SnackbarUtils.showShort(mTaskList, message);
    }

    @Override
    public void updateDownloadTasksList(List<DownloadTaskModel> tasks) {
        mAdapter.replaceAll(tasks);
    }

    @Override
    public void setComicFullChapters(List<ComicData.ChaptersBean.ChapterBean> chapters) {
        mAdapter.setFullChapters(chapters);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        if (mAdapter.getActionMode() == null) {
            mAdapter.setActionMode(startSupportActionMode(this));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.download_tasks_action_mode, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_select_all:
                mAdapter.selectAll();
                return true;
            case R.id.menu_unselect_all:
                mAdapter.unselectAll();
                return true;
            case R.id.menu_delete_selected:
                presenter.deleteTasks(mAdapter.deleteSelectedTasks());
                mAdapter.getActionMode().finish();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mAdapter.quitMultiselectionMode();
    }
}
