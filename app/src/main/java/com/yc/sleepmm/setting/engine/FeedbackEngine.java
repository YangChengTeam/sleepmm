package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.setting.constants.NetConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/7 14:32.
 */

public class FeedbackEngine extends BaseEngine {
    public FeedbackEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> createSuggest(String content,String user_id){

        Map<String,String> params= new HashMap<>();

        params.put("content",content);
        params.put("user_id",user_id);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_suggest_url,new TypeReference<ResultInfo<String>>(){}.getType(),params,
                true,true,true);
    }
}
