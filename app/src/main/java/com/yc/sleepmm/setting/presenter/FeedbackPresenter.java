package com.yc.sleepmm.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ToastUtil;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.setting.contract.FeedbackContract;
import com.yc.sleepmm.setting.engine.FeedbackEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/2/7 14:31.
 */

public class FeedbackPresenter extends BasePresenter<FeedbackEngine, FeedbackContract.View> implements FeedbackContract.Presenter {
    public FeedbackPresenter(Context context, FeedbackContract.View view) {
        super(context, view);
        mEngine = new FeedbackEngine(context);
    }

    @Override
    public void createSuggest(final String content, String user_id) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.toast(mContext, "反馈意见不能为空");
            return;
        }
        mView.showLoadingDialog("正在提交意见反馈，请稍候...");

        Subscription subscription = mEngine.createSuggest(content, user_id).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK && stringResultInfo.data != null) {
                    ToastUtil.toast(mContext, stringResultInfo.data);
                    mView.finish();
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
