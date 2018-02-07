package com.yc.sleepmm.setting.engine;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.index.bean.UserInfo;
import com.yc.sleepmm.setting.bean.UploadInfo;
import com.yc.sleepmm.setting.constants.NetConstant;
import com.yc.sleepmm.vip.bean.GoodsInfo;
import com.yc.sleepmm.vip.bean.PayInfo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/2/5 14:10.
 */

public class SettingEngine extends BaseEngine {
    public SettingEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<List<PayInfo>>> getPayInfos() {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", APP.getInstance().getUserData() != null ? APP.getInstance().getUserData().getId() : "0");

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.orders_payWay_url, new TypeReference<ResultInfo<List<PayInfo>>>() {
        }.getType(), params, true, true, true);
    }

    public Observable<ResultInfo<UploadInfo>> uploadInfo(File file, String fileName) {

        UpFileInfo upFileInfo = new UpFileInfo();
        upFileInfo.file = file;
        upFileInfo.filename = fileName;
        upFileInfo.name = "image";

        return HttpCoreEngin.get(mContext).rxuploadFile(NetConstant.upload_url, new TypeReference<ResultInfo<UploadInfo>>() {
        }.getType(), upFileInfo, null, true);

    }

    public Observable<ResultInfo<UserInfo>> updateInfo(String user_id, String nick_name, String face, String password) {


        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        if (!TextUtils.isEmpty(nick_name)) params.put("nick_name", nick_name);
        if (!TextUtils.isEmpty(face)) params.put("face", face);
        if (!TextUtils.isEmpty(password)) params.put("password", password);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_update_url, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true);


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
