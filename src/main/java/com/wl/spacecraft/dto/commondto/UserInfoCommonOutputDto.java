package com.wl.spacecraft.dto.commondto;

import java.util.Date;

/**
 * 用户数据输出实体
 */
public class UserInfoCommonOutputDto {
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 此轮登录生成的token
     */
    private String token;
    /**
     * 该轮token的过期时间
     */
    private Date expire;
    /**
     * token校验码
     */
    private String tokenValidateStr;

    /**
     * 积分总数
     */
    private Integer amount;

    /**
     * 今日已获取og数量
     */
    private Integer limit;

    /**
     * og币每日获取上限
     */
    private Integer topLimit;


    public Integer getTopLimit() {
        return topLimit;
    }

    public void setTopLimit(Integer topLimit) {
        this.topLimit = topLimit;
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

    public String getTokenValidateStr() {
        return tokenValidateStr;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getLimit() {
        return limit;
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

    public void setTokenValidateStr(String tokenValidateStr) {
        this.tokenValidateStr = tokenValidateStr;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "UserInfoCommonOutputDto{" +
                "phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", expire=" + expire +
                ", tokenValidateStr='" + tokenValidateStr + '\'' +
                ", amount=" + amount +
                ", limit=" + limit +
                ", topLimit=" + topLimit +
                '}';
    }
}
