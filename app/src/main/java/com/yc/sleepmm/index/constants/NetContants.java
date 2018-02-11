package com.yc.sleepmm.index.constants;

/**
 * TinyHung@Outlook.com
 * 2018/1/22.
 */

public interface NetContants {

    String BASE_HOST = "http://app.nq6.com/api/index/";

    String DEBUG_HOST = "http://api.sleep.slpi1.com/v1/";
    String HOST_USER_GET_CODE = "user/code";
    String HOST_USER_FIND_PASSWORD = "user/reset";
    String HOST_USER_REGISTER = "user/reg";
    String HOST_USER_LOGIN = "user/login";

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
