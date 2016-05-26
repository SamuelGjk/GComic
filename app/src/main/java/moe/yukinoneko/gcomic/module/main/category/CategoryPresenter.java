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
                                                     iView.setRefreshing(false);
                                                     iView.updateCategoryList(categoryDatas);
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
