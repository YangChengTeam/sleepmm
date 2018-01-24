package com.yc.sleepmm.setting.ui.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TinyHung@Outlook.com
 * 2018/1/22.
 */

public class Utils {
    /**
     * 判断是否是常用11位数手机号
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    /**
     * 是否是6或者4位数字验证码
     * @param phoneNumber
     * @param type 6 或 4
     * @return
     */
    public static boolean isNumberCode(String phoneNumber,int type) {
        String rex="^\\d{4}$";
        if(type==6){
            rex="^\\d{6}$";
        }
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }
}
