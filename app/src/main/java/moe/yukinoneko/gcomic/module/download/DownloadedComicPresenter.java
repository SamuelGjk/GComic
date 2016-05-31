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

package moe.yukinoneko.gcomic.module.download;

import android.content.Context;

import java.util.ArrayList;

import moe.yukinoneko.gcomic.base.BasePresenter;
import moe.yukinoneko.gcomic.database.model.DownloadTaskModel;
import moe.yukinoneko.gcomic.download.DownloadTasksManager;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by SamuelGjk on 2016/5/13.
 */
public class DownloadedComicPresenter extends BasePresenter<IDownloadedComicView> {

    public DownloadedComicPresenter(Context context, IDownloadedComicView iView) {
        super(context, iView);
    }

    void fetchDownloadedComic() {
        Subscription subscription = DownloadTasksManager.getInstance(mContext)
                                                        .getDownloadedComic()
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(new Action1<ArrayList<DownloadTaskModel>>() {
                                                            @Override
                                                            public void call(ArrayList<DownloadTaskModel> downloadTaskModels) {
                                                                iView.updateDownloadedComicList(downloadTaskModels);
                                                            }
                                                        });
        addSubscription(subscription);
    }
}
