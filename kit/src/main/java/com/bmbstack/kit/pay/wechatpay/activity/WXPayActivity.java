package com.bmbstack.kit.pay.wechatpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bmbstack.kit.pay.wechatpay.wxpay.WXPay;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;


public class WXPayActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXPay.getInstance().getWXApi().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WXPay.getInstance().getWXApi().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            WXPay.getInstance().onResp(baseResp.errCode, baseResp.errStr);
            finish();
        }
    }
}