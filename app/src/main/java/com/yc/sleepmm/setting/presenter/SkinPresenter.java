package com.yc.sleepmm.setting.presenter;

import android.content.Context;

import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.setting.bean.SkinInfo;
import com.yc.sleepmm.setting.contract.SkinContract;
import com.yc.sleepmm.setting.engine.SkinEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/1/26 13:43.
 */

public class SkinPresenter extends BasePresenter<SkinEngine, SkinContract.View> implements SkinContract.Presenter {
    public SkinPresenter(Context mContext, SkinContract.View view) {
        super(mContext, view);
        mEngine = new SkinEngine(mContext);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getSkinInfos();
    }


    @Override
    public void getSkinInfos() {
        Subscription subscription = mEngine.getSkinInfos().subscribe(new Subscriber<List<SkinInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<SkinInfo> skinInfos) {
                mView.showSkinInfos(skinInfos);
            }
        });

        mSubscriptions.add(subscription);
    }
}
