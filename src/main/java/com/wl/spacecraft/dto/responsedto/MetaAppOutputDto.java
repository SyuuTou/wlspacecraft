package com.wl.spacecraft.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class MetaAppOutputDto {
    /**
     * appkey
     */
    private String appKey;
    /**
     * app唯一标志
     */

//    @JsonInclude(Include.NON_NULL)
    private String appBkground;
    /**
     * 图片的字节数组
     */
    private String base64;
    /**
     * app名称
     */
    private String appName;
    /**
     * app描述
     */
    private String appDescription;
    /**
     * 今日在该app中已经获取的OG总量
     */
    private Integer todaytGotAmount;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppBkground() {
        return appBkground;
    }

    public void setAppBkground(String appBkground) {
        this.appBkground = appBkground;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public Integer getTodaytGotAmount() {
        return todaytGotAmount;
    }

    public void setTodaytGotAmount(Integer todaytGotAmount) {
        this.todaytGotAmount = todaytGotAmount;
    }

    @Override
    public String toString() {
        return "MetaAppOutputDto{" +
                "appKey='" + appKey + '\'' +
                ", appBkground='" + appBkground + '\'' +
                ", base64='" + base64 + '\'' +
                ", appName='" + appName + '\'' +
                ", appDescription='" + appDescription + '\'' +
                ", todaytGotAmount=" + todaytGotAmount +
                '}';
    }
}
