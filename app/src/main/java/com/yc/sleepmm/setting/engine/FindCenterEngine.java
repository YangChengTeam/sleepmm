package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.setting.bean.FindCenterInfo;
import com.yc.sleepmm.setting.constants.NetConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/1/26 09:09.
 */

public class FindCenterEngine extends BaseEngine {
    public FindCenterEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<List<FindCenterInfo>>> getFindCenterInfo(int page, int limit) {

        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("limit", limit + "");

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.app_index_url, new TypeReference<ResultInfo<List<FindCenterInfo>>>() {
        }.getType(), params, true, true, true);


    }
}
