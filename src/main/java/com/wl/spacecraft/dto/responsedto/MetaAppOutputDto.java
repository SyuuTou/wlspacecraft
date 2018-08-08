package com.wl.spacecraft.dto.responsedto;

public class MetaAppOutputDto {
    /**
     * appkey
     */
    private String appKey;
    /**
     * app唯一标志
     */
    private String appBkground;
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
                ", appName='" + appName + '\'' +
                ", appDescription='" + appDescription + '\'' +
                ", todaytGotAmount=" + todaytGotAmount +
                '}';
    }
}
