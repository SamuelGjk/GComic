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

package moe.yukinoneko.gcomic.module.gallery;

import android.content.Context;
import android.util.Log;

import java.util.List;

import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.BasePresenter;
import moe.yukinoneko.gcomic.data.ChapterData;
import moe.yukinoneko.gcomic.database.GComicDB;
import moe.yukinoneko.gcomic.database.model.ReadHistoryModel;
import moe.yukinoneko.gcomic.network.GComicApi;
import moe.yukinoneko.gcomic.utils.FileUtlis;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by SamuelGjk on 2016/4/20.
 */
public class GalleryPresenter extends BasePresenter<IGalleryView> {
    public static final String TAG = "GalleryPresenter";

    public GalleryPresenter(Context context, IGalleryView iView) {
        super(context, iView);
    }

    void fetchChapterDetails(int comicId, int chapterId) {
        Log.d(TAG, "NETWORK");
        Subscription subscription = GComicApi.getInstance()
                                             .fetchChapterDetails(comicId, chapterId)
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .subscribe(new Action1<ChapterData>() {
                                                 @Override
                                                 public void call(ChapterData chapterData) {
                                                     iView.updatePagerContent(chapterData.pageUrl, null, false);
                                                 }
                                             }, new Action1<Throwable>() {
                                                 @Override
                                                 public void call(Throwable throwable) {
                                                     iView.showMessageSnackbar(R.string.message_load_error);
                                                     iView.loadError();
                                                 }
                                             });
        addSubscription(subscription);
    }

    void fetchPicturesFromDisk(String path) {
        Log.d(TAG, "DISK");
        Subscription subscription = FileUtlis.readFileContent(path)
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .subscribe(new Action1<List<byte[]>>() {
                                                 @Override
                                                 public void call(List<byte[]> bytes) {
                                                     iView.updatePagerContent(null, bytes, true);
                                                 }
                                             });
        addSubscription(subscription);
    }

    void updateReadHistory(int comicId, int chapterId, int position) {
        Subscription subscription = GComicDB.getInstance(mContext)
                                            .save(new ReadHistoryModel(comicId, chapterId, position))
                                            .subscribe(new Action1<Long>() {
                                                @Override
                                                public void call(Long aLong) {
                                                    // do nothing..
                                                }
                                            });
        addSubscription(subscription);
    }
}
