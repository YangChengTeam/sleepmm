package com.yc.sleepmm.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.base.util.UIUtils;
import com.yc.sleepmm.setting.bean.UploadInfo;
import com.yc.sleepmm.setting.contract.SettingContract;
import com.yc.sleepmm.setting.engine.SettingEngine;
import com.yc.sleepmm.vip.bean.PayInfo;
import com.yc.sleepmm.vip.utils.PaywayHelper;

import java.io.File;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/2/5 14:09.
 */

public class SettingPresenter extends BasePresenter<SettingEngine, SettingContract.View> implements SettingContract.Presenter {
    public SettingPresenter(Context mContext, SettingContract.View view) {
        super(mContext, view);
        mEngine = new SettingEngine(mContext);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getPayInfos();
    }

    @Override
    public void getPayInfos() {
        Subscription subscription = mEngine.getPayInfos().subscribe(new Subscriber<ResultInfo<List<PayInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<PayInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    PaywayHelper.setmPaywayInfo(listResultInfo.data);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void uploadFile(File file, String fileName) {
        mView.showLoadingDialog("正在上传，请稍候...");
        Subscription subscription = mEngine.uploadInfo(file, fileName).subscribe(new Subscriber<ResultInfo<UploadInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(final ResultInfo<UploadInfo> uploadInfoResultInfo) {
                if (uploadInfoResultInfo != null && uploadInfoResultInfo.code == HttpConfig.STATUS_OK && uploadInfoResultInfo.data != null) {
                    UIUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            mView.showUploadFile(uploadInfoResultInfo.data);
                        }
                    });

                }
            }
        });
        mSubscriptions.add(subscription);

    }
}
