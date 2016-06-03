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

package moe.yukinoneko.gcomic.module.main.category;

import android.content.Context;

import java.util.List;

import moe.yukinoneko.gcomic.data.CategoryData;
import moe.yukinoneko.gcomic.network.GComicApi;
import moe.yukinoneko.gcomic.base.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public class CategoryPresenter extends BasePresenter<ICategoryView> {
    public CategoryPresenter(Context context, ICategoryView iView) {
        super(context, iView);
    }

    void fetchCategoryData() {
        Subscription subscription = GComicApi.getInstance()
                                             .fetchCategoryData()
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .subscribe(new Action1<List<CategoryData>>() {
                                                 @Override
                                                 public void call(List<CategoryData> categoryDatas) {
                                                     iView.updateCategoryList(categoryDatas);
                                                     iView.setRefreshing(false);
                                                 }
                                             }, new Action1<Throwable>() {
                                                 @Override
                                                 public void call(Throwable throwable) {
                                                     iView.showMessageSnackbar(throwable.getMessage());
                                                     iView.setRefreshing(false);
                                                 }
                                             });
        addSubscription(subscription);
    }
}
