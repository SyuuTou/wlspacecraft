package com.wl.spacecraft.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

public class MetaApp {
    /**
     * appid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * app名称
     */
    @Column(name = "app_name")
    private String appName;

    /**
     * app代码
     */
    @Column(name = "app_key")
    private String appKey;

    /**
     * app背景图片
     */
    @Column(name = "app_bkground")
    private String appBkground;

    /**
     * 图片的字节数组
     */
    @Transient
    private byte[] bytes;


    /**
     * app描述
     */
    @Column(name = "app_description")
    private String appDescription;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 删除标志
     * 0 有效
     * 1 无效
     */
    @Column(name = "del_flag")
    private Integer delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {

        this.delFlag = delFlag;
    }

    public String getAppBkground() {
        return appBkground;
    }

    public void setAppBkground(String appBkground) {
        this.appBkground = appBkground;
    }


    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "MetaApp{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", appKey='" + appKey + '\'' +
                ", appBkground='" + appBkground + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                ", appDescription='" + appDescription + '\'' +
                ", createTime=" + createTime +
                ", delFlag=" + delFlag +
                '}';
    }
}
