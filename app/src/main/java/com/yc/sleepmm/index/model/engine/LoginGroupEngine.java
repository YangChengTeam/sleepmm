package com.yc.sleepmm.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.index.constants.NetContants;
import com.yc.sleepmm.index.model.bean.UserInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/9 17:24.
 */

public class LoginGroupEngine extends BaseEngine {
    public LoginGroupEngine(Context context) {
        super(context);
    }

    /**
     * 注册账户
     *
     * @param account
     * @param password
     * @param code
     */

    public Observable<ResultInfo<UserInfo>> registerAccount(String account, String password, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", account);
        params.put("code", code);
        params.put("password", password);
        return HttpCoreEngin.get(mContext).rxpost(NetContants.DEBUG_HOST + NetContants.HOST_USER_REGISTER, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 账号密码登录
     *
     * @param account
     * @param password
     */

    public Observable<ResultInfo<UserInfo>> loginAccount(String account, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("mobile", account);
        params.put("password", password);

        return HttpCoreEngin.get(mContext).rxpost(NetContants.DEBUG_HOST + NetContants.HOST_USER_LOGIN, new TypeReference<ResultInfo<UserInfo>>() {
                }.getType(), params, true,
                true, true);
    }


    public Observable<ResultInfo<UserInfo>> findPassword(String phoneNumber, String code, String newPassword) {

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("code", code);
        params.put("new_password", newPassword);
        return HttpCoreEngin.get(mContext).rxpost(NetContants.DEBUG_HOST + NetContants.HOST_USER_FIND_PASSWORD, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true);

    }


    public Observable<ResultInfo<String>> getCode(String phoneNumber) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("user_id", "0");

        return HttpCoreEngin.get(mContext).rxpost(NetContants.DEBUG_HOST + NetContants.HOST_USER_GET_CODE, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * token: 第三方登录凭证
     * sns: 1: QQ；2: 微信openID；3: 微信unionId；4: 微博
     * [nick_name]: 昵称
     * [face]: 头像
     * [user_id]: 用户ID，默认为零，否则绑定至用户
     */
    public Observable<ResultInfo<UserInfo>> snsLogin(String token, String sns, String nick_name, String face) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("sns", sns);
        params.put("nick_name", nick_name);
        params.put("face", face);
        params.put("user_id", APP.getInstance().isLogin() ? APP.getInstance().getUserData().getId() : "0");
        return HttpCoreEngin.get(mContext).rxpost(NetContants.HOST_USER__SNS, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true);

    }
}
