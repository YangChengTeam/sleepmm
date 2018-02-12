package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.setting.constants.NetConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/11 09:50.
 */

public class CollectEngine extends BaseEngine {
    public CollectEngine(Context context) {
        super(context);
    }

    // user_id: 用户ID
//    page: 页码
//    limit: 数量
    public Observable<ResultInfo<List<MusicInfo>>> getSpaFavoriteList(int page, int limit) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", APP.getInstance().getUserData().getId());
        params.put("page", page + "");
        params.put("limit", limit + "");
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.spa_myfavorite_url, new TypeReference<ResultInfo<List<MusicInfo>>>() {
        }.getType(), params, true, true, true);
    }


    public Observable<ResultInfo<List<MusicInfo>>> getMusicFavoriteList(int page, int limit) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", APP.getInstance().getUserData().getId());
        params.put("page", page + "");
        params.put("limit", limit + "");
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.music_myfavorite_url, new TypeReference<ResultInfo<List<MusicInfo>>>() {
        }.getType(), params, true, true, true);
    }


}
