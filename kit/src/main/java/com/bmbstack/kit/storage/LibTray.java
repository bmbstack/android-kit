package com.bmbstack.kit.storage;

import android.content.Context;

import com.bmbstack.kit.app.BaseApplication;

import net.grandcentrix.tray.TrayPreferences;

import androidx.annotation.NonNull;

public class LibTray extends TrayPreferences {

    private LibTray(@NonNull Context context) {
        super(context, "lib", 1);
    }

    public static LibTray instance() {
        return SingleInstance.INSTANCE;
    }

    private static class SingleInstance {
        private static LibTray INSTANCE = new LibTray(BaseApplication.instance());
    }

    //=========================================================================
    //                          key
    //=========================================================================
    public static final String LAST_VERSION_NAME = "last_version_name";
    public static final String LAST_VERSION_CODE = "last_version_code";
}
