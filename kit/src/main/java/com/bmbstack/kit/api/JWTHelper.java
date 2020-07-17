package com.bmbstack.kit.api;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;

/**
 * Use json web token (jwt)
 */
public class JWTHelper {
    public static final String TAG = JWTHelper.class.getSimpleName();

    public static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    /**
     * @param jwt json web token
     * @return true/false
     */
    public static boolean isExpired(String jwt) {
        Payload payload = getPayLoad(jwt);
        if (payload == null) {
            return true;
        }
        return System.currentTimeMillis() / 1000 >= payload.exp;
    }

    private static Payload getPayLoad(String jwt) {
        String[] jwtArray = jwt.split("\\.");
        if (jwtArray.length != 3) {
            return null;
        }
        String payload = new String(EncodeUtils.base64Decode(jwtArray[1]));
        LogUtils.dTag(TAG, "payload=>" + payload);
        return GsonUtils.fromJson(payload, Payload.class);
    }

    public static String getAuthorizationVal(String jwt) {
        return BEARER + " " + jwt;
    }
}
