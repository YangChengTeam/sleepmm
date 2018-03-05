package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.setting.constants.NetConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/3/5 11:51.
 */

public class PaySuccessEngine extends BaseEngine {
    public PaySuccessEngine(Context context) {
        super(context);
    }

    /**
     *
     * @param phone
     * @param ordersn
     * @return
     */
    public Observable<ResultInfo<String>> uploadPhone(String phone, String ordersn) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("user_id", APP.getInstance().getUid());
        params.put("order_sn", ordersn);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.upload_user_phone, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }
}
