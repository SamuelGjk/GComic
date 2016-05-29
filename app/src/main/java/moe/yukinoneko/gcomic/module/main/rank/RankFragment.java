package moe.yukinoneko.gcomic.module.main.rank;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.BaseFragment;
import moe.yukinoneko.gcomic.data.RankData;
import moe.yukinoneko.gcomic.module.main.MainActivity;
import moe.yukinoneko.gcomic.utils.SnackbarUtils;

/**
 * Created by SamuelGjk on 2016/4/6.
 */
public class RankFragment extends BaseFragment<RankPresenter> implements IRankView, MainActivity.ChildRefresher {

    @BindView(R.id.swipe_target) RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mLayoutManager;
    private RankListAdapter mAdapter;
    private boolean isRefresh;
    private int page;

    @Override
    protected int provideViewLayoutId() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initPresenter() {
        presenter = new RankPresenter(getContext(), this);
        presenter.init();
    }

    @Override
    public void init() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        mLayoutManager = new LinearLayoutManager(getContext());
        mSwipeTarget.setLayoutManager(mLayoutManager);
        mSwipeTarget.setHasFixedSize(true);
        mAdapter = new RankListAdapter(getContext());
        mSwipeTarget.setAdapter(mAdapter);
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && !mSwipeRefreshLayout.isRefreshing() && mLayoutManager.findLastCompletelyVisibleItemPosition() > mAdapter.getItemCount() - 3) {
                    setRefreshing(true);
                    isRefresh = false;
                    presenter.fetchRankData(page);
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
        if (mSwipeRefreshLayout == null) {
            return;
        }

        setRefreshing(true);
        mSwipeTarget.smoothScrollToPosition(0);
        isRefresh = true;
        presenter.fetchRankData(0);
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
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(refreshing);
                }
            }
        }, refreshing ? 0 : 1000);
    }

    @Override
    public void updateRankList(List<RankData> rankDatas) {
        if (isRefresh) {
            page = 1;
            mAdapter.replaceAll(rankDatas);
        } else {
            page++;
            mAdapter.addAll(rankDatas);
        }
    }

    @Override
    public void doChildRefresh() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            doRefresh();
        }
    }
}
