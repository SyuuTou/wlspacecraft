package com.wl.spacecraft.dto.responsedto;

import java.util.Date;

public class SendMsgOutputDto {
    /**
     * 短信正确性校验码
     */
    private String msgValidateStr;
    /**
     * 失效时间
     */
    private Date expire;

    /**
     * 发送成功与否标志位
     * true 发送成功
     * false 发送不成功
     */
    private boolean state;

    /**
     * 发送说明
     */
    private String note;

    public String getMsgValidateStr() {
        return msgValidateStr;
    }

    public Date getExpire() {
        return expire;
    }

    public boolean isState() {
        return state;
    }

    public void setMsgValidateStr(String msgValidateStr) {
        this.msgValidateStr = msgValidateStr;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "SendMsgOutputDto{" +
                "msgValidateStr='" + msgValidateStr + '\'' +
                ", expire=" + expire +
                ", state=" + state +
                ", note='" + note + '\'' +
                '}';
    }
}
