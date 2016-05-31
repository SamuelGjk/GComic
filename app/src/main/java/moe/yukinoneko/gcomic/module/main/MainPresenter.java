package moe.yukinoneko.gcomic.module.main;

import android.content.Context;

import java.util.ArrayList;

import moe.yukinoneko.gcomic.base.BasePresenter;
import moe.yukinoneko.gcomic.database.GComicDB;
import moe.yukinoneko.gcomic.database.model.SearchHistoryModel;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public class MainPresenter extends BasePresenter<IMainView> {
    public MainPresenter(Context context, IMainView iView) {
        super(context, iView);
    }

    void addSearchHistory() {
        Subscription subscription = GComicDB.getInstance(mContext)
                                            .insert(new SearchHistoryModel(iView.getSearchKeyword()
                                                                                .hashCode(), iView.getSearchKeyword()))
                                            .subscribe(new Action1<Long>() {
                                                @Override
                                                public void call(Long aLong) {
                                                    // do nothing..
                                                }
                                            });
        addSubscription(subscription);
    }

    void removeSearchHistory(SearchHistoryModel history) {
        Subscription subscription = GComicDB.getInstance(mContext)
                                            .delete(history)
                                            .subscribe(new Action1<Integer>() {
                                                @Override
                                                public void call(Integer integer) {
                                                    // do nothing..
                                                }
                                            });
        addSubscription(subscription);
    }

    void fetchSearchHistory() {
        Subscription subscription = GComicDB.getInstance(mContext)
                                            .queryByLike(SearchHistoryModel.class, "keyword", new String[]{ iView.getSearchKeyword() + "%" })
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<ArrayList<SearchHistoryModel>>() {
                                                @Override
                                                public void call(ArrayList<SearchHistoryModel> searchHistoryModels) {
                                                    iView.updateSearchSuggestions(searchHistoryModels);
                                                }
                                            });
        addSubscription(subscription);
    }
}
