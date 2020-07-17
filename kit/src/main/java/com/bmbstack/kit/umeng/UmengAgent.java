package com.bmbstack.kit.umeng;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;

import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class UmengAgent {
    private static Context context;
    private static UMShareAPI umShareAPI;
    private static SHARE_MEDIA[] shareMedias;//SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN_CIRCLE

    public static int REQUEST_PERMISSION_CODE = 899;
    public static final String[] PERMISSION_LIST;

    static {
        if (Build.VERSION.SDK_INT >= 16) {
            PERMISSION_LIST = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        } else {
            PERMISSION_LIST = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }

    public static void setLogEnable(boolean degbug) {
        UMConfigure.setLogEnabled(degbug);
    }

    public static void init(Context ctx, String appKey, String channel, String pushSecret, boolean debuggable, SHARE_MEDIA... shareMediaList) {
        context = ctx;
        shareMedias = shareMediaList;

        UMConfigure.init(ctx, appKey, channel, UMConfigure.DEVICE_TYPE_PHONE, pushSecret); // 初始化SDK
        UMConfigure.setLogEnabled(debuggable);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);  // 选用AUTO页面采集模式, 只收集4.0以上
        UMConfigure.setProcessEvent(true);

        umShareAPI = UMShareAPI.get(ctx);
    }

    //==============================设置==========================================
    public static void setKeySecretWeixin(String key, String secret) {
        PlatformConfig.setWeixin(key, secret);
    }

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }

    public static void onDestroy(Activity activity) {
        UMShareAPI.get(activity).release();
    }

    //============================统计============================================
    public static void reportError(Context context, String error) {
        MobclickAgent.reportError(context, error);
    }

    public static void reportError(Context context, Throwable throwable) {
        MobclickAgent.reportError(context, throwable);
    }

    //============================分析============================================
    public static void onKillProcess() {
        MobclickAgent.onKillProcess(context);
    }

    public static void onResume(Activity activity) {
        MobclickAgent.onResume(activity);
    }

    public static void onPause(Activity activity) {
        MobclickAgent.onPause(activity);
    }

    public static void onPageStart(String pageName) {
        MobclickAgent.onPageStart(pageName);
    }

    public static void onPageEnd(String pageName) {
        MobclickAgent.onPageEnd(pageName);
    }

    public static void onProfileSignIn(String ID) {
        MobclickAgent.onProfileSignIn(ID);
    }

    public static void onProfileSignIn(String provider, String ID) {
        MobclickAgent.onProfileSignIn(provider, ID);
    }

    public static void onProfileSignOff() {
        MobclickAgent.onProfileSignOff();
    }

    public static void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    public static void onEvent(Context context, String eventId, String label) {
        MobclickAgent.onEvent(context, eventId, label);
    }

    public static void onEvent(Context context, String eventId, HashMap map) {
        MobclickAgent.onEvent(context, eventId, map);
    }

    public static void onEventValue(Context context, String eventId, Map<String, String> map, int value) {
        MobclickAgent.onEventValue(context, eventId, map, value);
    }

    public static void onEventObject(Context context, String eventId, HashMap map) {
        MobclickAgent.onEventObject(context, eventId, map);
    }

    //============================登录============================================
    public static void loginByWeixin(Activity activity, AuthCallback callback) {
        login(activity, SHARE_MEDIA.WEIXIN, callback);
    }

    private static boolean checkPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : PERMISSION_LIST) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            permission)) {
                        // show explanation
                        Log.e("Umeng", "permission " + permission + " is no granted!!");
                    } else {
                        ActivityCompat.requestPermissions(activity, PERMISSION_LIST, REQUEST_PERMISSION_CODE);
                        Log.e("Umeng", "Implicit request permission=" + permission);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static void login(Activity activity, final SHARE_MEDIA platform, final AuthCallback callback) {
        umShareAPI.getPlatformInfo(activity, platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                callback.onStart(share_media);
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int action, Map<String, String> data) {
                BaseInfo info = null;
                if (share_media == SHARE_MEDIA.WEIXIN) {
                    WeixinInfo weixinInfo = new WeixinInfo();
                    weixinInfo.openid = data.get("openid");
                    weixinInfo.country = data.get("country");
                    info = weixinInfo;
                } else if (share_media == SHARE_MEDIA.SINA) {
                    SinaInfo sinaInfo = new SinaInfo();
                    sinaInfo.followers_count = data.get("followers_count");
                    sinaInfo.friends_count = data.get("friends_count");
                    info = sinaInfo;
                } else if (share_media == SHARE_MEDIA.QQ) {
                    QQInfo qqInfo = new QQInfo();
                    qqInfo.is_yellow_year_vip = data.get("is_yellow_year_vip");
                    info = qqInfo;
                }

                info.accessToken = data.get("accessToken");
                info.city = data.get("city");
                info.expiration = data.get("expiration");
                info.gender = data.get("gender");
                info.iconurl = data.get("iconurl");
                info.name = data.get("name");
                info.province = data.get("province");
                info.refreshtoken = data.get("refreshtoken");
                info.uid = data.get("uid");

                callback.onComplete(action, info);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                callback.onError(i, throwable);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                callback.onCancel(i);
            }
        });
    }

    //============================分享============================================
    public static void shareImage(boolean usePermission, Activity activity, final String uid,
                                  String title, String desc, String thumbUrl,
                                  String targetUrl, final ShareCallback callback) {
        share(usePermission, 0, activity, uid, title, desc, thumbUrl, targetUrl, targetUrl, callback);
    }

    public static void shareMusic(boolean usePermission, Activity activity, final String uid,
                                  String title, String desc, String thumbUrl, String musicUrl,
                                  String targetUrl, final ShareCallback callback) {
        share(usePermission, 1, activity, uid, title, desc, thumbUrl, musicUrl, targetUrl, callback);
    }

    public static void shareVideo(boolean usePermission, Activity activity, final String uid,
                                  String title, String desc, String thumbUrl, String musicUrl,
                                  String targetUrl, final ShareCallback callback) {
        share(usePermission, 2, activity, uid, title, desc, thumbUrl, musicUrl, targetUrl, callback);
    }

    public static void shareLink(boolean usePermission, Activity activity, final String uid,
                                 String title, String desc, String thumbUrl,
                                 String targetUrl, final ShareCallback callback) {
        share(usePermission, 3, activity, uid, title, desc, "", thumbUrl, targetUrl, callback);
    }

    /**
     * 自带分享的统计,其中uid是app本身的账户系统的id
     *
     * @param type      类型
     * @param activity  activity实例
     * @param uid       user id
     * @param title     标题
     * @param desc      描述
     * @param thumbUrl  缩路图url
     * @param mediaUrl  媒体url
     * @param targetUrl 地址
     * @param callback  回调
     */
    private static void share(boolean usePermission, int type, Activity activity, final String uid,
                              String title, String desc, String thumbUrl, String mediaUrl,
                              String targetUrl, final ShareCallback callback) {
        if (usePermission && !checkPermissions(activity)) {
            return;
        }
        ShareAction action = new ShareAction(activity);
        if (type == 0) { // image
            action.withMedia(new UMImage(activity, mediaUrl));
        } else if (type == 1) { // music
            UMusic music = new UMusic(mediaUrl);
            music.setTitle(title);//音乐的标题
            music.setThumb(new UMImage(activity, thumbUrl));//音乐的缩略图
            music.setDescription(desc);//音乐的描述
            music.setmTargetUrl(targetUrl);
            action.withMedia(music);
        } else if (type == 2) { // video
            UMVideo video = new UMVideo(mediaUrl);
            video.setTitle(title);//音乐的标题
            video.setThumb(new UMImage(activity, thumbUrl));//音乐的缩略图
            video.setDescription(desc);//音乐的描述
            action.withMedia(video);
        } else if (type == 3) { // link
            UMWeb web = new UMWeb(mediaUrl);
            web.setTitle(title);
            UMImage img = new UMImage(activity, thumbUrl);
            web.setThumb(img);
            web.setDescription(desc);
            action.withMedia(web);
        }
        action.withText(desc)
                .setDisplayList(shareMedias)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        callback.onStart(share_media);
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        callback.onResult(share_media);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        callback.onError(share_media, throwable);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        callback.onCancel(share_media);
                    }
                })
                .open();
    }
}
