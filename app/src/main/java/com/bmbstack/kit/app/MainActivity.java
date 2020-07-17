package com.bmbstack.kit.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ColorUtils;
import com.bmbstack.kit.app.databinding.ActivityMainBinding;
import com.bmbstack.kit.umeng.AuthCallback;
import com.bmbstack.kit.umeng.ShareCallback;
import com.bmbstack.kit.umeng.UmengAgent;
import com.bmbstack.kit.umeng.WeixinInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitleBar.setBgColor(ColorUtils.setAlphaComponent(getColorValue(android.R.color.holo_blue_light), 0.92f));
        mTitleBar.setTextColor(getColorValue(R.color.white));
        mTitleBar.setStatusBarLightMode(false);


        mLoadingLayout.showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingLayout.showContent();
            }
        }, 2000);
    }

    @Override
    protected TitleBarMode getTitleBarMode() {
        return TitleBarMode.FLOAT;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengAgent.onResume(this);
    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text:
                UmengAgent.shareImage(false, MainActivity.this, "uid", "title", "this is content", "https://gold.xitu.io/",
                        "http://static.codeceo.com/images/2015/02/34426f99991154e63015e9e0278638ee.jpg",
                        new ShareCallback() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                toast("success" + share_media.toString());

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                toast("onError" + share_media.toString());
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {
                                toast("onCancel" + share_media.toString());
                            }

                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }
                        });
                break;
            case R.id.music:
                UmengAgent.shareMusic(false, MainActivity.this, "uid", "风吹麦浪", "李健情歌小王子", "http://pic2.ooopic.com/11/45/78/11b1OOOPICc9.jpg",
                        "http://static-dev.qxinli.com/audio/248_20170206_161304/MP3File.mp3", "https://www.baidu.com/",
                        new ShareCallback() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                toast("success" + share_media.toString());

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                toast("onError" + share_media.toString());
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {
                                toast("onCancel" + share_media.toString());
                            }

                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }
                        });
                break;
            case R.id.video:
                UmengAgent.shareVideo(false, MainActivity.this, "uid", "劲爆视频", "给力女司机停车转坏八两奥迪", "http://static.codeceo.com/images/2015/02/34426f99991154e63015e9e0278638ee.jpg",
                        "http://v1.365yg.com/9fc5496a036e2fe82bf813d96fbedaaf/58a2d09f/video/m/220cce02e12c941430fa3f15a7d110f7bc0114320100001a607842bbc9/",
                        "http://www.toutiao.com/a6386833685304312066/",
                        new ShareCallback() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                toast("success" + share_media.toString());
                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                toast("onError" + share_media.toString());
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {
                                toast("onCancel" + share_media.toString());
                            }

                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }
                        });


                break;
            case R.id.login_weixin:
                UmengAgent.loginByWeixin(MainActivity.this, new AuthCallback<WeixinInfo>() {
                    @Override
                    public void onComplete(int var2, WeixinInfo info) {
                        Log.e("dd", info.toString());

                    }

                    @Override
                    public void onError(int var2, Throwable var3) {

                    }

                    @Override
                    public void onCancel(int var2) {

                    }

                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }
                });
                break;
            case R.id.http_api:
                break;
            case R.id.button1:
                OneActivity.launch(this);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengAgent.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengAgent.onDestroy(this);
    }
}