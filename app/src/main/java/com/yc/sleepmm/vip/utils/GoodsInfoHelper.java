package com.yc.sleepmm.vip.utils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yc.sleepmm.setting.constants.SpConstant;
import com.yc.sleepmm.vip.bean.GoodsInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/2/6 15:40.
 */

public class GoodsInfoHelper {

    private static List<GoodsInfo> mGoodsInfoList;

    public static List<GoodsInfo> getGoodsInfoList() {
        if (mGoodsInfoList != null) {
            return mGoodsInfoList;
        }
        try {
            mGoodsInfoList = JSON.parseArray(SPUtils.getInstance().getString(SpConstant.GOODS_INFOS), GoodsInfo.class);
        } catch (Exception e) {
            LogUtils.e("error:--> " + e.getMessage());
        }

        return mGoodsInfoList;
    }

    public static void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
        try {
            String str = JSON.toJSONString(goodsInfoList);
            SPUtils.getInstance().put(SpConstant.GOODS_INFOS, str);
        } catch (Exception e) {
            LogUtils.e("error:--> " + e.getMessage());
        }
        mGoodsInfoList = goodsInfoList;
    }
}
