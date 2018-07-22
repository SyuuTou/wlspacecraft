package com.wl.spacecraft.dto.requestdto;

import java.util.Date;

public class UserInfoInputDto {
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
     * token校验字符串
     */
    private String tokenValidateStr;

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public Date getExpire() {
        return expire;
    }

    public String getTokenValidateStr() {
        return tokenValidateStr;
    }

    public void setTokenValidateStr(String tokenValidateStr) {
        this.tokenValidateStr = tokenValidateStr;
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

    @Override
    public String toString() {
        return "UserInfoInputDto{" +
                "phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", expire=" + expire +
                ", tokenValidateStr='" + tokenValidateStr + '\'' +
                '}';
    }
}
