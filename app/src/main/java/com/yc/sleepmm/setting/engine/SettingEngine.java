package com.yc.sleepmm.setting.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.setting.bean.UploadInfo;
import com.yc.sleepmm.setting.constants.NetConstant;
import com.yc.sleepmm.vip.bean.PayInfo;

import java.io.File;
import java.util.List;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/5 14:10.
 */

public class SettingEngine extends BaseEngine {
    public SettingEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<List<PayInfo>>> getPayInfos() {
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.orders_payWay_url, new TypeReference<ResultInfo<List<PayInfo>>>() {
        }.getType(), null, true, true, true);
    }

    public Observable<ResultInfo<UploadInfo>> uploadInfo(File file, String fileName) {

        UpFileInfo upFileInfo = new UpFileInfo();
        upFileInfo.file = file;
        upFileInfo.filename = fileName;
        upFileInfo.name = "image";

        return HttpCoreEngin.get(mContext).rxuploadFile(NetConstant.upload_url, new TypeReference<ResultInfo<UploadInfo>>() {
        }.getType(), upFileInfo, null, true);

    }
}
