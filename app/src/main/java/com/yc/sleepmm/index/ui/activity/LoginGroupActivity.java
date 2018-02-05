package com.yc.sleepmm.index.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kk.securityhttp.domain.GoagalInfo;
import com.music.player.lib.util.Logger;
import com.music.player.lib.util.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.index.bean.UserDataInfo;
import com.yc.sleepmm.index.bean.UserInfo;
import com.yc.sleepmm.index.constants.Constant;
import com.yc.sleepmm.index.ui.contract.LoginContract;
import com.yc.sleepmm.index.ui.dialog.LoadingProgressView;
import com.yc.sleepmm.index.ui.fragment.LoginFragment;
import com.yc.sleepmm.index.ui.fragment.LoginRegisterFragment;
import com.yc.sleepmm.index.ui.presenter.LoginPresenter;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TinyHung@Outlook.com
 * 2017/11/28.
 * 用户登录、注册、修改密码
 */

public class LoginGroupActivity extends AppCompatActivity implements LoginContract.View {

    private static final String TAG = "LoginGroupActivity";

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
    private LoadingProgressView mLoadingProgressedView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginPresenter = new LoginPresenter(this);
        mLoginPresenter.attachView(this);
        //顶部透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login_group);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
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


    //=====================================QQ、微信、微博登录========================================
    /**
     * 拿到用户信息后登录到应用服务器
     * @param userDataInfo
     */
    private void login(UserDataInfo userDataInfo) {
        if(null!=mLoginPresenter&&!mLoginPresenter.isLogin()){
            mLoginPresenter.loginOther(userDataInfo);
        }
    }


    /**
     * 用户注册\修改密码成功
     */
    public void registerResultFinlish() {
        onBackPressed();
        loginResultFinlish();
    }

    /**
     * 手机号码、第三方登录成功调用此方法
     */
    public void loginResultFinlish() {
        UserInfo userData = APP.getInstance().getUserData();
        if(null!=userData&&TextUtils.isEmpty(userData.getMobile())){
            //如果第三方用户登录成功，可以在这里判断是否需要补全手机号码等信息
            Logger.d("loginResultFinlish","用户使用的第三方账号登录");
        }
        setResult(Constant.INTENT_LOGIN_RESULTCODE);
        onBackPressed();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }


    /**
     * QQ、微信、微博 登录
     * @param media
     */
    public void login(SHARE_MEDIA media) {
        boolean isauth = UMShareAPI.get(LoginGroupActivity.this).isAuthorize(LoginGroupActivity.this, media);//判断当前APP有没有授权登录
        if (isauth) {
            UMShareAPI.get(LoginGroupActivity.this).getPlatformInfo(LoginGroupActivity.this, media, LoginAuthListener);//获取用户信息
        } else {
            UMShareAPI.get(LoginGroupActivity.this).doOauthVerify(LoginGroupActivity.this, media, LoginAuthListener);//用户授权登录
        }
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
                    Logger.d(TAG,"data="+data.toString());
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
                        //登录App成功,防止微博获取不到用户信息
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
        loginResultFinlish();
    }

    @Override
    public void showLoginAccountResult(UserInfo data) {

    }


    @Override
    public void showRegisterAccountResult(UserInfo data) {

    }

    @Override
    public void showFindPasswordResult(UserInfo data) {

    }

    @Override
    public void showGetCodeResult(String data) {

    }

    @Override
    public void showRequstError(String data) {

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
