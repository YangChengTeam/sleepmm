package com.yc.sleepmm.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.index.constants.NetContants;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/9 08:33.
 */

public class EngineUtils {

    /**
     * 播放统计
     **/

    public static Observable<ResultInfo<String>> playStatistics(Context context, String music_id) {
        Map<String, String> params = new HashMap<>();

        params.put("music_id", music_id);

        return HttpCoreEngin.get(context).rxpost(NetContants.MUSIC_PLAY_URL, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 随便听听
     *
     * @param context
     * @return
     */

    public static Observable<ResultInfo<MusicInfo>> randomPlay(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", APP.getInstance().isLogin() ? APP.getInstance().getUserData().getId() : "");
        return HttpCoreEngin.get(context).rxpost(NetContants.MUSIC_RANDOM_URL, new TypeReference<ResultInfo<MusicInfo>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 音乐收藏
     *
     * @param context
     * @param user_id
     * @param music_id
     * @return
     */

    public static Observable<ResultInfo<String>> collectMusic(Context context, String user_id, String music_id) {


        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("music_id", music_id);
        return HttpCoreEngin.get(context).rxpost(NetContants.MUSIC_FAVORITE_URL, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }

}
