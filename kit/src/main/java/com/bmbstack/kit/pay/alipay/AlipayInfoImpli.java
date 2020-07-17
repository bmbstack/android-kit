package com.bmbstack.kit.pay.alipay;

import com.bmbstack.kit.pay.base.IPayInfo;


public class AlipayInfoImpli implements IPayInfo {

    private String orderInfo;

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

}
