package com.wl.spacecraft.dto.requestdto;

import java.util.Date;

public class CoinToAccountInputDto {
    private String phone;

    private String token;

    private Date expire;
    /**
     * token校验md5
     */
    private String tokenValidateStr;

    /**
     * 提币的数量
     */
    private Integer integralChange;
    /**
     * 提币地址
     */
    private String address;

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public Date getExpire() {
        return expire;
    }


    public Integer getIntegralChange() {
        return integralChange;
    }

    public String getAddress() {
        return address;
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


    public void setIntegralChange(Integer integralChange) {
        this.integralChange = integralChange;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTokenValidateStr() {
        return tokenValidateStr;
    }

    public void setTokenValidateStr(String tokenValidateStr) {
        this.tokenValidateStr = tokenValidateStr;
    }

    @Override
    public String toString() {
        return "CoinToAccountInputDto{" +
                "phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", expire=" + expire +
                ", tokenValidateStr='" + tokenValidateStr + '\'' +
                ", integralChange=" + integralChange +
                ", address='" + address + '\'' +
                '}';
    }
}
