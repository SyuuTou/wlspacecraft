package com.lhjl.tzzs.proxy.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "investment_institutions")
public class InvestmentInstitutionsNew {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 机构简称
     */
    @Column(name = "short_name")
    private String shortName;

    /**
     * 机构备注
     */
    private String commet;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 机构类型：1: 50机构，0: 非50机构
     */
    private Integer type;

    /**
     * logoURl
     */
    private String logo;

    /**
     * 案例网址
     */
    @Column(name = "case_url")
    private String caseUrl;

    /**
     * 一句话介绍
     */
    @Column(name = "kenel_case")
    private String kenelCase;

    /**
     * 简介
     */
    private String comment;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 投资阶段
     */
    private String stage;

    /**
     * 客户代表
     */
    private String representative;

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     * 审核状态
     */
    @Column(name = "approval_status")
    private Integer approvalStatus;

    /**
     * 审核时间
     */
    @Column(name = "approval_time")
    private Integer approvalTime;

    public Integer getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Integer approvalTime) {
        this.approvalTime = approvalTime;
    }

    /**
     * 投机项目数量（自定义字段，数据库中不存在）
     */
    @Column(name = "projects_num")
    private Integer projectsNum;

    public Integer getProjectsNum() {
        return projectsNum;
    }

    public void setProjectsNum(Integer projectsNum) {
        this.projectsNum = projectsNum;
    }

    /**
     * @return ID
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
     * 获取机构简称
     *
     * @return short_name - 机构简称
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 设置机构简称
     *
     * @param shortName 机构简称
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * 获取机构备注
     *
     * @return commet - 机构备注
     */
    public String getCommet() {
        return commet;
    }

    /**
     * 设置机构备注
     *
     * @param commet 机构备注
     */
    public void setCommet(String commet) {
        this.commet = commet;
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
     * 获取机构类型：1: 50机构，0: 非50机构
     *
     * @return type - 机构类型：1: 50机构，0: 非50机构
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置机构类型：1: 50机构，0: 非50机构
     *
     * @param type 机构类型：1: 50机构，0: 非50机构
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取logoURl
     *
     * @return logo - logoURl
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 设置logoURl
     *
     * @param logo logoURl
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * 获取案例网址
     *
     * @return case_url - 案例网址
     */
    public String getCaseUrl() {
        return caseUrl;
    }

    /**
     * 设置案例网址
     *
     * @param caseUrl 案例网址
     */
    public void setCaseUrl(String caseUrl) {
        this.caseUrl = caseUrl;
    }

    /**
     * 获取一句话介绍
     *
     * @return kenel_case - 一句话介绍
     */
    public String getKenelCase() {
        return kenelCase;
    }

    /**
     * 设置一句话介绍
     *
     * @param kenelCase 一句话介绍
     */
    public void setKenelCase(String kenelCase) {
        this.kenelCase = kenelCase;
    }

    /**
     * 获取简介
     *
     * @return comment - 简介
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置简介
     *
     * @param comment 简介
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 获取所在城市
     *
     * @return city - 所在城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置所在城市
     *
     * @param city 所在城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取投资阶段
     *
     * @return stage - 投资阶段
     */
    public String getStage() {
        return stage;
    }

    /**
     * 设置投资阶段
     *
     * @param stage 投资阶段
     */
    public void setStage(String stage) {
        this.stage = stage;
    }

    /**
     * 获取客户代表
     *
     * @return representative - 客户代表
     */
    public String getRepresentative() {
        return representative;
    }

    /**
     * 设置客户代表
     *
     * @param representative 客户代表
     */
    public void setRepresentative(String representative) {
        this.representative = representative;
    }
}