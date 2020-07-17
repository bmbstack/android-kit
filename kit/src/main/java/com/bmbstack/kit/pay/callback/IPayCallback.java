package com.bmbstack.kit.pay.callback;


public interface IPayCallback {
    void success();

    void failed(int code, String message);

    void cancel();
}
