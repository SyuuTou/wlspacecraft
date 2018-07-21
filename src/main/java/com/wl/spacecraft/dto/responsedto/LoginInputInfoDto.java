package com.wl.spacecraft.dto.responsedto;

public class LoginInputInfoDto {
    /**
     * 验证码是否失效标识
     */
    private String verification;
    /**
     * 失效时间
     */
    private String deadTime;

    /**
     * 手机号码
     */
    private String phone;

    /**
     *
     */
    private String verifyCode;

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(String deadTime) {
        this.deadTime = deadTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
        return "LoginInputInfoDto{" +
                "verification='" + verification + '\'' +
                ", deadTime='" + deadTime + '\'' +
                ", phone='" + phone + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}
