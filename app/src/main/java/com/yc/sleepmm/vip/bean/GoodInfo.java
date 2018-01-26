package com.yc.sleepmm.vip.bean;

/**
 * Created by wanglin  on 2018/1/25 12:00.
 */

public class GoodInfo {

    private int id;
    private String name;
    private String price;

    public GoodInfo() {
    }

    public GoodInfo(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
