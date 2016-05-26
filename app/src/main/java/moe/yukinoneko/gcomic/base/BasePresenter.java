package moe.yukinoneko.gcomic.base;

import android.content.Context;

import moe.yukinoneko.gcomic.base.IBaseView;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public abstract class BasePresenter<T extends IBaseView> {
    protected CompositeSubscription mCompositeSubscription;
    protected Context mContext;
    protected T iView;

    public BasePresenter(Context context, T iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void init() {
        iView.init();
    }

    public void release() {
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }
}
