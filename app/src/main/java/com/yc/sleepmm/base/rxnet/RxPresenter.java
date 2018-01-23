
package com.yc.sleepmm.base.rxnet;
import android.content.Context;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * TinyHung@Outlook.com
 * 2017/3/22.
 * 基于RX生命周期控制
 */

public class RxPresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {


    protected final Context mContext;

    public RxPresenter(Context context){
        this.mContext=context;
    }
    protected T mView;
    protected CompositeSubscription mCompositeSubscription;

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
