package com.yc.sleepmm.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.index.constants.NetContants;
import com.yc.sleepmm.index.model.bean.MusicTypeInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/8 10:22.
 */

public class IndexMusicEngine extends BaseEngine {
    public IndexMusicEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<List<MusicTypeInfo>>> getMusicTypes() {

        return HttpCoreEngin.get(mContext).rxpost(NetContants.MUSIC_TYPE_URL, new TypeReference<ResultInfo<List<MusicTypeInfo>>>() {
        }.getType(), null, true, true, true);

    }


}
