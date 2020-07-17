package com.bmbstack.kit.umeng;

import com.umeng.socialize.bean.SHARE_MEDIA;

public interface ShareCallback {

    void onResult(SHARE_MEDIA media);

    void onError(SHARE_MEDIA media, Throwable throwable);

    void onCancel(SHARE_MEDIA media);

    void onStart(SHARE_MEDIA share_media);
}
