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

package moe.yukinoneko.gcomic.module.classify;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.ToolBarActivity;
import moe.yukinoneko.gcomic.data.ClassifyData;
import moe.yukinoneko.gcomic.utils.SnackbarUtils;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public class ClassifyActivity extends ToolBarActivity<ClassifyPresenter> implements IClassifyView {

    public static final String CLASSIFY_TAG_ID = "CLASSIFY_TAG_ID";
    public static final String CLASSIFY_TITLE = "CLASSIFY_TITLE";

    @BindView(R.id.swipe_target) RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mLayoutManager;
    private ClassifyListAdapter mAdapter;
    private boolean isRefresh;
    private int page;

    private int tagId;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_classify;
    }

    @Override
    protected void initPresenter() {
        presenter = new ClassifyPresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        Intent intent = getIntent();

        mToolbar.setTitle(intent.getStringExtra(CLASSIFY_TITLE));

        tagId = intent.getIntExtra(CLASSIFY_TAG_ID, -1);

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
        mAdapter = new ClassifyListAdapter(this);
        mSwipeTarget.setAdapter(mAdapter);
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && !mSwipeRefreshLayout.isRefreshing() && mLayoutManager.findLastCompletelyVisibleItemPosition() > mAdapter.getItemCount() - 3) {
                    setRefreshing(true);
                    isRefresh = false;
                    presenter.fetchClassifyData(tagId, page);
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
        presenter.fetchClassifyData(tagId, 0);
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
    public void updateClassifyList(List<ClassifyData> classifyDatas) {
        if (isRefresh) {
            page = 1;
            mAdapter.replaceAll(classifyDatas);
        } else {
            page++;
            mAdapter.addAll(classifyDatas);
        }
    }
}
