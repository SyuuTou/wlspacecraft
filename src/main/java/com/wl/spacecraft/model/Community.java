package com.wl.spacecraft.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "community")
public class Community {
    /**
     * 社区id_自动生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 社区logo
     */
//    @JsonInclude(Include.NON_NULL)
    @Column(name = "logo")
    private String logo;

    /**
     * 社区名称
     */
    @Column(name = "comm_name")
    private String commName;

    /**
     * 社区描述
     */
    @Column(name = "comm_desc")
    private String commDesc;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

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
     * 创建人
     */
    private Integer creator;

    /**
     * 子群
     */
    @Transient
    private List<CommunityGroup> groups=new ArrayList<>();

    /**
     * 社区投放og的总量
     */
    @Transient
    private Long ogAmount;
    /**
     * 社区logo的base64编码
     */
    @Transient
    private String base64;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public Long getOgAmount() {
        return ogAmount;
    }

    public void setOgAmount(Long ogAmount) {
        this.ogAmount = ogAmount;
    }

    public List<CommunityGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<CommunityGroup> groups) {
        this.groups = groups;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 更新人
     */
    private Integer updator;

    /**
     * 是否有效，0有效，1无效
     */
    @Column(name = "del_flag")
    private Integer delFlag;

    /**
     * 获取社区id_自动生成
     *
     * @return id - 社区id_自动生成
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置社区id_自动生成
     *
     * @param id 社区id_自动生成
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取社区名称
     *
     * @return comm_name - 社区名称
     */
    public String getCommName() {
        return commName;
    }

    /**
     * 设置社区名称
     *
     * @param commName 社区名称
     */
    public void setCommName(String commName) {
        this.commName = commName;
    }

    /**
     * 获取社区描述
     *
     * @return comm_desc - 社区描述
     */
    public String getCommDesc() {
        return commDesc;
    }

    /**
     * 设置社区描述
     *
     * @param commDesc 社区描述
     */
    public void setCommDesc(String commDesc) {
        this.commDesc = commDesc;
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
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取更新人
     *
     * @return updator - 更新人
     */
    public Integer getUpdator() {
        return updator;
    }

    /**
     * 设置更新人
     *
     * @param updator 更新人
     */
    public void setUpdator(Integer updator) {
        this.updator = updator;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {

        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Community{" +
                "id=" + id +
                ", logo='" + logo + '\'' +
                ", commName='" + commName + '\'' +
                ", commDesc='" + commDesc + '\'' +
                ", sort=" + sort +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", groups=" + groups +
                ", ogAmount=" + ogAmount +
                ", base64='" + base64 + '\'' +
                ", updator=" + updator +
                ", delFlag=" + delFlag +
                '}';
    }
}