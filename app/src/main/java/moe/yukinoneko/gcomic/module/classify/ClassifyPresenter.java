package moe.yukinoneko.gcomic.module.classify;

import android.content.Context;

import java.util.List;

import moe.yukinoneko.gcomic.data.ClassifyData;
import moe.yukinoneko.gcomic.network.GComicApi;
import moe.yukinoneko.gcomic.base.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public class ClassifyPresenter extends BasePresenter<IClassifyView> {

    public ClassifyPresenter(Context context, IClassifyView iView) {
        super(context, iView);
    }

    void fetchClassifyData(int tagId, int page) {
        Subscription subscription = GComicApi.getInstance()
                                             .fetchClassifyData(tagId, page)
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .subscribe(new Action1<List<ClassifyData>>() {
                                                 @Override
                                                 public void call(List<ClassifyData> classifyDatas) {
                                                     iView.setRefreshing(false);
                                                     iView.updateClassifyList(classifyDatas);
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
