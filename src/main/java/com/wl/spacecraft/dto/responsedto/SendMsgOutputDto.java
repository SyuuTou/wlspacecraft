package com.wl.spacecraft.dto.responsedto;

public class SendMsgOutputDto {
    /**
     * 验证码是否失效标识
     */
    private String verification;
    /**
     * 失效时间
     */
    private String deadTime;

    /**
     * 成功或失败标识
     */
    private String state;//1：失败  0：成功

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SendMsgOutputDto{" +
                "verification='" + verification + '\'' +
                ", deadTime='" + deadTime + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
