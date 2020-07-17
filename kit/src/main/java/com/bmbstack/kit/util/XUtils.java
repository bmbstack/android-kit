package com.bmbstack.kit.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ObjectUtils;
import com.facebook.stetho.BuildConfig;
import com.facebook.stetho.common.LogUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

public class XUtils {
    public static <T> T getSafely(List<?> list, int position) {
        if (checkIndex(list, position)) {
            return (T) list.get(position);
        }
        return null;
    }

    public static <T> T getFirstSafely(List<?> list) {
        return getSafely(list, 0);
    }

    public static <T> T getLastSafely(List<?> list) {
        if (ObjectUtils.isEmpty(list)) {
            return null;
        }
        return getSafely(list, list.size() - 1);
    }

    public static <T> T popFirstSafely(List<?> list) {
        T t = getSafely(list, 0);
        if (t != null) {
            removeSafely(list, 0);
        }
        return t;
    }

    public static String getSafely(String[] array, int position) {
        return getSafely(array, position, null);
    }

    public static String getSafely(String[] array, int position, String defaultValue) {
        if (array != null && position >= 0 && position < array.length) {
            return array[position];
        } else {
            if (BuildConfig.DEBUG) {
                LogUtil.d("array:" + (array == null ? "null" : "length=" + array.length) + ", position=" + position
                );
            }
        }
        return defaultValue;
    }

    public static void clearSafely(Collection<?> collection) {
        if (ObjectUtils.isEmpty(collection)) {
            return;
        }
        collection.clear();
    }

    public static void clearSafely(Map<?, ?> map) {
        if (ObjectUtils.isEmpty(map)) {
            return;
        }
        map.clear();
    }

    public static <T> void addAllSafely(Collection<T> origin, Collection<? extends T> additional) {
        if (origin == null || ObjectUtils.isEmpty(additional)) {
            return;
        }
        origin.addAll(additional);
    }

    public static <T> void addAllSafely(Collection<T> origin, T[] additional) {
        if (additional == null) {
            return;
        }
        addAllSafely(origin, Arrays.asList(additional));
    }

    public static int getCount(List<?> list) {
        return list == null ? 0 : list.size();
    }

    public static int getCount(String[] array) {
        return array == null ? 0 : array.length;
    }

    public static int getLeLengthSafely(String string) {
        return string == null ? 0 : string.length();
    }

    public static void removeSafely(List<?> list, int position) {
        if (checkIndex(list, position)) {
            list.remove(position);
        }
    }

    private static boolean checkIndex(List<?> list, int position) {
        return list != null && position >= 0 && position < list.size();
    }


    public static boolean isJSONObject(String content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if (isBlank(content)) {
            return false;
        }
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isJSONArray(String content) {
        if (isBlank(content)) {
            return false;
        }
        try {
            JSONArray.parseArray(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    // 数据转16进制编码
    public static String encodeHex(final byte[] data, final boolean toLowerCase) {
        final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return new String(out);
    }

    public static String formatDuration(long duration) {
        duration /= 1000; // milliseconds into seconds
        return DateUtils.formatElapsedTime(duration);
    }

    /**
     * 检测是否安装支付宝
     *
     * @param context
     * @return
     */
    public static boolean isAliPayAvilible(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    /**
     * 检测是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isWifiProxy(Context context) {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    public static String getUmengChannel(Context context, String defaultChannel) {
        String channel = defaultChannel;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null) {
                if (applicationInfo.metaData != null) {
                    channel = applicationInfo.metaData.getString("UMENG_CHANNEL");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public static CompletableObserver defaultCompletableObserver() {
        return new CompletableObserver() {
            public void onSubscribe(Disposable disposable) {
                System.out.println("onSubscribe");
            }

            public void onComplete() {
                System.out.println("onComplete");
            }

            public void onError(Throwable throwable) {
                System.out.println("onError, throwable: " + throwable);
            }
        };
    }
}
