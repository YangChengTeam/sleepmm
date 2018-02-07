package com.kk.pay;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangkai on 2017/4/14.
 */

public class OrderParamsInfo {

    private String user_id;

    private String app_id;

    private String title;

    private String money;

    private String imeil;

    private String pay_way_name;

    private List<OrderGood> goods_list;

    private String pay_url = ""; // 支付请求url

    private String type = ""; //商品购买类型

    private String is_payway_split = "1";  //支付方式 划分标识

    private String md5signstr = ""; //现代支付 md5校验值

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getImeil() {
        return imeil;
    }

    public void setImeil(String imeil) {
        this.imeil = imeil;
    }

    public String getPay_way_name() {
        return pay_way_name;
    }

    public void setPay_way_name(String pay_way_name) {
        this.pay_way_name = pay_way_name;
    }

    public List<OrderGood> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<OrderGood> goods_list) {
        this.goods_list = goods_list;
    }

    public String getPay_url() {
        return pay_url;
    }

    public void setPay_url(String pay_url) {
        this.pay_url = pay_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_payway_split() {
        return is_payway_split;
    }

    public void setIs_payway_split(String is_payway_split) {
        this.is_payway_split = is_payway_split;
    }

    public String getMd5signstr() {
        return md5signstr;
    }

    public void setMd5signstr(String md5signstr) {
        this.md5signstr = md5signstr;
    }

    private String dsMoney;

    public String getDsMoney() {
        return dsMoney;
    }

    public void setDsMoney(String dsMoney) {
        this.dsMoney = dsMoney;
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", this.getUser_id());
        params.put("app_id", String.valueOf(1));
        params.put("title", this.getTitle());
        params.put("money", this.getMoney());
        params.put("pay_way_name", this.getPay_way_name());
        params.put("goods_list", JSON.toJSONString(this.getGoods_list()));
        if (md5signstr != null && !md5signstr.isEmpty() && !md5signstr.equalsIgnoreCase("null")) {
            params.put("md5signstr", this.getMd5signstr());
        }
        return params;
    }
}
