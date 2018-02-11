package com.yc.sleepmm.index.constants;

import static com.yc.sleepmm.base.constant.BaseNetConstant.DEBUG_HOST;

/**
 * TinyHung@Outlook.com
 * 2018/1/22.
 */

public interface NetContants {


    String HOST_USER_GET_CODE = DEBUG_HOST + "user/code";
    String HOST_USER_FIND_PASSWORD = DEBUG_HOST + "user/reset";
    String HOST_USER_REGISTER = DEBUG_HOST + "user/reg";
    String HOST_USER_LOGIN = DEBUG_HOST + "user/login";

    /**
     * 第三方登录
     */

    String HOST_USER__SNS = DEBUG_HOST + "user/sns";

    /**
     * 音乐分类
     **/
    String MUSIC_TYPE_URL = DEBUG_HOST + "music/type";
    /**
     * 音乐列表
     */
    String MUSIC_INDEX_URL = DEBUG_HOST + "music/index";

    /**
     * 音乐播放
     */
    String MUSIC_PLAY_URL = DEBUG_HOST + "music/play";

    /**
     * 随便听听
     */
    String MUSIC_RANDOM_URL = DEBUG_HOST + "music/random";

    /**
     * 音乐收藏
     **/
    String MUSIC_FAVORITE_URL = DEBUG_HOST + "music/favorite";

}
