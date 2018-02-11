package com.yc.sleepmm.sleep.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.sleep.constants.NetConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/11 08:57.
 */

public class SpaDetailEngine extends BaseEngine {
    public SpaDetailEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<MusicInfo>> getSpaDetailInfo(String spa_id) {

        Map<String, String> params = new HashMap<>();
        params.put("spa_id", spa_id);
        params.put("user_id", APP.getInstance().isLogin() ? APP.getInstance().getUserData().getId() : "");
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.SPA_INFO_DETAIL_URL, new TypeReference<ResultInfo<MusicInfo>>() {
        }.getType(), params, true, true, true);

    }
}
