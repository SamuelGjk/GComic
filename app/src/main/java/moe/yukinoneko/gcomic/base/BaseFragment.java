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

package moe.yukinoneko.gcomic.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {

    protected T presenter;

    protected Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(provideViewLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initPresenter();
        return view;
    }

    protected abstract int provideViewLayoutId();

    protected abstract void initPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.release();
        unbinder.unbind();
    }
}
