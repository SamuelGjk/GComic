package moe.yukinoneko.gcomic.module.download;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.ToolBarActivity;
import moe.yukinoneko.gcomic.database.model.DownloadTaskModel;

/**
 * Created by SamuelGjk on 2016/5/13.
 */
public class DownloadedComicActivity extends ToolBarActivity<DownloadedComicPresenter> implements IDownloadedComicView {
    @BindView(R.id.downloaded_comic_grid) RecyclerView downloadedComicGrid;

    private DownloadedComicGridAdapter mAdapter;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_downloaded_comic;
    }

    @Override
    protected void initPresenter() {
        presenter = new DownloadedComicPresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        downloadedComicGrid.setLayoutManager(layoutManager);
        downloadedComicGrid.setHasFixedSize(true);

        mAdapter = new DownloadedComicGridAdapter(this);
        downloadedComicGrid.setAdapter(mAdapter);

        presenter.fetchDownloadedComic();
    }

    @Override
    public void updateDownloadedComicList(List<DownloadTaskModel> comics) {
        mAdapter.replaceAll(comics);
    }
}
