package com.yc.sleepmm.base;

import android.support.multidex.MultiDexApplication;

import com.mob.MobSDK;
import com.music.player.lib.manager.MusicPlayerManager;
import com.yc.sleepmm.index.bean.UserDataInfo;


/**
 * Created by wanglin  on 2018/1/10 15:19.
 */

public class APP extends MultiDexApplication {


    private static APP INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
//        RxUtils.init(this);
        MusicPlayerManager.getInstance().init(this);
        MusicPlayerManager.getInstance().setDebug(true);
        INSTANCE=this;
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
}
