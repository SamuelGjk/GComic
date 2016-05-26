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
