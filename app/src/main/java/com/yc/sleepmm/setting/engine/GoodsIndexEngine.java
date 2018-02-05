package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.sleepmm.setting.bean.GoodsInfo;
import com.yc.sleepmm.setting.constants.NetConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/4 09:30.
 */

public class GoodsIndexEngine extends BaseEngin<ResultInfo<List<GoodsInfo>>> {
    public GoodsIndexEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.goods_index_url;
    }

    public Observable<ResultInfo<List<GoodsInfo>>> getGoodsInfo(String type_id, int page, int limit) {
        Map<String, String> params = new HashMap<>();
        params.put("type_id", type_id);
        params.put("page", page + "");
        params.put("limit", limit + "");

        return rxpost(new TypeReference<ResultInfo<List<GoodsInfo>>>() {
        }.getType(), params, true, true, true);
    }
}
