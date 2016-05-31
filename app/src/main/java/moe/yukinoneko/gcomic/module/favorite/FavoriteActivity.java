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

package moe.yukinoneko.gcomic.module.favorite;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.ToolBarActivity;
import moe.yukinoneko.gcomic.database.model.FavoriteModel;

/**
 * Created by SamuelGjk on 2016/5/9.
 */
public class FavoriteActivity extends ToolBarActivity<FavoritePresenter> implements IFavoriteView {

    @BindView(R.id.swipe_target) RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    private FavoriteGridAdapter mAdapter;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_favorite;
    }

    @Override
    protected void initPresenter() {
        presenter = new FavoritePresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeTarget.smoothScrollToPosition(0);
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setHasFixedSize(true);

        mAdapter = new FavoriteGridAdapter(this);
        mSwipeTarget.setAdapter(mAdapter);

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
        presenter.fetchFavoriteData();
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
    public void updateFavoriteList(List<FavoriteModel> favorites) {
        mAdapter.replaceAll(favorites);
    }
}
