package com.yc.sleepmm.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.index.constants.NetContants;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.setting.constants.NetConstant;

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


    /**
     * 获取验证码
     */
    public static Observable<ResultInfo<String>> getCode(Context context, String phoneNumber) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("user_id", APP.getInstance().getUserData() != null ? APP.getInstance().getUserData().getId() : "0");

        return HttpCoreEngin.get(context).rxpost(NetContants.HOST_USER_GET_CODE, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 获取用户信息
     *
     * @param user_id
     * @return
     */

    public static Observable<ResultInfo<UserInfo>> getUserInfo(Context context, String user_id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);

        return HttpCoreEngin.get(context).rxpost(NetConstant.user_info_url, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true);


    }
}
