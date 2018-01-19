package com.yc.sleepmm.base;

import android.support.multidex.MultiDexApplication;

/**
 * Created by wanglin  on 2018/1/10 15:19.
 */

public class APP extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        RxUtils.init(this);
    }
}
