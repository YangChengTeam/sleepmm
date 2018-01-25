package com.yc.sleepmm.pay.engine;

import android.content.Context;

import com.kk.securityhttp.engin.BaseEngin;
import com.yc.sleepmm.pay.bean.GoodInfo;

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
}
