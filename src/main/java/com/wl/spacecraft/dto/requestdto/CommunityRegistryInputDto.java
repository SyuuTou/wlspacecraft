package com.wl.spacecraft.dto.requestdto;

import java.util.Date;

public class CommunityRegistryInputDto {
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
    /**
     * 社区id
     */
    private Integer communityId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpire() {
        return expire;
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

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }
}
