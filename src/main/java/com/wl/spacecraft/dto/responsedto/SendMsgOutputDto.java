package com.wl.spacecraft.dto.responsedto;

import com.wl.spacecraft.model.Community;

import java.util.Date;
import java.util.List;

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

    /**
     * 用户状态
     * register/login
     */
    private String userStatus;
    /**
     * 社区
     */
    private List<Community> communities;

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public List<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(List<Community> communities) {
        this.communities = communities;
    }

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
                ", userStatus='" + userStatus + '\'' +
                ", communities=" + communities +
                '}';
    }
}
