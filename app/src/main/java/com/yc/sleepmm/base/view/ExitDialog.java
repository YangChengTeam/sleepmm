package com.yc.sleepmm.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.yc.sleepmm.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/2/7 13:28.
 */

public class ExitDialog extends BaseDialog {
    @BindView(R.id.tv_tint_content)
    TextView tvTintContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    public ExitDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_exit;
    }

    @Override
    public void init() {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        layoutParams.width = ScreenUtil.getWidth(mContext) * 3 / 5;
        window.setAttributes(layoutParams);

        RxView.clicks(tvCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
        RxView.clicks(tvConfirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (listener != null) {
                    listener.onConfirm();
                }
            }
        });
    }

    public void setTvTintContent(String content) {
        tvTintContent.setText(content);
    }

    public void setTvCancel(String cancelContent) {
        tvCancel.setText(cancelContent);
    }

    public void setTvConfirm(String confirmContent) {
        tvConfirm.setText(confirmContent);
    }

    private onConfirmListener listener;

    public interface onConfirmListener {
        void onConfirm();
    }

    public void setOnConfirmListener(onConfirmListener listener) {
        this.listener = listener;
    }
}
