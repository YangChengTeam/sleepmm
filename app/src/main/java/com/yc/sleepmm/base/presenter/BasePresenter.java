package com.yc.sleepmm.base.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by wanglin  on 2018/1/24 16:38.
 */

public abstract class BasePresenter<M, V extends IView> implements IPresenter {
    @NonNull
    protected CompositeSubscription mSubscriptions;

    protected M mEngine;

    protected V view;

    protected Context mContext;
    protected boolean mFirstLoad = true;

    public BasePresenter(V view) {
        this(null, view);
    }

    public BasePresenter(Context mContext, V view) {
        this.view = view;
        this.mContext = mContext;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        loadData(mFirstLoad);
        mFirstLoad = false;
    }

    protected abstract void loadData(boolean forceUpdate);

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }
}
