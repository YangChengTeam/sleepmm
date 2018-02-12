package com.yc.sleepmm.index.constants;

import static com.yc.sleepmm.base.constant.BaseNetConstant.BASE_HOST;
import static com.yc.sleepmm.base.constant.BaseNetConstant.DEBUG_HOST;
import static com.yc.sleepmm.base.constant.BaseNetConstant.IS_DEBUG;

/**
 * TinyHung@Outlook.com
 * 2018/1/22.
 */

public interface NetContants {


    String HOST_USER_GET_CODE = (IS_DEBUG ? DEBUG_HOST : BASE_HOST) + "user/code";
    String HOST_USER_FIND_PASSWORD = (IS_DEBUG ? DEBUG_HOST : BASE_HOST) + "user/reset";
    String HOST_USER_REGISTER = (IS_DEBUG ? DEBUG_HOST : BASE_HOST) + "user/reg";
    String HOST_USER_LOGIN = (IS_DEBUG ? DEBUG_HOST : BASE_HOST) + "user/login";

    /**
     * 第三方登录
     */

    String HOST_USER__SNS = (IS_DEBUG ? DEBUG_HOST : BASE_HOST) + "user/sns";

    /**
     * 音乐分类
     **/
    String MUSIC_TYPE_URL = (IS_DEBUG ? DEBUG_HOST : BASE_HOST) + "music/type";
    /**
     * 音乐列表
     */
    String MUSIC_INDEX_URL = (IS_DEBUG ? DEBUG_HOST : BASE_HOST) + "music/index";

    /**
     * 音乐播放
     */
    String MUSIC_PLAY_URL = (IS_DEBUG ? DEBUG_HOST : BASE_HOST) + "music/play";

    /**
     * 随便听听
     */
    String MUSIC_RANDOM_URL = (IS_DEBUG ? DEBUG_HOST : BASE_HOST) + "music/random";

    /**
     * 音乐收藏
     **/
    String MUSIC_FAVORITE_URL = (IS_DEBUG ? DEBUG_HOST : BASE_HOST) + "music/favorite";

}
