package com.yc.sleepmm.vip.engine;

import android.content.Context;

import com.kk.securityhttp.engin.BaseEngin;
import com.yc.sleepmm.vip.bean.GoodInfo;
import com.yc.sleepmm.vip.bean.PayInfo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2018/1/25 13:33.
 */

public class GoodInfoEngine extends BaseEngin<List<GoodInfo>> {
    public GoodInfoEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return null;
    }

    public Observable<List<GoodInfo>> getGoodInfoList() {
        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, List<GoodInfo>>() {
            @Override
            public List<GoodInfo> call(String s) {
                List<GoodInfo> goodInfos = new ArrayList<>();
                goodInfos.add(new GoodInfo("会员3天", "2"));
                goodInfos.add(new GoodInfo("会员30天", "5"));
                goodInfos.add(new GoodInfo("会员3月", "13"));
                goodInfos.add(new GoodInfo("会员半年", "25"));

                return goodInfos;
            }
        });

    }


    public Observable<List<PayInfo>> getPayInfos() {
        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, List<PayInfo>>() {
            @Override
            public List<PayInfo> call(String s) {

                List<PayInfo> list = new ArrayList<>();
                list.add(new PayInfo("支付宝", "alipay"));
                list.add(new PayInfo("微信", "wxpay"));

                return list;
            }
        });

    }
}
