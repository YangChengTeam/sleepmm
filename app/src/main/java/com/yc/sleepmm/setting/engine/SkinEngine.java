package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.kk.securityhttp.engin.BaseEngin;
import com.yc.sleepmm.R;
import com.yc.sleepmm.setting.bean.SkinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2018/1/26 13:43.
 */

public class SkinEngine extends BaseEngin<List<SkinInfo>>{
    public SkinEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return null;
    }


    public Observable<List<SkinInfo>> getSkinInfos(){

        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, List<SkinInfo>>() {
            @Override
            public List<SkinInfo> call(String s) {

                List<SkinInfo> skinInfoList =new ArrayList<>();

                skinInfoList.add(new SkinInfo(R.mipmap.skin_violet,true));
                skinInfoList.add(new SkinInfo(R.mipmap.skin_green,false));
                skinInfoList.add(new SkinInfo(R.mipmap.skin_red,false));
                return skinInfoList;
            }
        });

    }
}
