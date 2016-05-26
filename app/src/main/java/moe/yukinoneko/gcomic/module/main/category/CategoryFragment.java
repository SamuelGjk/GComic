package moe.yukinoneko.gcomic.module.main.category;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.BaseFragment;
import moe.yukinoneko.gcomic.data.CategoryData;
import moe.yukinoneko.gcomic.module.main.MainActivity;
import moe.yukinoneko.gcomic.utils.SnackbarUtils;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public class CategoryFragment extends BaseFragment<CategoryPresenter> implements ICategoryView, MainActivity.ChildRefresher {

    @BindView(R.id.swipe_target) RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    private GridLayoutManager mLayoutManager;
    private CategoryGridAdapter mAdapter;

    @Override
    protected int provideViewLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initPresenter() {
        presenter = new CategoryPresenter(getContext(), this);
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

        mLayoutManager = new GridLayoutManager(getContext(), 3);
        mSwipeTarget.setLayoutManager(mLayoutManager);
        mSwipeTarget.setHasFixedSize(true);
        mAdapter = new CategoryGridAdapter(getContext());
        mSwipeTarget.setAdapter(mAdapter);

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
        presenter.fetchCategoryData();
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
    public void updateCategoryList(List<CategoryData> categoryDatas) {
        mAdapter.replaceAll(categoryDatas);
    }

    @Override
    public void doChildRefresh() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            doRefresh();
        }
    }
}
