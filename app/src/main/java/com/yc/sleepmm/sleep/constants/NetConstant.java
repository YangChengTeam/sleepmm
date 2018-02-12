package com.yc.sleepmm.sleep.constants;

import com.yc.sleepmm.base.constant.BaseNetConstant;

/**
 * Created by wanglin  on 2018/2/11 08:53.
 */

public interface NetConstant {

    /**
     * spa分类
     */
    String SPA_DATA_LIST_URL = BaseNetConstant.DEBUG_HOST + "spa/type";

    /**
     * spa列表
     */
    String SPA_ITEM_LIST_URL = BaseNetConstant.DEBUG_HOST + "spa/index";

    /**
     * spa详情
     */
    String SPA_INFO_DETAIL_URL = BaseNetConstant.DEBUG_HOST + "spa/info";
}
