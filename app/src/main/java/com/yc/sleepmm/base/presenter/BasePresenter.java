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

    protected V mView;

    protected Context mContext;

    protected boolean mFirstLoad = true;

    public BasePresenter(V view) {
        this(null, view);
    }

    public BasePresenter(Context mContext, V view) {
        this.mContext = mContext;
        mSubscriptions = new CompositeSubscription();
        this.mView = view;
    }

    @Override
    public void subscribe() {
        loadData(false);

    }

    public void loadData(boolean forceUpdate) {
        loadData(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    public abstract void loadData(boolean forceUpdate, boolean showLoadingUI);

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}
