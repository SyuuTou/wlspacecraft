package com.wl.spacecraft.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "community_group")
public class CommunityGroup {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 社区id
     */
    @Column(name = "community_id")
    private Integer communityId;

    /**
     * 子群名称
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 子群描述
     */
    @Column(name = "group_desc")
    private String groupDesc;

    /**
     * 自定义排序
     */
    @Column(name = "sort")
    private String sort;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新者
     */
    @Column(name = "updator")
    private Integer updator;

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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdator() {
        return updator;
    }

    public void setUpdator(Integer updator) {
        this.updator = updator;
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
        return "CommunityGroup{" +
                "id=" + id +
                ", communityId=" + communityId +
                ", groupName='" + groupName + '\'' +
                ", groupDesc='" + groupDesc + '\'' +
                ", sort='" + sort + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", updator=" + updator +
                ", creator=" + creator +
                ", delFlag=" + delFlag +
                '}';
    }
}
