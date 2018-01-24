package com.music.player.lib.util;

import android.os.Build;

/**
 * TinyHung@Outlook.com
 * 2018/1/18.
 */

public class MusicPlayerUtils {

    /**
     * 生成 min 到 max之间的随机数,包含 min max
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNum(int min,int max) {
        return min + (int)(Math.random() * max);
    }

    /**
     * 格式化时间
     * @param seconds 单位秒
     * @return
     * 当时间小于半个小时
     */
    public static String stringForTime(long seconds) {
        if(seconds<=0) return "无限制";
        if(seconds >= 24 * 60 * 60 ) return "24小时";
        if(seconds<3600){//如果是再一个小时以内，直接返回分钟数
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;
            return minutes+":"+remainingSeconds;
        }else{
            //否则返回小时和分钟
            long hours = seconds/60/60;
            long minutes =(seconds-60*60)/60;//分钟=减去一个小时后，剩余的分钟
            long remainingSeconds = seconds % 60;
            return hours+":"+minutes+":"+remainingSeconds;
        }
    }

    /**
     * 将秒格式化为小时分钟
     * @param timeMs
     * @return
     */
    public static String stringHoursForTime(long timeMs) {
        if(timeMs<=0) return "无限制";
        if(timeMs >= 24 * 60 * 60 ) return "24小时";
        if(timeMs<3600){//如果是在一个小时以内
            return timeMs/60+"分钟";
        }else{
            long hours = timeMs/60/60;
            long minutes =(timeMs-60*60)/60;
            return hours+"小时"+minutes+"分钟";
        }
    }

    public static boolean isJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
}
