package com.yc.sleepmm.index.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseDialog;

/**
 * Created by wanglin  on 2018/2/13 08:55.
 */

public class BindPhoneDialog extends BaseDialog {
    public BindPhoneDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_bind_phone;
    }

    @Override
    public void init() {

    }
}
