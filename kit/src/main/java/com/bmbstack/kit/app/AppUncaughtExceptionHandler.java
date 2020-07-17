package com.bmbstack.kit.app;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bmbstack.kit.util.XUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class AppUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler defaultHandler;

    private final Map<String, String> infoMap = new LinkedHashMap<String, String>();
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);

    public AppUncaughtExceptionHandler() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        LogUtils.eTag("uncaught exception @ thread " + thread.getId() + ", err: " + throwable);
        try {
            collectDeviceInfo(BaseApplication.instance());
            saveCrashInfo2File(throwable);
            saveHeapDump2File(throwable);
        } catch (Exception e) {
            LogUtils.eTag("error writing crash log", "", e);
        } finally {
            defaultHandler.uncaughtException(thread, throwable);
        }
    }

    public void collectDeviceInfo(Context context) {
        infoMap.put("PackageName", context.getPackageName());
        infoMap.put("VersionName", AppUtils.getAppVersionName());
        infoMap.put("VersionCode", String.valueOf(AppUtils.getAppVersionCode()));
        infoMap.put("ChannelCode", XUtils.getUmengChannel(context, "bmbstack"));
        infoMap.put("ProcessName", getCurProcessName(context));
        infoMap.put("ThreadName", Thread.currentThread().getName());
        infoMap.put("=", "==============================");

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infoMap.put(field.getName(), field.get("").toString());
            } catch (Exception e) {
            }
        }
        infoMap.put("==", "=============================");
    }

    public static String getCurProcessName(Context ctx) {
        String currentProcName = "";
        try {
            int pid = android.os.Process.myPid();
            ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            // isolatedProcess属性为true的process 调用getRunningAppProcesses会抛出异常
            for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
                if (processInfo.pid == pid) {
                    currentProcName = processInfo.processName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentProcName;
    }

    private String saveCrashInfo2File(Throwable ex) {
        String fileName = null;

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        sb.append(writer.toString());
        try {
            fileName = String.format("crash-%s.txt", formatter.format(new Date()));
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                final String path =
                        Environment.getExternalStorageDirectory() + "/" + BaseApplication.instance()
                                .getPackageName() + "/log";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
        } catch (Exception e) {
            LogUtils.eTag("an error occured while writing file..." + e.getMessage());
        }

        return fileName;
    }

    private void saveHeapDump2File(Throwable throwable) {
        if (isOutOfMemoryError(throwable)) {
            try {
                String fileName = String.format("crash-%s.hprof", formatter.format(new Date()));
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    final String path =
                            Environment.getExternalStorageDirectory() + "/" + BaseApplication.instance()
                                    .getPackageName() + "/log";
                    File dir = new File(path);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    Debug.dumpHprofData(path + File.separator + fileName);
                }
            } catch (Exception ex) {
                LogUtils.eTag("couldn't dump hprof:" + ex.getMessage());
            }
        }
    }

    private static boolean isOutOfMemoryError(Throwable ex) {
        if (OutOfMemoryError.class.equals(ex.getClass())) {
            return true;
        } else {
            Throwable cause = ex.getCause();
            while (null != cause) {
                if (OutOfMemoryError.class.equals(cause.getClass())) {
                    return true;
                }
                cause = cause.getCause();
            }
        }
        return false;
    }
}

