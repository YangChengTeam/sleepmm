package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.kk.securityhttp.engin.BaseEngin;
import com.yc.sleepmm.R;
import com.yc.sleepmm.setting.bean.FindCenterInfo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2018/1/26 09:09.
 */

public class FindCenterEngine extends BaseEngin<List<FindCenterInfo>> {
    public FindCenterEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return null;
    }

    public Observable<List<FindCenterInfo>> getFindCenterInfo() {

        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, List<FindCenterInfo>>() {
            @Override
            public List<FindCenterInfo> call(String s) {
                List<FindCenterInfo> findCenterInfos = new ArrayList<>();
                findCenterInfos.add(new FindCenterInfo(R.mipmap.download_app_icon, "说说英语", "说说英语是一款中小学英语智能点读学习软件，英语老师教学的好助手"));
                findCenterInfos.add(new FindCenterInfo(R.mipmap.download_app_icon, "小学英语音标课堂", "小学英语音标课堂是一款中小学英语智能点读学习软件，英语老师教学的好助手"));
                findCenterInfos.add(new FindCenterInfo(R.mipmap.download_app_icon, "初中英语考试", "初中英语考试是一款中小学英语智能点读学习软件，英语老师教学的好助手"));
                findCenterInfos.add(new FindCenterInfo(R.mipmap.download_app_icon, "小学拼音点读", "小学拼音点读是一款中小学英语智能点读学习软件，英语老师教学的好助手"));


                return findCenterInfos;
            }
        });

    }
}
