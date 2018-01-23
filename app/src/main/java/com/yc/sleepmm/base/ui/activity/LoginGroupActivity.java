package com.yc.sleepmm.base.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kk.securityhttp.domain.GoagalInfo;
import com.music.player.lib.util.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.ui.contract.LoginContract;
import com.yc.sleepmm.base.ui.dialog.LoadingProgressView;
import com.yc.sleepmm.base.ui.fragment.LoginFragment;
import com.yc.sleepmm.base.ui.fragment.LoginRegisterFragment;
import com.yc.sleepmm.base.ui.presenter.LoginPresenter;
import com.yc.sleepmm.bean.UserDataInfo;
import com.yc.sleepmm.constants.Constant;
import java.util.Map;
import butterknife.BindView;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * TinyHung@Outlook.com
 * 2017/11/28.
 * 用户登录、注册、修改密码
 */

public class LoginGroupActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_register)
    TextView btnRegister;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_other_login_tips)
    TextView tvOtherLoginTips;
    @BindView(R.id.re_weichat)
    RelativeLayout reWeichat;
    @BindView(R.id.re_qq)
    RelativeLayout reQq;
    @BindView(R.id.re_weibo)
    RelativeLayout reWeibo;
    @BindView(R.id.ll_other_login_view)
    LinearLayout llOtherLoginView;

    private LoginPresenter mLoginPresenter;
    private EventHandler mEventHandler;
    private LoadingProgressView mLoadingProgressedView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //顶部透明
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        mLoginPresenter = new LoginPresenter(this);
        mLoginPresenter.attachView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_group;
    }

    @Override
    public void init() {
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_back:
                        onBackPressed();
                        break;
                    case R.id.btn_register:
                        openBtnAction();
                        break;
                    //微信登录
                    case R.id.re_weichat:
                        login(SHARE_MEDIA.WEIXIN);
                        break;
                    //QQ登录
                    case R.id.re_qq:
                        login(SHARE_MEDIA.QQ);
                        break;
                    //微博登录
                    case R.id.re_weibo:
                        login(SHARE_MEDIA.SINA);
                        break;
                }
            }
        };
        btnBack.setOnClickListener(onClickListener);
        btnRegister.setOnClickListener(onClickListener);
        reWeichat.setOnClickListener(onClickListener);
        reQq.setOnClickListener(onClickListener);
        reWeibo.setOnClickListener(onClickListener);
        addReplaceFragment(new LoginFragment(),"登录","注册");//初始化默认登录界面
        tvOtherLoginTips.setText("快捷登录");
        showOthreLoginView(true);
        initSMS();
    }

    /**
     * 初始化短信监听
     */
    private void initSMS() {
        mEventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                //回调完成
                if (result == SMSSDK.RESULT_COMPLETE) {
//                    EventBus.getDefault().post(new SMSEventMessage(100,""));
                    //验证码正确
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                        EventBus.getDefault().post(new SMSEventMessage(101,""));
                        //获取验证码成功
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
//                        EventBus.getDefault().post(new SMSEventMessage(102,""));
                        //返回支持发送验证码的国家列表
                    }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    }
                }else{
                    Throwable throwable = (Throwable) data;
//                    EventBus.getDefault().post(new SMSEventMessage(99, throwable.toString()));
                }
            }
        };
        SMSSDK.registerEventHandler(mEventHandler); //注册短信回调
    }

    /**
     * 打开意图
     */
    public void openBtnAction() {
        if(TextUtils.equals("注册",btnRegister.getText().toString())){
            addReplaceFragment(new LoginRegisterFragment(),"注册","登录");
            tvOtherLoginTips.setText("快速注册");
        }else if(TextUtils.equals("登录",btnRegister.getText().toString())){
            onBackPressed();
        }
    }

    /**
     * 叠加界面
     * @param fragment 片段目标
     * @param centerTitle 中间标题
     * @param rightTitle 右边小标题
     */
    public void addReplaceFragment(Fragment fragment, String centerTitle, String rightTitle) {
        tvTitle.setText(centerTitle);
        btnRegister.setText(rightTitle);
        android.support.v4.app.FragmentManager supportFragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, fragment, centerTitle);
        fragmentTransaction.addToBackStack(centerTitle);
        fragmentTransaction.commit();
    }

    /**
     * 显示和占位第三方登录
     * @param flag
     */
    public void showOthreLoginView(boolean flag) {
        llOtherLoginView.setVisibility(flag?View.VISIBLE:View.INVISIBLE);
    }

    /**
     * 获取验证码
     * @param country
     * @param account
     */
    public void getCode(String country, String account) {
        SMSSDK.getVerificationCode(country, account, new OnSendMessageHandler() {
            @Override
            public boolean onSendMessage(String country, String account) {
                return false;//发送短信之前调用，返回TRUE表示无需真正发送验证码
            }
        });
    }

    public void makePasswordFinlish(String account) {
//        SMSEventMessage smsEventMessage=new SMSEventMessage();
//        smsEventMessage.setSmsCode(99);
//        smsEventMessage.setAccount(account);
//        onBackPressed();
//        EventBus.getDefault().post(smsEventMessage);
    }



    //=====================================QQ、微信、微博登录========================================
    /**
     * 登录到服务器
     * @param userDataInfo
     */
    private void login(UserDataInfo userDataInfo) {
        if(null!=mLoginPresenter&&!mLoginPresenter.isLogin()){
            mLoginPresenter.loginOther(userDataInfo);
        }
    }

    /**
     * 第三方账号登录成功
     */
    public void closeForResult() {
        if(null!= APP.getInstance().getUserData()&&!TextUtils.isEmpty(APP.getInstance().getUserData().getOpenid())){
            //携带登录成功消息
            Intent intent=new Intent();
            intent.putExtra(Constant.INTENT_LOGIN_STATE,true);
            setResult(Constant.INTENT_LOGIN_RESULTCODE,intent);
            finish();
        }else{
            ToastUtils.showCenterToast("登录异常，请重新登录!");
        }
    }

    /**
     * 手机账号登录成功
     */
    public void closeForResult(UserDataInfo data) {
        //手机账号登录需要补全用户信息
        if(null!=APP.getInstance().getUserData()&&null!=data&&!TextUtils.isEmpty(APP.getInstance().getUserData().getOpenid())){
            Intent intent=new Intent();
            intent.putExtra(Constant.INTENT_LOGIN_STATE,true);
            setResult(Constant.INTENT_LOGIN_RESULTCODE,intent);
            finish();
        }else{
            ToastUtils.showCenterToast("登录异常，请重新登录!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        //这个时候DialogFragment无法回调onActivityResult方法.由父窗体来回调结果到子View中,发出订阅的事件，如果用户信息编辑的界面已经初始化可以收到订阅消息
//        MessageEvent messageEvent=new MessageEvent();
//        messageEvent.setData(data);
//        messageEvent.setMessage("CAMERA_REQUEST");
//        messageEvent.setRequestCode(requestCode);
//        messageEvent.setResultState(resultCode);
//        EventBus.getDefault().post(messageEvent);
    }

    /**
     * 账号密码登录
     * @param account
     * @param password
     */
    public void login(String account,String password){
//        SMSEventMessage smsEventMessage=new SMSEventMessage();
//        smsEventMessage.setSmsCode(98);
//        smsEventMessage.setAccount(account);
//        smsEventMessage.setPassword(password);
//        onBackPressed();
//        EventBus.getDefault().post(smsEventMessage);
    }

    /**
     * QQ、微信、微博 登录
     * @param media
     */
    public void login(SHARE_MEDIA media) {
        showProgressDialog("登录中，请稍后..",true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                ToastUtils.showCenterToast("登陆失败，没有人写后台！");
            }
        },1800);
        return;
//        boolean isauth = UMShareAPI.get(LoginGroupActivity.this).isAuthorize(LoginGroupActivity.this, media);//判断当前APP有没有授权登录
//        if (isauth) {
//            UMShareAPI.get(LoginGroupActivity.this).getPlatformInfo(LoginGroupActivity.this, media, LoginAuthListener);//获取用户信息
//        } else {
//            UMShareAPI.get(LoginGroupActivity.this).doOauthVerify(LoginGroupActivity.this, media, LoginAuthListener);//用户授权登录
//        }
    }


    /**
     * QQ 微信 微博 登陆后回调
     */
    UMAuthListener LoginAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showProgressDialog("登录中，请稍后...",true);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            int loginType=0;
            switch (platform) {
                case QQ:
                    loginType=1;
                    break;
                case WEIXIN:
                    loginType=2;
                    break;
                case SINA:
                    loginType=3;
                    break;
            }
            try{
                if(null!=data&&data.size()>0){
                    UserDataInfo userDataInfo=new UserDataInfo();
                    userDataInfo.setIemil(GoagalInfo.get().uuid);
                    userDataInfo.setLoginType(loginType+"");
                    //新浪微博
                    if(platform== SHARE_MEDIA.SINA){
                        userDataInfo.setNickname(data.get("name"));
                        userDataInfo.setCity(data.get("location"));
                        userDataInfo.setFigureurl_qq_2(data.get("iconurl"));
                        userDataInfo.setGender(data.get("gender"));
                        userDataInfo.setProvince(data.get("location"));
                        userDataInfo.setOpenid(data.get("id"));
                        userDataInfo.setImageBG(data.get("cover_image_phone"));
                    //微信、QQ
                    }else{
                        userDataInfo.setNickname(data.get("screen_name"));
                        userDataInfo.setCity(data.get("city"));
                        userDataInfo.setFigureurl_qq_2(data.get("iconurl"));
                        userDataInfo.setGender(data.get("gender"));
                        userDataInfo.setProvince(data.get("province"));
                        userDataInfo.setOpenid(data.get("openid"));
                    }
                    //授权成功
                    if(TextUtils.isEmpty(userDataInfo.getNickname())&&TextUtils.isEmpty(userDataInfo.getFigureurl_qq_2())){
                        login(platform);
                    }else{
                        //登录App成功,防止微博
                        if(!TextUtils.isEmpty(userDataInfo.getOpenid())){
                            login(userDataInfo);
                        }else{
                            login(platform);
                        }
                    }
                }else{
                    closeProgressDialog();
                    ToastUtils.showCenterToast("登录失败，请重试!");
                }
            }catch (Exception e){
                closeProgressDialog();
                ToastUtils.showCenterToast("登录失败，请重试!");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            closeProgressDialog();
            ToastUtils.showCenterToast("登录失败，请重试!");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            closeProgressDialog();
            ToastUtils.showCenterToast("登录取消");
        }
    };



    //======================================登录到服务器回调=========================================

    @Override
    public void showErrorView() {
        closeProgressDialog();
    }

    @Override
    public void complete() {
        closeProgressDialog();
    }

    @Override
    public void showLoginOtherResult(String data) {
        closeProgressDialog();
        APP.getInstance().setUserData(null,true);
        closeForResult();
    }

    @Override
    public void showLoginAccountResult(String data) {

    }

    @Override
    public void showRegisterAccountResult(String data) {

    }

    @Override
    public void showMakePasswordResult(String data) {

    }

    @Override
    public void onBackPressed() {
        //只剩登录一个界面了
        if(getSupportFragmentManager().getBackStackEntryCount()==1&&!LoginGroupActivity.this.isFinishing()){
            finish();
            return;
        }
        //栈顶存在两个
        if(getSupportFragmentManager().getBackStackEntryCount()==2&&!LoginGroupActivity.this.isFinishing()){
            tvTitle.setText("登录");
            btnRegister.setText("注册");
            tvOtherLoginTips.setText("快捷登录");
            showOthreLoginView(true);
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.menu_exit);//出场动画
    }

    @Override
    public void onDestroy() {
        SMSSDK.unregisterEventHandler(mEventHandler);
        super.onDestroy();
        closeProgressDialog();
        if(null!=mLoginPresenter) mLoginPresenter.detachView();
        Runtime.getRuntime().gc();
    }

    /**
     * 显示进度框
     * @param message
     * @param isProgress
     */
    public void showProgressDialog(String message,boolean isProgress){
        if(!this.isFinishing()){
            if(null==mLoadingProgressedView){
                mLoadingProgressedView = new LoadingProgressView(this,isProgress);
            }
            mLoadingProgressedView.setMessage(message);
            mLoadingProgressedView.show();
        }
    }

    /**
     * 关闭进度框
     */
    public void closeProgressDialog(){
        try {
            if(!this.isFinishing()){
                if(null!=mLoadingProgressedView&&mLoadingProgressedView.isShowing()){
                    mLoadingProgressedView.dismiss();
                    mLoadingProgressedView=null;
                }
            }
        }catch (Exception e){

        }
    }
}
