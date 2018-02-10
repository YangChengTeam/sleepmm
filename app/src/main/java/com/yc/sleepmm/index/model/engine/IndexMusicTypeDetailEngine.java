package com.yc.sleepmm.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.index.constants.NetContants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/8 11:02.
 */

public class IndexMusicTypeDetailEngine extends BaseEngine {
    public IndexMusicTypeDetailEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<List<MusicInfo>>> getMusicInfos(String type_id, int page, int limit) {

        Map<String, String> params = new HashMap<>();
        params.put("type_id", type_id);
        params.put("page", page + "");
        params.put("limit", limit + "");
        params.put("user_id", APP.getInstance().isLogin() ? APP.getInstance().getUserData().getId() : "0");

        return HttpCoreEngin.get(mContext).rxpost(NetContants.MUSIC_INDEX_URL, new TypeReference<ResultInfo<List<MusicInfo>>>() {
        }.getType(), params, true, true, true);

    }
}
