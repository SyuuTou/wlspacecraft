package com.wl.spacecraft.dto.requestdto;

import java.util.Date;

public class LoginInputDto {
    /**
     * 手机号码
     */
    private String phone;

    /**
     * 短信验证码
     */
    private String msgCode;

    /**
     * Md5加密校验串
     */
    private String msgValidateStr;
    /**
     * 失效时间
     */
    private Date expire;


    public String getMsgValidateStr() {
        return msgValidateStr;
    }

    public Date getExpire() {
        return expire;
    }

    public String getPhone() {
        return phone;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgValidateStr(String msgValidateStr) {
        this.msgValidateStr = msgValidateStr;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    @Override
    public String toString() {
        return "LoginInputInfoDto{" +
                "msgValidateStr='" + msgValidateStr + '\'' +
                ", expire=" + expire +
                ", phone='" + phone + '\'' +
                ", msgCode='" + msgCode + '\'' +
                '}';
    }
}
