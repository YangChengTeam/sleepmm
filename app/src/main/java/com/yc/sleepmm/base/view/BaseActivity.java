package com.yc.sleepmm.base.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hwangjr.rxbus.RxBus;
import com.music.player.lib.util.Logger;
import com.umeng.analytics.MobclickAgent;
import com.vondear.rxtools.RxLogTool;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.base.util.EmptyUtils;
import com.yc.sleepmm.base.util.UIUtils;
import com.yc.sleepmm.index.constants.Constant;
import com.yc.sleepmm.index.ui.dialog.LoadingProgressView;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/1/10 14:33.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IView, IDialog {

    protected P mPresenter;
    protected LoadingDialog loadingDialog;

    protected LoadingProgressView loadingProgressedView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        RxBus.get().register(this);
        setContentView(getLayoutId());
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            RxLogTool.e("--> 初始化失败  " + e.getMessage());
        }
        loadingDialog = new LoadingDialog(this);
        loadingProgressedView = new LoadingProgressView(this, true);
        //顶部透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        init();
    }

    @Override
    public void showLoadingDialog(String message) {
        loadingDialog.setLoadingMessage(message);
        if (!isDestroyed())
            loadingDialog.show();
    }

    @Override
    public void dismissDialog() {
        UIUtils.post(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        });
    }


    @Override
    public void showLoadingProgressDialog(String mess, boolean isProgress) {
        if (!this.isFinishing()) {
            if (null != loadingProgressedView) {
                loadingProgressedView.setMessage(mess);
                loadingProgressedView.show();
            }
        }
    }

    @Override
    public void dismissProgressDialog() {
        try {
            if (!this.isFinishing()) {
                if (null != loadingProgressedView && loadingProgressedView.isShowing()) {
                    loadingProgressedView.dismiss();
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.subscribe();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.unsubscribe();
        }
        RxBus.get().unregister(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //用户登录成功
        if (Constant.INTENT_LOGIN_REQUESTCODE == requestCode && Constant.INTENT_LOGIN_RESULTCODE == resultCode) {
            Logger.d("onActivityResult", "登录成功");
        }
    }
}
