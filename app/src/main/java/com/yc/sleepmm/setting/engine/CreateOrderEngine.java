package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.pay.OrderInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.sleepmm.setting.constants.NetConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/4 10:35.
 */

public class CreateOrderEngine extends BaseEngin<ResultInfo<OrderInfo>> {
    public CreateOrderEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.orders_init_url;
    }

    public Observable<ResultInfo<OrderInfo>> getOrderInfos(String user_id, String title, String money, String pay_way_name) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("title", title);
        params.put("money", money);
        params.put("pay_way_name", pay_way_name);
        params.put("goods_list", "");

        return rxpost(new TypeReference<ResultInfo<OrderInfo>>() {
        }.getType(), params, true, true, true);
    }
}
