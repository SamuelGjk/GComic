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

package moe.yukinoneko.gcomic.base;

import android.content.Context;

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
