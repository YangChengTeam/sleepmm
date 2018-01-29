package com.yc.sleepmm.base;

import android.os.Build;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.mob.MobSDK;
import com.music.player.lib.manager.MusicPlayerManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.vondear.rxtools.RxTool;
import com.yc.sleepmm.index.bean.UserDataInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by wanglin  on 2018/1/10 15:19.
 */

public class APP extends MultiDexApplication {


    private static APP INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

//        RxUtils.init(this);
        Utils.init(this);

        RxTool.init(this);


        INSTANCE = this;
        Observable.just("").subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                init();
            }
        });
        MusicPlayerManager.getInstance().init(this);
        MusicPlayerManager.getInstance().setDebug(false);

        MobSDK.init(INSTANCE, "23dc6f3757060", "c40753ae2913059b5443faa339dee6ed");
    }


    public static APP getInstance() {
        return INSTANCE;
    }

    public UserDataInfo getUserData() {
        return new UserDataInfo();
    }

    public void setUserData(UserDataInfo userDataInfo, boolean isPlant) {

    }

    public String getUid() {
        return "";
    }


    private void init() {
//        Utils.init(this);
//        Bugly.init(getApplicationContext(), "7e2f7f339a", false);

        UMGameAgent.setDebugMode(false);
        UMGameAgent.init(this);
        UMGameAgent.setPlayerLevel(1);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        //全局信息初始化
        GoagalInfo.get().init(this);

        //设置http默认参数
        String agent_id = "1";
        Map<String, String> params = new HashMap<>();
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id;
        }
        params.put("agent_id", agent_id);
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = getSV();
        params.put("sv", sv);
        params.put("device_type", "2");
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionCode + "");
        }
        HttpConfig.setDefaultParams(params);


    }

    public static String getSV() {
        return Build.MODEL.contains(Build.BRAND) ? Build.MODEL + " " + Build.VERSION.RELEASE : Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
    }
}
