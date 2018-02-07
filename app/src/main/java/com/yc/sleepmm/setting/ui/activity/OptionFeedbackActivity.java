package com.yc.sleepmm.setting.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.setting.contract.FeedbackContract;
import com.yc.sleepmm.setting.presenter.FeedbackPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/26 14:19.
 */

public class OptionFeedbackActivity extends BaseActivity<FeedbackPresenter> implements FeedbackContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_vip_protocol)
    TextView tvVipProtocol;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.et_content)
    EditText etContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void init() {
        mPresenter = new FeedbackPresenter(this, this);

        tvTitle.setText(getString(R.string.opinion_feedback));
        tvVipProtocol.setVisibility(View.GONE);
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        RxView.clicks(btnConfirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!APP.getInstance().isGotoLogin(OptionFeedbackActivity.this)) {
                    mPresenter.createSuggest(etContent.getText().toString().trim(), APP.getInstance().getUserData().getId());
                }
            }
        });

    }
}
