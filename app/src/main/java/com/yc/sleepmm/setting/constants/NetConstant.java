package com.yc.sleepmm.setting.constants;

import com.yc.sleepmm.base.Config;
import com.yc.sleepmm.base.constant.BaseNetConstant;

/**
 * Created by wanglin  on 2018/2/4 09:07.
 */

public interface NetConstant {

    /**
     * 上传图片
     */
    String upload_url = (Config.IS_DEBUG ? BaseNetConstant.DEBUG_HOST : BaseNetConstant.BASE_HOST) + "upload";

    /**
     * 商品列表
     */
    String goods_index_url = (Config.IS_DEBUG ? BaseNetConstant.DEBUG_HOST : BaseNetConstant.BASE_HOST) + "goods/index";

    /**
     * 支付方式
     */
    String orders_payWay_url = (Config.IS_DEBUG ? BaseNetConstant.DEBUG_HOST : BaseNetConstant.BASE_HOST) + "orders/payWay";
    /**
     * 创建订单
     */
    String orders_init_url = (Config.IS_DEBUG ? BaseNetConstant.DEBUG_HOST : BaseNetConstant.BASE_HOST) + "orders/init";

    /**
     * 更新资料
     */
    String user_update_url = (Config.IS_DEBUG ? BaseNetConstant.DEBUG_HOST : BaseNetConstant.BASE_HOST) + "user/update";

    /**
     * 意见反馈
     */
    String user_suggest_url = (Config.IS_DEBUG ? BaseNetConstant.DEBUG_HOST : BaseNetConstant.BASE_HOST) + "user/suggest";

    /**
     * 应用列表
     */
    String app_index_url = (Config.IS_DEBUG ? BaseNetConstant.DEBUG_HOST : BaseNetConstant.BASE_HOST) + "app/index";

    /**
     * spa收藏
     */
    String spa_myfavorite_url=(Config.IS_DEBUG ? BaseNetConstant.DEBUG_HOST : BaseNetConstant.BASE_HOST) +"spa/myfavorite";

    /**
     * 我的收藏
     */
    String music_myfavorite_url=(Config.IS_DEBUG ? BaseNetConstant.DEBUG_HOST : BaseNetConstant.BASE_HOST)+"music/myfavorite";

}
