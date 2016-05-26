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
