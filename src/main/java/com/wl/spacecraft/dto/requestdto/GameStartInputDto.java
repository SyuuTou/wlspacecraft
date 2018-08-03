package com.wl.spacecraft.dto.requestdto;

import java.util.Date;

public class GameStartInputDto {
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 用户token
     */
    private String token;
    /**
     * 过期时间
     */
    private Date expire;
    /**
     * 校验字符串
     */
    private String tokenValidateStr;

    /**
     * appkey
     * SPACECRAFT|LEEKREAP|JUMP|BRICKS
     */
    private String appKey;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public Date getExpire() {
        return expire;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public String getTokenValidateStr() {
        return tokenValidateStr;
    }

    public void setTokenValidateStr(String tokenValidateStr) {
        this.tokenValidateStr = tokenValidateStr;
    }

    @Override
    public String
    toString() {
        return "GameStartInputDto{" +
                "phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", expire=" + expire +
                ", tokenValidateStr='" + tokenValidateStr + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }
}
