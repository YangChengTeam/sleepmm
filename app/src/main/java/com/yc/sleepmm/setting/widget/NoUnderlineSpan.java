package com.yc.sleepmm.setting.widget;

import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * Created by wanglin  on 2018/3/5 11:39.
 */

public class NoUnderlineSpan extends URLSpan {
    public NoUnderlineSpan(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (ds != null) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }

    }
}
