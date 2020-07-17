package com.bmbstack.kit.app;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bmbstack.kit.storage.LibTray;

public class AppHook {
    private static final String TAG = AppHook.class.getSimpleName();

    public interface AppUpgradeHook {

        void onFirstInstallOrFromUninstall(int lastVersionCode, int appVersionCode);

        void onUpgrade(int lastVersionCode, int appVersionCode);

        void onSameVersion(int versionCode);

        void onDowngrading(int lastVersionCode, int appVersionCode);
    }

    public static void checkUpgrade() {
        checkUpgrade(null);
    }

    public static void checkUpgrade(AppUpgradeHook hook) {
        String lastVersionName = LibTray.instance().getString(LibTray.LAST_VERSION_NAME, "1.0.0");
        int lastVersionCode = LibTray.instance().getInt(LibTray.LAST_VERSION_CODE, -1);
        LogUtils.iTag(TAG, "lastVName = ", lastVersionName, " , lastVCode=", lastVersionCode);
        LogUtils.iTag(TAG, "curVName = ", AppUtils.getAppVersionName(), " , curVCode=", String.valueOf(AppUtils.getAppVersionCode()));
        if (lastVersionCode == -1) {
            LogUtils.iTag(TAG, " 当前是首次安装或卸载安装");
            if (hook != null) {
                hook.onFirstInstallOrFromUninstall(lastVersionCode, AppUtils.getAppVersionCode());
            }
        } else if (AppUtils.getAppVersionCode() > lastVersionCode) {
            LogUtils.iTag(TAG, " 当前是升级安装");
            if (hook != null) {
                hook.onUpgrade(lastVersionCode, AppUtils.getAppVersionCode());
            }
        } else if (AppUtils.getAppVersionCode() == lastVersionCode) {
            LogUtils.iTag(TAG, " 当前是平级安装");
            if (hook != null) {
                hook.onSameVersion(AppUtils.getAppVersionCode());
            }
        } else {
            LogUtils.iTag(TAG, " 当前是降级安装");
            if (hook != null) {
                hook.onDowngrading(lastVersionCode, AppUtils.getAppVersionCode());
            }
        }
        LibTray.instance().put(LibTray.LAST_VERSION_NAME, AppUtils.getAppVersionName());
        LibTray.instance().put(LibTray.LAST_VERSION_CODE, AppUtils.getAppVersionCode());
    }
}
