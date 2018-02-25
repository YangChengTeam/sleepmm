package com.yc.sleepmm.index.ui.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.player.lib.util.ToastUtils;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.index.ui.activity.LoginGroupActivity;
import com.yc.sleepmm.index.ui.contract.LoginContract;
import com.yc.sleepmm.index.ui.presenter.LoginPresenter;
import com.yc.sleepmm.setting.utils.Utils;

import butterknife.BindView;

/**
 * TinyHung@Outlook.com
 * 2017/11/28.
 * 账号密码登录
 */

public class LoginFragment extends MusicBaseFragment<LoginPresenter> implements LoginContract.View {

    private static final String TAG = "LoginFragment";
    @BindView(R.id.tv_retrieve_password)
    TextView tvRetrievePassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.iv_account_cancel)
    ImageView ivAccountCancel;
    @BindView(R.id.iv_password_cancel)
    ImageView ivPasswordCancel;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    private Animation mInputAnimation;
    private LoginGroupActivity mLoginGroupActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginGroupActivity = (LoginGroupActivity) context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }


    @Override
    protected void initViews() {

        mPresenter = new LoginPresenter(getActivity(), this);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    //登录
                    case R.id.btn_login:
                        createAccountLogin();
                        break;
                    //忘记密码
                    case R.id.tv_retrieve_password:
                        if (null != mLoginGroupActivity && !mLoginGroupActivity.isFinishing()) {
                            mLoginGroupActivity.addReplaceFragment(new LoginEditPasswordFragment(), "修改密码", "登录");//打开修改密码界面
                            mLoginGroupActivity.showOthreLoginView(false);
                        }
                        break;
                    //清除输入框账号
                    case R.id.iv_account_cancel:
                        etAccount.setText("");
                        break;
                    //清除输入框密码
                    case R.id.iv_password_cancel:
                        etPassword.setText("");
                        break;
                }
            }
        };
        etAccount.setText(APP.getInstance().getUserData() != null ? APP.getInstance().getUserData().getMobile() : "");
        etAccount.setSelection(etAccount.getText().toString().length());
        tvRetrievePassword.setOnClickListener(onClickListener);
        ivAccountCancel.setOnClickListener(onClickListener);
        ivPasswordCancel.setOnClickListener(onClickListener);
        btnLogin.setOnClickListener(onClickListener);
        etAccount.addTextChangedListener(accountChangeListener);
        etPassword.addTextChangedListener(passwordChangeListener);
        //监听焦点获悉情况
        etAccount.setOnFocusChangeListener(onFocusChangeListener);
        etPassword.setOnFocusChangeListener(onFocusChangeListener);
        mInputAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        //设置密码属性
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }


//    @Subscribe(
//       thread = EventThread.MAIN_THREAD,
//       tags = {@Tag(BusAction.LOGIN_COMPER)}
//    )
//    /**
//     * 接收找回密码界面的通知消息
//     */
//    public void loginCopmper(String account) {
//        if(!TextUtils.isEmpty(account)){
//            etAccount.setText(account);
//            etAccount.setSelection(account.length());
//        }
//    }


    /**
     * 用户使用账号登录
     */
    private void createAccountLogin() {
        if (null != etAccount && null != etPassword) {
            String account = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(account)) {
                ToastUtils.showCenterToast("手机号码不能为空");
                etAccount.startAnimation(mInputAnimation);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                ToastUtils.showCenterToast("密码不能为空");
                etPassword.startAnimation(mInputAnimation);
                return;
            }
            if (!Utils.isPhoneNumber(account)) {
                ToastUtils.showCenterToast("手机号码格式不正确");
                return;
            }
            if (null != mPresenter && !mPresenter.isLogin()) {
                mPresenter.loginAccount(account, password);
            }
        }
    }

    /**
     * 账号输入框监听
     */
    private TextWatcher accountChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (null != ivAccountCancel)
                ivAccountCancel.setVisibility(!TextUtils.isEmpty(s) && s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 密码输入框监听
     */
    private TextWatcher passwordChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (null != ivPasswordCancel)
                ivPasswordCancel.setVisibility(!TextUtils.isEmpty(s) && s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    /**
     * 对个输入框焦点进行监听
     */
    private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.et_account:
                    if (hasFocus) {
                        if (etAccount.getText().toString().length() > 0) {
                            ivAccountCancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (null != ivAccountCancel) ivAccountCancel.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.et_password:
                    if (hasFocus) {
                        if (etPassword.getText().toString().length() > 0) {
                            ivPasswordCancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (null != ivPasswordCancel)
                            ivPasswordCancel.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        etAccount.setText("");
        etPassword.setText("");
        if (null != mInputAnimation) {
            mInputAnimation.cancel();
            mInputAnimation = null;
        }
    }


    @Override
    public void showAccountResult(UserInfo data, String tint) {
        if (null != data && !TextUtils.isEmpty(data.getId())) {
            if (TextUtils.equals(getString(R.string.login), tint)) {
                APP.getInstance().setUserData(data, true);
            }
            if (null != mLoginGroupActivity && !mLoginGroupActivity.isFinishing()) {
                mLoginGroupActivity.loginResultFinlish();
            }
        } else {
            ToastUtils.showCenterToast(tint + "异常，请重试！");
        }
    }


    @Override
    public void showRequstError(String data) {

        ToastUtils.showCenterToast(data);
    }

    @Override
    public void showLoadingDialog(String mess) {

    }

    @Override
    public void showLoadingProgressDialog(String mess, boolean isProgress) {
        ((BaseActivity) getActivity()).showLoadingProgressDialog(mess, isProgress);
    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public void dismissProgressDialog() {
        ((BaseActivity) getActivity()).dismissProgressDialog();
    }
}
