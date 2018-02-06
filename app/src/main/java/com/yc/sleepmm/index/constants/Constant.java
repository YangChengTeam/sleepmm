package com.yc.sleepmm.index.constants;

import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.index.util.FileUtils;

/**
 * TinyHung@Outlook.com
 * 2018/1/22.
 */

public interface Constant {

    String PATH_DATA = FileUtils.createRootPath(APP.getInstance()) + "sleep/cache/";

    //登录意图
    int INTENT_LOGIN_REQUESTCODE = 111;
    //登录成功意图
    int INTENT_LOGIN_RESULTCODE = 222;
    //话题列表登录意图
    int INTENT_LOGIN_TOPIC = 333;
    String INTENT_LOGIN_STATE = "login_state";

    String SP_USER_USERINFO = "sp_user_userinfo";
    String RX_LOGIN_SUCCESS = "rx_login_success";
}
