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
 * Created by wanglin  on 2018/2/12 10:53.
 */

public class VipEngine extends BaseEngine {
    public VipEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<UserInfo>> getUserInfo(String user_id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_info_url, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true);


    }
}
