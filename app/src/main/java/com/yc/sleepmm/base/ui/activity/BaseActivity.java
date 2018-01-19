package com.yc.sleepmm.base.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/1/10 14:33.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
//            RxLogUtils.e("--> 初始化失败");
        }
        init();
    }

    public abstract int getLayoutId();

    public abstract void init();
}
