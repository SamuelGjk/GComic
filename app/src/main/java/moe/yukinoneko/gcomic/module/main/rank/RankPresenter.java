package moe.yukinoneko.gcomic.module.main.rank;

import android.content.Context;

import java.util.List;

import moe.yukinoneko.gcomic.data.RankData;
import moe.yukinoneko.gcomic.network.GComicApi;
import moe.yukinoneko.gcomic.base.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public class RankPresenter extends BasePresenter<IRankView> {

    public RankPresenter(Context context, IRankView iView) {
        super(context, iView);
    }

    void fetchRankData(int page) {
        Subscription subscription = GComicApi.getInstance()
                                             .fetchRankData(page)
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .subscribe(new Action1<List<RankData>>() {
                                                 @Override
                                                 public void call(List<RankData> rankDatas) {
                                                     iView.setRefreshing(false);
                                                     iView.updateRankList(rankDatas);
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
