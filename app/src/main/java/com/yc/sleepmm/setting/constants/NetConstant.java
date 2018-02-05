package com.yc.sleepmm.setting.constants;

import com.yc.sleepmm.base.Config;

/**
 * Created by wanglin  on 2018/2/4 09:07.
 */

public interface NetConstant {

    String debug_url = "";

    String base_url = "http://api.sleep.slpi1.com/v1/";


    /**
     * 上传图片
     */
    String upload_url = (Config.IS_DEBUG ? debug_url : base_url) + "upload";

    /**
     * 商品列表
     */
    String goods_index_url = (Config.IS_DEBUG ? debug_url : base_url) + "goods/index";

    /**
     * 支付方式
     */
    String orders_payWay_url = (Config.IS_DEBUG ? debug_url : base_url) + "orders/payWay";
    /**
     * 创建订单
     */
    String orders_init_url = (Config.IS_DEBUG ? debug_url : base_url) + "orders/init";
}
