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

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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

    static final int REQUEST_CODE_FAVORITE_COMIC = 10001;

    @BindView(R.id.favorite_comic_grid) RecyclerView mFavoriteComicGrid;

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
                mFavoriteComicGrid.smoothScrollToPosition(0);
            }
        });

        mFavoriteComicGrid.setHasFixedSize(true);

        mAdapter = new FavoriteGridAdapter(this);
        mFavoriteComicGrid.setAdapter(mAdapter);

        presenter.fetchFavoriteData();
    }

    @Override
    public void updateFavoriteList(List<FavoriteModel> favorites) {
        mAdapter.replaceAll(favorites);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FAVORITE_COMIC) {
            presenter.fetchFavoriteData();
        }
    }
}
