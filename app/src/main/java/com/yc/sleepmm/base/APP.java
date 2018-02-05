package com.yc.sleepmm.base;

import android.os.Build;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
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
import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;


/**
 * Created by wanglin  on 2018/1/10 15:19.
 */

public class APP extends MultiDexApplication {


    private static APP INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

//        RxUtils.init(this);

        INSTANCE = this;
        Observable.just("").subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                init();
            }
        });
        MusicPlayerManager.getInstance().init(this);
        MusicPlayerManager.getInstance().setDebug(true);
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
        Utils.init(this);

        RxTool.init(this);
        UMGameAgent.setDebugMode(false);
        UMGameAgent.init(this);
        UMGameAgent.setPlayerLevel(1);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        GoagalInfo.get().publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA5KaI8l7xplShIEB0Pwgm\n" +
                "MRX/3uGG9BDLPN6wbMmkkO7H1mIOXWB/Jdcl4/IMEuUDvUQyv3P+erJwZ1rvNsto\n" +
                "hXdhp2G7IqOzH6d3bj3Z6vBvsXP1ee1SgqUNrjX2dn02hMJ2Swt4ry3n3wEWusaW\n" +
                "mev4CSteSKGHhBn5j2Z5B+CBOqPzKPp2Hh23jnIH8LSbXmW0q85a851BPwmgGEan\n" +
                "5HBPq04QUjo6SQsW/7dLaaAXfUTYETe0HnpLaimcHl741ftGyrQvpkmqF93WiZZX\n" +
                "wlcDHSprf8yW0L0KA5jIwq7qBeu/H/H5vm6yVD5zvUIsD7htX0tIcXeMVAmMXFLX\n" +
                "35duvYDpTYgO+DsMgk2Q666j6OcEDVWNBDqGHc+uPvYzVF6wb3w3qbsqTnD0qb/p\n" +
                "WxpEdgK2BMVz+IPwdP6hDsDRc67LVftYqHJLKAfQt5T6uRImDizGzhhfIfJwGQxI\n" +
                "7TeJq0xWIwB+KDUbFPfTcq0RkaJ2C5cKIx08c7lYhrsPXbW+J/W4M5ZErbwcdj12\n" +
                "hrfV8TPx/RgpJcq82otrNthI3f4QdG4POUhdgSx4TvoGMTk6CnrJwALqkGl8OTfP\n" +
                "KojOucENSxcA4ERtBw4It8/X39Mk0aqa8/YBDSDDjb+gCu/Em4yYvrattNebBC1z\n" +
                "ulK9uJIXxVPi5tNd7KlwLRMCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----";

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
        initSkin();

    }

    public static String getSV() {
        return Build.MODEL.contains(Build.BRAND) ? Build.MODEL + " " + Build.VERSION.RELEASE : Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
    }

    private void initSkin() {
        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(true)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(true)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }
}
