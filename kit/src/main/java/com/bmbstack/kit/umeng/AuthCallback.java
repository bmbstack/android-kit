package com.bmbstack.kit.umeng;

import com.umeng.socialize.bean.SHARE_MEDIA;

public interface AuthCallback<T extends BaseInfo> {

    void onComplete(int var2, T info);

    void onError(int var2, Throwable throwable);

    void onCancel(int var2);

    //授权开始的回调，可以用来处理等待框，或相关的文字提示
    void onStart(SHARE_MEDIA share_media);
}
