package com.yc.sleepmm.base.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.yc.sleepmm.base.ui.activity.LoginGroupActivity;
import com.yc.sleepmm.base.ui.contract.LoginContract;
import com.yc.sleepmm.base.ui.presenter.LoginPresenter;
import com.yc.sleepmm.sleep.ui.CommonUtils;
import com.yc.sleepmm.setting.ui.activity.Utils;
import butterknife.BindView;

/**
 * TinyHung@Outlook.com
 * 2017/11/28.
 * 用户注册
 */

public class LoginRegisterFragment extends MusicBaseFragment  implements LoginContract.View {


    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_register)
    Button btnRegister;
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


    private Animation mInputAnimation;
    private Handler mHandler;
    private LoginPresenter mLoginPresenter;
    private LoginGroupActivity mLoginGroupActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginGroupActivity = (LoginGroupActivity) context;
    }

    @Override
    protected void initViews() {
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    //注册
                    case R.id.btn_register:
                        cureateRegisterUser();
                        break;
                    //获取验证码
                    case R.id.tv_get_code:
                        cureateGetNumberCode();
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
        btnRegister.setOnClickListener(onClickListener);
        ivAccountCancel.setOnClickListener(onClickListener);
        ivPasswordCancel.setOnClickListener(onClickListener);
        tvGetCode.setOnClickListener(onClickListener);
        etAccount.addTextChangedListener(accountChangeListener);
        etPassword.addTextChangedListener(passwordChangeListener);
        //监听焦点获悉情况
        etAccount.setOnFocusChangeListener(onFocusChangeListener);
        etPassword.setOnFocusChangeListener(onFocusChangeListener);
        mInputAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showContentView();
        mHandler=new Handler();
        mLoginPresenter = new LoginPresenter(getActivity());
        mLoginPresenter.attachView(this);
    }


    /**
     * 准备获取验证码
     */
    private void cureateGetNumberCode() {
        String account =etAccount.getText().toString().trim();
        if(TextUtils.isEmpty(account)){
            ToastUtils.showCenterToast("手机号码不能为空");
            etAccount.startAnimation(mInputAnimation);
            return;
        }
        if(!Utils.isPhoneNumber(account)){
            ToastUtils.showCenterToast("手机号码格式不正确");
            return;
        }
        getCode("86",account);
    }

    /**
     * 获取验证码
     * @param country 区号
     * @param account 手机号码
     */
    private void getCode(String country, String account) {
        if(null!=mLoginGroupActivity&&!mLoginGroupActivity.isFinishing()){
            showProgressDialog("获取验证码中，请稍后...",true);
            mLoginGroupActivity.getCode(country,account);
        }
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
     * 监听短信
     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(SMSEventMessage event) {
//        if(null!=event){
//            closeProgressDialog();
//            switch (event.getSmsCode()) {
//                //发送验证码失败
//                case 99:
//                    initGetCodeBtn();
//                    try {
//                        if(!TextUtils.isEmpty(event.getMessage())){
//                            try {
//                                JSONObject jsonObject=new JSONObject(event.getMessage());
//                                if(null!=jsonObject&&jsonObject.length()>0){
//                                    if(null!=jsonObject.getString("detail")){
//                                        ToastUtils.showCenterToast(jsonObject.getString("detail"));
//                                    }else{
//                                        ToastUtils.showCenterToast("发送验证码失败");
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }catch (Exception e){
//
//                    }
//                    break;
//
//                //获取支持的国家列表成功
//                case 103:
////                    HashMap<String, String> stringStringHashMap = (HashMap<String, String>) event.getMessage();
////                    //保存至本地
////                    SerMap serMap=new SerMap();
////                    serMap.setMap(stringStringHashMap);
////                    VideoApplication.mACache.put(Constant.CACHE_COUNTRY_NUMBER_LIST,serMap,Constant.CACHE_TIME);
//                    break;
//                //短信验证码验证成功
//                case 100:
//                    break;
//                //获取验证码成功
//                case 102:
//                    showGetCodeDisplay();
//                    ToastUtils.showCenterToast("已成功发送验证码");
//                    break;
//                //短信验证码已提交完成
//                case 101:
//                    //请求后台服务器验证
//
//                    break;
//
//            }
//        }
//    }

    /**
     * 改变获取验证码按钮状态
     */
    private void showGetCodeDisplay() {
        totalTime=60;
        tvGetCode.setClickable(false);
        tvGetCode.setTextColor(CommonUtils.getColor(R.color.coment_color));
        tvGetCode.setBackgroundResource(R.drawable.bg_btn_get_code);
        if(null!=mHandler) mHandler.postDelayed(taskRunnable,0);
    }

    /**
     * 还原获取验证码按钮状态
     */
    private void initGetCodeBtn() {
        totalTime=0;
        if(null!=taskRunnable&&null!=mHandler){
            mHandler.removeCallbacks(taskRunnable);
            mHandler.removeMessages(0);
        }
        tvGetCode.setText("重新获取");
        tvGetCode.setClickable(true);
        tvGetCode.setTextColor(CommonUtils.getColor(R.color.white));
        tvGetCode.setBackgroundResource(R.drawable.bg_btn_get_code_true);
    }


    /**
     * 定时任务，模拟倒计时广告
     */
    private int totalTime=60;

    Runnable taskRunnable=new Runnable() {
        @Override
        public void run() {
            tvGetCode.setText(totalTime+"S后重试");
            totalTime--;
            if(totalTime<0){
                //还原
                initGetCodeBtn();
                return;
            }
            if(null!=mHandler) mHandler.postDelayed(this,1000);
        }
    };


    /**
     * 准备注册用户
     */
    private void cureateRegisterUser() {

        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String code = etCode.getText().toString().trim();

        if(TextUtils.isEmpty(account)){
            etAccount.startAnimation(mInputAnimation);
            ToastUtils.showCenterToast("手机号码不能为空");
            return;
        }
        if(!Utils.isPhoneNumber(account)){
            etAccount.startAnimation(mInputAnimation);
            ToastUtils.showCenterToast("手机号码格式不正确");
            return;
        }

        if(TextUtils.isEmpty(password)){
            etPassword.startAnimation(mInputAnimation);
            ToastUtils.showCenterToast("请设置密码");
            return;
        }

        if(TextUtils.isEmpty(code)){
            etCode.startAnimation(mInputAnimation);
            ToastUtils.showCenterToast("验证码不能为空");
            return;
        }

        if(!Utils.isNumberCode(code,4)){
            etCode.startAnimation(mInputAnimation);
            ToastUtils.showCenterToast("验证码格式不正确");
            return;
        }

        if(null!= mLoginPresenter &&!mLoginPresenter.isRegister()){
            showProgressDialog("提交注册中...",true);
            // TODO: 2017/6/20 用户注册
            mLoginPresenter.registerAccount(account,password,code);
        }
    }


    /**
     * 账号输入框监听
     */
    private TextWatcher accountChangeListener=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(null!=ivAccountCancel)ivAccountCancel.setVisibility(!TextUtils.isEmpty(s)&&s.length()>0?View.VISIBLE:View.INVISIBLE);
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
        if(null!=mInputAnimation){
            mInputAnimation.cancel();
            mInputAnimation=null;
        }
        initGetCodeBtn();
        if(null!=mHandler){
            mHandler.removeCallbacks(taskRunnable);
            mHandler=null;
        }
        taskRunnable=null;
    }


    //==========================================注册结果回调=========================================


    @Override
    public void showLoginOtherResult(String data) {

    }

    @Override
    public void showLoginAccountResult(String data) {

    }

    @Override
    public void showRegisterAccountResult(String data) {
        closeProgressDialog();
        ToastUtils.showCenterToast(data);
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