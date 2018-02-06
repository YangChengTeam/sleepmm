package com.yc.sleepmm.vip.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.setting.constants.NetConstant;
import com.yc.sleepmm.vip.bean.GoodsInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/1/25 13:33.
 */

public class GoodInfoEngine extends BaseEngine {
    public GoodInfoEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<List<GoodsInfo>>> getGoodInfoList(String type_id, int page, int limit) {
        Map<String, String> params = new HashMap<>();
        params.put("type_id", type_id);
        params.put("page", page + "");
        params.put("limit", limit + "");

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.goods_index_url, new TypeReference<ResultInfo<List<GoodsInfo>>>() {
        }.getType(), params, true, true, true);

    }

}
