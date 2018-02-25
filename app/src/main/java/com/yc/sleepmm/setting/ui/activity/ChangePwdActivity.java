package com.yc.sleepmm.setting.ui.activity;

import android.content.Intent;
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

import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.music.player.lib.util.ToastUtils;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.index.constants.Constant;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.index.ui.activity.LoginGroupActivity;
import com.yc.sleepmm.index.ui.contract.LoginContract;
import com.yc.sleepmm.index.ui.presenter.LoginPresenter;
import com.yc.sleepmm.setting.utils.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/2/24 10:40.
 */

public class ChangePwdActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
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
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_password_cancel)
    ImageView ivPasswordCancel;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private Animation animation;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit_password;
    }

    @Override
    public void init() {
        mPresenter = new LoginPresenter(this, this);
        tvTitle.setText(getString(R.string.change_password));
        tvVipProtocol.setVisibility(View.GONE);
        animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        etAccount.setText(APP.getInstance().getUserData() != null ? APP.getInstance().getUserData().getMobile() : "");
        etAccount.setSelection(etAccount.getText().toString().length());
        initListener();

    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivAccountCancel.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ivPasswordCancel.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        RxView.clicks(tvGetCode).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                getCode();
            }
        });
        RxView.clicks(btnSubmit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                changePwd();
            }
        });
        RxView.clicks(ivAccountCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                etAccount.setText("");
            }
        });
        RxView.clicks(ivPasswordCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                etPassword.setText("");
            }
        });

    }

    @Override
    public void showAccountResult(UserInfo data, String tint) {
        if (null != data && !TextUtils.isEmpty(data.getId())) {
            ToastUtils.showCenterToast(tint + "成功");
            if (!isFinishing()) {
                APP.getInstance().setUserData(null, true);
                RxBus.get().post(Constant.RX_LOGIN_SUCCESS, "change pwd");
                startActivity(new Intent(ChangePwdActivity.this, LoginGroupActivity.class));

                finish();
            }
        } else {
            ToastUtils.showCenterToast(tint + "异常，请重试！");
        }
    }

    @Override
    public void showRequstError(String data) {

    }

    private void getCode() {
        String phone = etAccount.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showCenterToast("手机号码不能为空");
            etAccount.startAnimation(animation);
            return;
        }
        if (!isFinishing()) {
            showGetCodeDisplay(tvGetCode);
            mPresenter.getCode(phone);
        }
    }

    private void changePwd() {
        String phone = etAccount.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String newPwd = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            etAccount.startAnimation(animation);
            return;
        }
        if (!Utils.isPhoneNumber(phone)) {
            etAccount.startAnimation(animation);
            ToastUtils.showCenterToast("手机号码格式不正确");
            return;
        }

        if (TextUtils.isEmpty(newPwd)) {
            ToastUtils.showCenterToast("请设置新密码");
            etPassword.startAnimation(animation);
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast("请输入接收到的验证码");
            etCode.startAnimation(animation);
            return;
        }

        if (!isFinishing()) {
            mPresenter.findPassword(phone, code, newPwd);
        }
    }


}
