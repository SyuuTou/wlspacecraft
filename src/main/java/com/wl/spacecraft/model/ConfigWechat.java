package com.wl.spacecraft.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "config_wechat")
public class ConfigWechat {

    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 自定义排序
     */
    @Column(name = "wechat")
    private String wechat;

    /**
     * 自定义排序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @Column(name = "creator")
    private Integer creator;

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

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "ConfigWechat{" +
                "id=" + id +
                ", wechat=" + wechat +
                ", sort=" + sort +
                ", createTime=" + createTime +
                ", creator=" + creator +
                ", delFlag=" + delFlag +
                '}';
    }
}
