package com.bmbstack.kit.app;

import android.app.Application;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.bmbstack.kit.util.XUtils;
import com.facebook.stetho.Stetho;

public abstract class BaseApplication extends Application {
    private static final String TAG = BaseApplication.class.getSimpleName();

    private static BaseApplication sInstance = null;

    public static BaseApplication instance() {
        if (sInstance == null) {
            throw new RuntimeException("BaseApplication ==null ?? you should extends BaseApplication in you app");
        }
        return sInstance;
    }

    public BaseApplication() {
        super();
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(appBuildConfigDebug());
        LogUtils.getConfig().setGlobalTag("bmbstack");

        if (appBuildConfigDebug()) {
            Stetho.initializeWithDefaults(this);
            LogUtils.dTag(TAG, "init Facebook Stetho");
        } else {
            AppUncaughtExceptionHandler appUncaughtExceptionHandler = new AppUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(appUncaughtExceptionHandler);
            LogUtils.iTag(TAG, "set app uncaught exception handler");
        }

        String channel = XUtils.getUmengChannel(this, "bmbstack");
        LogUtils.dTag(TAG, "channel=" + channel);
    }

    protected abstract boolean appBuildConfigDebug();
}