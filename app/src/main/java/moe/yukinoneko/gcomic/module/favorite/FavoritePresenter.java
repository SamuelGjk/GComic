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

import android.content.Context;

import java.util.ArrayList;

import moe.yukinoneko.gcomic.base.BasePresenter;
import moe.yukinoneko.gcomic.database.GComicDB;
import moe.yukinoneko.gcomic.database.model.FavoriteModel;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by SamuelGjk on 2016/5/9.
 */
public class FavoritePresenter extends BasePresenter<IFavoriteView> {

    public FavoritePresenter(Context context, IFavoriteView iView) {
        super(context, iView);
    }

    void fetchFavoriteData() {
        Subscription subscription = GComicDB.getInstance(mContext)
                                            .queryAllDesc(FavoriteModel.class, "favoriteTime")
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<ArrayList<FavoriteModel>>() {
                                                @Override
                                                public void call(ArrayList<FavoriteModel> favoriteModels) {
                                                    iView.updateFavoriteList(favoriteModels);
                                                }
                                            });
        addSubscription(subscription);
    }
}
