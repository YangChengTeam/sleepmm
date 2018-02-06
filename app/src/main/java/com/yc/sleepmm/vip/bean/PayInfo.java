package com.yc.sleepmm.vip.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2018/1/25 14:41.
 */

public class PayInfo {
    @JSONField(name = "pay_way_title")
    private String title;
    @JSONField(name = "pay_way_name")
    private String payway;

    public PayInfo() {
    }

    public PayInfo(String title, String payway) {
        this.title = title;
        this.payway = payway;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }
}
