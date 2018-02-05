
package com.yc.sleepmm.index.rxnet;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

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

    protected Map<String,String> getParams(){
        Map<String,String> params=new HashMap<>();
        params.put("imeil","");//手机串码
        params.put("version","");//软件版本
        params.put("sv","");//系统版本
        params.put("timestamp","");//时间戳
        params.put("app_id","");//应用ID
        params.put("agent_id","");//渠道ID
        return params;
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
