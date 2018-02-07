package com.kk.pay;

/**
 * Created by wanglin  on 2018/2/6 14:06.
 */

public class OrderGood {
    public String good_id;// 商品ID
    public int num;// 购买数量（件/月）

    public OrderGood() {
    }

    public OrderGood(String good_id, int num) {
        this.good_id = good_id;
        this.num = num;
    }
}
