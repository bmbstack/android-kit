package com.bmbstack.kit.pay.base;

import android.app.Activity;

import com.bmbstack.kit.pay.callback.IPayCallback;


public interface IPayStrategy<T extends IPayInfo> {
    void pay(Activity activity, T payInfo, IPayCallback payCallback);
}
