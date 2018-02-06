package com.yc.sleepmm.vip.utils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yc.sleepmm.setting.constants.SpConstant;
import com.yc.sleepmm.vip.bean.PayInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/2/5 13:59.
 */

public class PaywayHelper {

    private static List<PayInfo> mPaywayInfo;

    public static List<PayInfo> getmPaywayInfo() {
        if (mPaywayInfo != null) {
            return mPaywayInfo;
        }
        String result = SPUtils.getInstance().getString(SpConstant.PAYWAY_INFOS);
        try {

            mPaywayInfo = JSON.parseArray(result, PayInfo.class);
        } catch (Exception e) {
            LogUtils.e("error:->>" + e.getMessage());
        }

        return mPaywayInfo;
    }

    public static void setmPaywayInfo(List<PayInfo> paywayInfo) {

        try {

            SPUtils.getInstance().put(SpConstant.PAYWAY_INFOS, JSON.toJSONString(mPaywayInfo));
        } catch (Exception e) {
            LogUtils.e("error:->>" + e.getMessage());
        }

        mPaywayInfo = paywayInfo;
    }
}
