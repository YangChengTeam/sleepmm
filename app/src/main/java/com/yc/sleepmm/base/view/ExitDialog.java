package com.yc.sleepmm.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
    ImageView tvCancel;
    @BindView(R.id.tv_exit)
    ImageView tvExit;

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

        layoutParams.width = ScreenUtil.getWidth(mContext) * 4 / 5;

        window.setAttributes(layoutParams);

        window.setGravity(Gravity.TOP);



        RxView.clicks(tvCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
        RxView.clicks(tvExit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
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


    private onConfirmListener listener;

    public interface onConfirmListener {
        void onConfirm();
    }

    public void setOnConfirmListener(onConfirmListener listener) {
        this.listener = listener;
    }
}
