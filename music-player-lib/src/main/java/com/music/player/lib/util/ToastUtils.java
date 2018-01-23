package com.music.player.lib.util;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.music.player.lib.R;
import com.music.player.lib.manager.MusicPlayerManager;

/**
 * TinyHung@outlook.com
 * 2017/3/17 16:56
 */
public class ToastUtils {

    private static TextView sMTv_text;
    private static Toast centerToast;


    public static void showCenterToast(String text) {

        if (null==centerToast) {
            centerToast = new Toast(MusicPlayerManager.getInstance().getContext());
            centerToast.setDuration(Toast.LENGTH_SHORT);
            centerToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
            View view = View.inflate(MusicPlayerManager.getInstance().getContext(), R.layout.toast_center_layout, null);
            sMTv_text = (TextView) view.findViewById(R.id.tv_text);
            if (!TextUtils.isEmpty(text)) {
                sMTv_text.setText(text);
            } else {
                sMTv_text.setText("null");
            }
            centerToast.setView(view);

        } else {
            if (!TextUtils.isEmpty(text)) {
                sMTv_text.setText(text);
            } else {
                sMTv_text.setText("null");
            }
        }
        centerToast.show();
    }
}
