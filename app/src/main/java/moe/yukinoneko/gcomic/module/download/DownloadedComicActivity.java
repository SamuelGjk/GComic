package moe.yukinoneko.gcomic.module.download;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.ToolBarActivity;
import moe.yukinoneko.gcomic.database.model.DownloadTaskModel;

/**
 * Created by SamuelGjk on 2016/5/13.
 */
public class DownloadedComicActivity extends ToolBarActivity<DownloadedComicPresenter> implements IDownloadedComicView {
    public static final String TAG = "DownloadedComicActivity";

    static final int REQUEST_CODE_DOWNLOADED_COMIC = 10001;

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
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadedComicGrid.smoothScrollToPosition(0);
            }
        });

        downloadedComicGrid.setHasFixedSize(true);

        mAdapter = new DownloadedComicGridAdapter(this);
        downloadedComicGrid.setAdapter(mAdapter);

        presenter.fetchDownloadedComic();
    }

    @Override
    public void updateDownloadedComicList(List<DownloadTaskModel> comics) {
        mAdapter.replaceAll(comics);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DOWNLOADED_COMIC) {
            presenter.fetchDownloadedComic();
        }
    }
}
