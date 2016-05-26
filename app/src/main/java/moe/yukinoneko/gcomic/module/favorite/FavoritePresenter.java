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
                                            .queryAll(FavoriteModel.class)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<ArrayList<FavoriteModel>>() {
                                                @Override
                                                public void call(ArrayList<FavoriteModel> favoriteModels) {
                                                    iView.setRefreshing(false);
                                                    iView.updateFavoriteList(favoriteModels);
                                                }
                                            });
        addSubscription(subscription);
    }
}
