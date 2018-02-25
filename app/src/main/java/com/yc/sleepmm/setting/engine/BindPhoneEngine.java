package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.setting.constants.NetConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/13 11:40.
 */

public class BindPhoneEngine extends BaseEngine {
    public BindPhoneEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<UserInfo>> bindPhone(String mobile, String user_id, String code) {
        Map<String, String> params = new HashMap<>();

        params.put("mobile", mobile);
        params.put("user_id", user_id);
        params.put("code", code);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_bind_url, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true);
    }
}
