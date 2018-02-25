package com.yc.sleepmm.setting.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.setting.contract.BindContract;
import com.yc.sleepmm.setting.presenter.BindPhonePresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/2/13 11:37.
 */

public class BindPhoneActivity extends BaseActivity<BindPhonePresenter> implements BindContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_vip_protocol)
    TextView tvVipProtocol;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.iv_account_cancel)
    ImageView ivAccountCancel;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_register)
    Button btnRegister;

    private Animation animation;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void init() {
        mPresenter = new BindPhonePresenter(this, this);

        tvTitle.setText(getString(R.string.bind_phone));
        tvVipProtocol.setVisibility(View.GONE);
        animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        initListener();
    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        RxView.clicks(ivAccountCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                etAccount.setText("");
            }
        });
        RxView.clicks(tvGetCode).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                getCode();
            }
        });
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    ivAccountCancel.setVisibility(View.VISIBLE);
                } else {
                    ivAccountCancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RxView.clicks(btnRegister).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                bindPhone();
            }
        });

    }


    private void getCode() {
        String phone = etAccount.getText().toString().trim();
        if (!isFinishing() && null != mPresenter) {
            mPresenter.getCode(phone);
        }
    }


    @Override
    public void showPhoneErrorView() {
        etAccount.startAnimation(animation);
    }

    @Override
    public void showRightView() {
        showGetCodeDisplay(tvGetCode);
    }

    @Override
    public void showCodeErrorView() {
        etCode.startAnimation(animation);
    }

    private void bindPhone() {
        String code = etCode.getText().toString().trim();
        String phone = etAccount.getText().toString().trim();
        if (!isFinishing()) {
            mPresenter.bindPhone(phone, APP.getInstance().getUserData().getId(), code);
        }

    }
}
