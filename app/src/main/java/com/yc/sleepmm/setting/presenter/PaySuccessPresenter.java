package com.yc.sleepmm.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.music.player.lib.util.ToastUtils;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.setting.contract.PaySuccessContract;
import com.yc.sleepmm.setting.engine.PaySuccessEngine;
import com.yc.sleepmm.setting.utils.Utils;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/3/5 11:51.
 */

public class PaySuccessPresenter extends BasePresenter<PaySuccessEngine, PaySuccessContract.View> implements PaySuccessContract.Presenter {
    public PaySuccessPresenter(Context mContext, PaySuccessContract.View view) {
        super(mContext, view);
        mEngine = new PaySuccessEngine(mContext);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }


    @Override
    public void uploadPhone(String phone, String orderSn) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showCenterToast("手机号不能为空");
            return;
        }
        if (!Utils.isPhoneNumber(phone)) {
            ToastUtils.showCenterToast("手机号码格式有误，请重新输入");
            return;
        }

        mView.showLoadingProgressDialog("正在联系客服，请稍候...", false);

        Subscription subscription = mEngine.uploadPhone(phone, orderSn).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissProgressDialog();
            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                mView.dismissProgressDialog();
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK && stringResultInfo.data != null) {
                    mView.showUploadResult(stringResultInfo.data);
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
