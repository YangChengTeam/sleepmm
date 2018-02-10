package com.yc.sleepmm.index.ui.fragment;

import android.content.Context;
import android.os.Handler;
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

import com.music.player.lib.util.ToastUtils;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.index.ui.activity.LoginGroupActivity;
import com.yc.sleepmm.index.ui.contract.LoginContract;
import com.yc.sleepmm.index.ui.presenter.LoginPresenter;
import com.yc.sleepmm.index.util.CommonUtils;
import com.yc.sleepmm.setting.utils.Utils;

import butterknife.BindView;

/**
 * TinyHung@Outlook.com
 * 2017/11/28.
 * 修改密码
 */

public class LoginEditPasswordFragment extends MusicBaseFragment<LoginPresenter> implements LoginContract.View {


    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.iv_account_cancel)
    ImageView ivAccountCancel;
    @BindView(R.id.iv_password_cancel)
    ImageView ivPasswordCancel;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private Animation mInputAnimation;

    private LoginGroupActivity mLoginGroupActivity;
    private Handler mHandler;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginGroupActivity = (LoginGroupActivity) context;
    }

    @Override
    protected void initViews() {
        mPresenter = new LoginPresenter(getActivity(), this);
        mInputAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        mHandler = new Handler();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    //获取验证码
                    case R.id.tv_get_code:
                        cureateGetNumberCode();
                        break;
                    //确定修改
                    case R.id.btn_submit:
                        cureateSubmit();
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
        tvGetCode.setOnClickListener(onClickListener);
        ivAccountCancel.setOnClickListener(onClickListener);
        ivPasswordCancel.setOnClickListener(onClickListener);
        btnSubmit.setOnClickListener(onClickListener);
        etAccount.addTextChangedListener(accountChangeListener);
        etPassword.addTextChangedListener(passwordChangeListener);
        //监听焦点获悉情况
        etAccount.setOnFocusChangeListener(onFocusChangeListener);
        etPassword.setOnFocusChangeListener(onFocusChangeListener);
        etCode.setOnFocusChangeListener(onFocusChangeListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit_password;
    }


    /**
     * 准备获取验证码
     */
    private void cureateGetNumberCode() {
        String account = etAccount.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showCenterToast("手机号码不能为空");
            etAccount.startAnimation(mInputAnimation);
            return;
        }
        getCode("86", account);
    }

    /**
     * 获取验证码
     *
     * @param country 区号
     * @param account 手机号码
     */
    private void getCode(String country, String account) {
        if (null != mLoginGroupActivity && !mLoginGroupActivity.isFinishing() && null != mPresenter) {
            showGetCodeDisplay();

            mPresenter.getCode(account);
        }
    }


    /**
     * 改变获取验证码按钮状态
     */
    private void showGetCodeDisplay() {
        if (null != taskRunnable && null != mHandler) {
            mHandler.removeCallbacks(taskRunnable);
            totalTime = 60;
            tvGetCode.setClickable(false);
            tvGetCode.setTextColor(CommonUtils.getColor(R.color.coment_color));
            tvGetCode.setBackgroundResource(R.drawable.bg_btn_get_code);
            mHandler.postDelayed(taskRunnable, 0);
        }
    }


    /**
     * 还原获取验证码按钮状态
     */
    private void initGetCodeBtn() {
        totalTime = 0;
        if (null != taskRunnable && null != mHandler) {
            mHandler.removeCallbacks(taskRunnable);
        }
        tvGetCode.setText("重新获取");
        tvGetCode.setClickable(true);
        tvGetCode.setTextColor(CommonUtils.getColor(R.color.white));
        tvGetCode.setBackgroundResource(R.drawable.bg_btn_get_code_true);
    }

    /**
     * 定时任务，模拟倒计时广告
     */
    private int totalTime = 60;

    Runnable taskRunnable = new Runnable() {
        @Override
        public void run() {
            tvGetCode.setText(totalTime + "S后重试");
            totalTime--;
            if (totalTime < 0) {
                //还原
                initGetCodeBtn();
                return;
            }
            if (null != mHandler) mHandler.postDelayed(this, 1000);
        }
    };


    /**
     * 准备提交新密码
     */
    private void cureateSubmit() {

        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String code = etCode.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            etAccount.startAnimation(mInputAnimation);
            return;
        }
        if (!Utils.isPhoneNumber(account)) {
            etAccount.startAnimation(mInputAnimation);
            ToastUtils.showCenterToast("手机号码格式不正确");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.showCenterToast("请设置新密码");
            etPassword.startAnimation(mInputAnimation);
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast("请输入接收到的验证码");
            etCode.startAnimation(mInputAnimation);
            return;
        }
        if (null != mPresenter && !mPresenter.isMakePassword()) {

            mPresenter.findPassword(account, code, password);
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
                        ivAccountCancel.setVisibility(View.INVISIBLE);
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


    //=======================================修改密码结果回调========================================


    @Override
    public void showAccountResult(UserInfo data, String tint) {
        if (null != data && !TextUtils.isEmpty(data.getId())) {
            ToastUtils.showCenterToast(tint + "成功");
            if (TextUtils.equals(getString(R.string.login), tint)) {
                APP.getInstance().setUserData(data, true);
            }
            if (null != mLoginGroupActivity && !mLoginGroupActivity.isFinishing()) {
                mLoginGroupActivity.registerResultFinlish();//修改密码完成
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
    public void onDestroyView() {
        super.onDestroyView();
        initGetCodeBtn();
        if (null != mInputAnimation) {
            mInputAnimation.cancel();
            mInputAnimation = null;
        }
        if (null != mHandler) {
            mHandler.removeCallbacks(taskRunnable);
            mHandler = null;
        }
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
