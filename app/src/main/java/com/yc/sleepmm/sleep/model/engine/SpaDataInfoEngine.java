package com.yc.sleepmm.sleep.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.sleep.constants.NetConstant;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;


public class SpaDataInfoEngine extends BaseEngine {
    public SpaDataInfoEngine(Context context) {
        super(context);
    }
    //分类
    public Observable<ResultInfo<List<SpaDataInfo>>> getSpaDataInfo() {
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.SPA_DATA_LIST_URL, new TypeReference<ResultInfo<List<SpaDataInfo>>>() {
        }.getType(), null, true, true, true);
    }

    //列表
    public Observable<ResultInfo<List<SpaItemInfo>>> getSpaItemList(String typeId,int page,int limit) {

        Map<String, String> params = new HashMap<>();
        params.put("type_id", typeId);
        params.put("page", page+"");
        params.put("limit", limit+"");
        params.put("user_id", APP.getInstance().isLogin() ? APP.getInstance().getUserData().getId() : "0");
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.SPA_ITEM_LIST_URL, new TypeReference<ResultInfo<List<SpaItemInfo>>>() {
        }.getType(), params, true, true, true);

    }
}
