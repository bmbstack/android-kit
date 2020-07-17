package com.bmbstack.kit.api;

import com.google.gson.annotations.SerializedName;

public class Payload {
    // 该JWT的签发者
    @SerializedName("iss")
    public String iss;
    // 在什么时候签发的
    @SerializedName("iat")
    public long iat;
    // 什么时候过期，这里是一个Unix时间戳
    @SerializedName("exp")
    public long exp;
    //接收该JWT的一方
    @SerializedName("aud")
    public String aud;
    //该JWT所面向的用户
    @SerializedName("sub")
    public String sub;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Payload{");
        sb.append("iss='").append(iss).append('\'');
        sb.append(", iat=").append(iat);
        sb.append(", exp=").append(exp);
        sb.append(", aud='").append(aud).append('\'');
        sb.append(", sub='").append(sub).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
