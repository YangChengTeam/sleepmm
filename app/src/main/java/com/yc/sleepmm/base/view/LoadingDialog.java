package com.yc.sleepmm.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yc.sleepmm.R;

import butterknife.BindView;

/**
 * Created by wanglin  on 2018/2/5 16:00.
 */

public class LoadingDialog extends BaseDialog {
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.tv_loading_message)
    TextView tvLoadingMessage;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public void setLoadingMessage(String message) {
        tvLoadingMessage.setText(message);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_dialog_progress_layout;
    }

    @Override
    public void init() {

    }
}
