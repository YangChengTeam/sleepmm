package com.yc.sleepmm.base.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.yc.sleepmm.R;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/2/5 15:53.
 */

public abstract class BaseDialog extends Dialog implements IView {
    protected Context mContext;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialogStyle);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(getLayoutId(), null);
        try {
            ButterKnife.bind(this, view);
        } catch (Exception e) {
            LogUtils.e(getClass().getName(), "--> 初始化失败 " + e.getMessage());
        }
        setContentView(view);
        setCancelable(true);
        init();

    }

}
