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

package moe.yukinoneko.gcomic.module.search;

import android.content.Context;

import java.util.List;

import moe.yukinoneko.gcomic.base.BasePresenter;
import moe.yukinoneko.gcomic.data.SearchData;
import moe.yukinoneko.gcomic.network.GComicApi;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public class SearchPresenter extends BasePresenter<ISearchView> {

    public SearchPresenter(Context context, ISearchView iView) {
        super(context, iView);
    }

    void search(String keyword, int page) {
        Subscription subscription = GComicApi.getInstance()
                                             .search(keyword, page)
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .subscribe(new Action1<List<SearchData>>() {
                                                 @Override
                                                 public void call(List<SearchData> searchDatas) {
                                                     iView.setRefreshing(false);
                                                     iView.updateResultList(searchDatas);
                                                 }
                                             }, new Action1<Throwable>() {
                                                 @Override
                                                 public void call(Throwable throwable) {
                                                     iView.setRefreshing(false);
                                                     iView.showMessageSnackbar(throwable.getMessage());
                                                 }
                                             });
        addSubscription(subscription);
    }
}
