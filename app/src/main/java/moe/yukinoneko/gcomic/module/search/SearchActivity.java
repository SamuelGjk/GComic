package moe.yukinoneko.gcomic.module.search;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.ToolBarActivity;
import moe.yukinoneko.gcomic.data.SearchData;
import moe.yukinoneko.gcomic.utils.SnackbarUtils;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public class SearchActivity extends ToolBarActivity<SearchPresenter> implements ISearchView {

    public static final String SEARCH_KEYWORD = "SEARCH_KEYWORD";

    @BindView(R.id.swipe_target) RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mLayoutManager;
    private ResultListAdapter mAdapter;
    private boolean isRefresh;
    private int page;

    private String keyword;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initPresenter() {
        presenter = new SearchPresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        Intent intent = getIntent();

        keyword = intent.getStringExtra(SEARCH_KEYWORD);

        mToolbar.setTitle(keyword);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mSwipeTarget.setLayoutManager(mLayoutManager);
        mSwipeTarget.setHasFixedSize(true);
        mAdapter = new ResultListAdapter(this);
        mSwipeTarget.setAdapter(mAdapter);
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && !mSwipeRefreshLayout.isRefreshing() && mLayoutManager.findLastCompletelyVisibleItemPosition() > mAdapter.getItemCount() - 3) {
                    setRefreshing(true);
                    isRefresh = false;
                    presenter.search(keyword, page);
                }
            }
        });

        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                doRefresh();
            }
        }, 358);
    }

    private void doRefresh() {
        setRefreshing(true);
        mSwipeTarget.smoothScrollToPosition(0);
        isRefresh = true;
        presenter.search(keyword, 0);
    }

    @Override
    public void showMessageSnackbar(String message) {
        SnackbarUtils.showShort(mSwipeRefreshLayout, message);
    }

    @Override
    public void setRefreshing(final boolean refreshing) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(refreshing);
            }
        }, refreshing ? 0 : 1000);
    }

    @Override
    public void updateResultList(List<SearchData> searchDatas) {
        if (isRefresh) {
            page = 1;
            mAdapter.replaceAll(searchDatas);
        } else {
            page++;
            mAdapter.addAll(searchDatas);
        }
    }
}
