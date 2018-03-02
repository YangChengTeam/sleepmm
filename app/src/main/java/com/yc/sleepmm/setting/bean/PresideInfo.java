package com.yc.sleepmm.setting.bean;

import com.yc.sleepmm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2018/3/1 16:41.
 */

public class PresideInfo {

    private String name;
    private int imgId;
    private String desc;

    public PresideInfo() {
    }

    public PresideInfo(String name, int imgId, String desc) {
        this.name = name;
        this.imgId = imgId;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static List<PresideInfo> getPresideInfos() {
        List<PresideInfo> list = new ArrayList<>();
        list.add(new PresideInfo("我是主播阿成", R.mipmap.preside1, "今年二十二岁，从事播音行业已经五年，在上大学期间就从事播音主持行业的兼职，获得许多业内好评，声音温暖磁性，在夜晚由我的声音伴您入眠，希望大家都能做个好梦。"));
        list.add(new PresideInfo("我是主播嘉琪", R.mipmap.preside2, "今年二十一岁，从事电台行业四年，在大学期间就一直在湖北广播电台实习，声音清澈温柔，致力于用声音传递信念，希望听到我的文章能让你们暂时忘记烦恼。"));

        return list;
    }


}
