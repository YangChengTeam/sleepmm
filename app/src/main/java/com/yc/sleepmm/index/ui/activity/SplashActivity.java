package com.yc.sleepmm.index.ui.activity;

import android.content.Intent;
import android.os.Handler;

import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.main.ui.activity.MainActivity;


/**
 * Created by wanglin  on 2018/2/12 09:32.
 */

public class SplashActivity extends BaseActivity {
    private Handler handler = new Handler();

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }
}
