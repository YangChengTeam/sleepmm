package com.yc.sleepmm.base.util;

import com.blankj.utilcode.util.StringUtils;

/**
 * Created by admin on 2018/2/12.
 */

public class DateUtils {
    public static String getFormatDateInSecond(String second) {
        if (!StringUtils.isEmpty(second)) {
            int seconds = Integer.parseInt(second);
            int temp = 0;
            StringBuffer sb = new StringBuffer();
            temp = seconds / 60;
            sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

            temp = seconds % 60;
            sb.append((temp < 10) ? "0" + temp : "" + temp);

            return sb.toString();
        }
        return null;
    }
}
