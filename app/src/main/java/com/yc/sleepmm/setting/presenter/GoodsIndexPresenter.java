package com.yc.sleepmm.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.setting.bean.GoodsInfo;
import com.yc.sleepmm.setting.contract.GoodsIndexContract;
import com.yc.sleepmm.setting.engine.GoodsIndexEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/2/4 09:42.
 */

public class GoodsIndexPresenter extends BasePresenter<GoodsIndexEngine, GoodsIndexContract.View> implements GoodsIndexContract.Presenter {
    public GoodsIndexPresenter(Context mContext, GoodsIndexContract.View view) {
        super(mContext, view);
        mEngine = new GoodsIndexEngine(mContext);
    }


    @Override
    protected void loadData(boolean forceUpdate) {

    }

    @Override
    public void getGoodsInfoList(String type_id, int page, int limit) {
        Subscription subscription = mEngine.getGoodsInfo(type_id, page, limit).subscribe(new Subscriber<ResultInfo<List<GoodsInfo>>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<GoodsInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    List<GoodsInfo> goodsInfos = listResultInfo.data;
                    mView.showGoodsInfos(goodsInfos);
                }
            }
        });
        mSubscriptions.add(subscription);

    }
}
