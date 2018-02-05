package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.sleepmm.setting.bean.UploadInfo;
import com.yc.sleepmm.setting.constants.NetConstant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/4 09:20.
 */

public class UploadEngine extends BaseEngin<ResultInfo<UploadInfo>> {
    public UploadEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.upload_url;
    }

    public Observable<ResultInfo<UploadInfo>> getUploadInfo(File file) {

        Map<String, String> params = new HashMap<>();
        UpFileInfo upFileInfo = new UpFileInfo();
        upFileInfo.file = file;

        return rxuploadFile(new TypeReference<ResultInfo<UploadInfo>>() {
        }.getType(), upFileInfo, params, true);
//                rxpost(new TypeReference<ResultInfo<UploadInfo>>(){}.getType(),params,false,true,true);

    }
}
