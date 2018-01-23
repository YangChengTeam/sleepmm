package com.yc.sleepmm.base.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.yc.sleepmm.base.ui.activity.LoginGroupActivity;
import com.yc.sleepmm.base.ui.contract.LoginContract;
import com.yc.sleepmm.base.ui.presenter.LoginPresenter;
import com.yc.sleepmm.util.Utils;
import butterknife.BindView;

/**
 * TinyHung@Outlook.com
 * 2017/11/28.
 * 用户账号密码登录
 */

public class LoginFragment extends MusicBaseFragment implements LoginContract.View {

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
    private LoginPresenter mLoginPresenter;
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
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    //登录
                    case R.id.btn_login:
                        createAccountLogin();
                        break;
                    //忘记密码
                    case R.id.tv_retrieve_password:
                        if(null!=mLoginGroupActivity&&!mLoginGroupActivity.isFinishing()){
                            mLoginGroupActivity.addReplaceFragment(new LoginEditPasswordFragment(),"修改密码","登录");//打开修改密码界面
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


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showContentView();
        mLoginPresenter = new LoginPresenter(getActivity());
        mLoginPresenter.attachView(this);
    }


    /**
     * 用户使用账号登录
     */
    private void createAccountLogin() {
        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(account)){
            ToastUtils.showCenterToast("手机号码不能为空");
            etAccount.startAnimation(mInputAnimation);
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtils.showCenterToast("密码不能为空");
            etPassword.startAnimation(mInputAnimation);
            return;
        }
        if(!Utils.isPhoneNumber(account)){
            ToastUtils.showCenterToast("手机号码格式不正确");
            return;
        }
        showProgressDialog("登录中，请稍后..",true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                ToastUtils.showCenterToast("登陆失败，没有人写后台！");
            }
        },1800);
        return;
//        if(null!= mLoginPresenter &&!mLoginPresenter.isLogin()){
//            showProgressDialog("登录中,请稍后...",true);
//            mLoginPresenter.loginAccount(account,password);
//        }
    }


    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    /**
     * 刷新通知
     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(SMSEventMessage event) {
//        if(null!=event){
//            if(98==event.getSmsCode()&&null!=bindingView) {
//                bindingView.etAccount.setText( event.getAccount());
//                bindingView.etPassword.setText(event.getPassword());
//                bindingView.etAccount.setSelection(event.getAccount().length());
//                bindingView.etPassword.setSelection(event.getPassword().length());
//                if (null != mLoginPresenter && !mLoginPresenter.isLogin()) {
//                    showProgressDialog("登录中,请稍后...", true);
//                    mLoginPresenter.userLogin("86", event.getAccount(), event.getPassword());
//                }
//            }else if(99==event.getSmsCode()&&null!=bindingView){
//                bindingView.etAccount.setText(event.getAccount());
//                bindingView.etAccount.setSelection(event.getAccount().length());
//                bindingView.etPassword.setText("");
//            }
//        }
//    }


    /**
     * 账号输入框监听
     */
    private TextWatcher accountChangeListener=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(null!=ivAccountCancel) ivAccountCancel.setVisibility(!TextUtils.isEmpty(s)&&s.length()>0?View.VISIBLE:View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 密码输入框监听
     */
    private TextWatcher passwordChangeListener=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(null!=ivPasswordCancel) ivPasswordCancel.setVisibility(!TextUtils.isEmpty(s)&&s.length()>0?View.VISIBLE:View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    /**
     * 对个输入框焦点进行监听
     */
    private View.OnFocusChangeListener onFocusChangeListener=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.et_account:
                    if(hasFocus){
                        if(etAccount.getText().toString().length()>0){
                            ivAccountCancel.setVisibility(View.VISIBLE);
                        }
                    }else{
                        if(null!=ivAccountCancel) ivAccountCancel.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.et_password:
                    if(hasFocus){
                        if(etPassword.getText().toString().length()>0){
                            ivPasswordCancel.setVisibility(View.VISIBLE);
                        }
                    }else{
                        if(null!=ivPasswordCancel) ivPasswordCancel.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        if(null!= mLoginPresenter){
            mLoginPresenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeProgressDialog();
        etAccount.setText("");
        etPassword.setText("");
        if(null!=mInputAnimation){
            mInputAnimation.cancel();
            mInputAnimation=null;
        }
        mLoginPresenter =null;
        mLoginGroupActivity=null;
    }


    @Override
    public void showLoginOtherResult(String data) {

    }

    @Override
    public void showLoginAccountResult(String data) {
        closeProgressDialog();
        ToastUtils.showCenterToast(data);
    }

    @Override
    public void showRegisterAccountResult(String data) {

    }

    @Override
    public void showMakePasswordResult(String data) {

    }

    @Override
    public void showErrorView() {
        closeProgressDialog();
    }

    @Override
    public void complete() {
        closeProgressDialog();
    }
}
