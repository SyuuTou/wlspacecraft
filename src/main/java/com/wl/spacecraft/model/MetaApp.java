package com.wl.spacecraft.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "meta_app")
public class MetaApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 游戏应用名称
     */
    @Column(name = "app_name")
    private String appName;

    /**
     * appkey
     */
    @Column(name = "app_key")
    private String appKey;

    /**
     * 游戏背景图片
     */
    @Column(name = "app_bkground")
    private String appBkground;

    /**
     * app描述
     */
    @Column(name = "app_description")
    private String appDescription;

    /**
     * 自定义排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否有效，0有效，1无效
     */
    @Column(name = "del_flag")
    private Integer delFlag;

    /**
     * 图片的base64存储,
     */
    private String base64;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取游戏应用名称
     *
     * @return app_name - 游戏应用名称
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置游戏应用名称
     *
     * @param appName 游戏应用名称
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获取appkey
     *
     * @return app_key - appkey
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * 设置appkey
     *
     * @param appKey appkey
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * 获取游戏背景图片
     *
     * @return app_bkground - 游戏背景图片
     */
    public String getAppBkground() {
        return appBkground;
    }

    /**
     * 设置游戏背景图片
     *
     * @param appBkground 游戏背景图片
     */
    public void setAppBkground(String appBkground) {
        this.appBkground = appBkground;
    }

    /**
     * 获取app描述
     *
     * @return app_description - app描述
     */
    public String getAppDescription() {
        return appDescription;
    }

    /**
     * 设置app描述
     *
     * @param appDescription app描述
     */
    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    /**
     * 获取自定义排序
     *
     * @return sort - 自定义排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置自定义排序
     *
     * @param sort 自定义排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取是否有效，0有效，1无效
     *
     * @return del_flag - 是否有效，0有效，1无效
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * 设置是否有效，0有效，1无效
     *
     * @param delFlag 是否有效，0有效，1无效
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * 获取图片的base64存储,
     *
     * @return base64 - 图片的base64存储,
     */
    public String getBase64() {
        return base64;
    }

    /**
     * 设置图片的base64存储,
     *
     * @param base64 图片的base64存储,
     */
    public void setBase64(String base64) {
        this.base64 = base64;
    }
}