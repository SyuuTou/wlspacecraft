package com.lhjl.tzzs.proxy.dto;

import java.util.Date;

public class ProjectSendBAdminListOutputDto {
    /**提交项目审核记录id*/
    private Integer id;
    /**
     * 项目来源标志
     */
    private Integer projectSourceFlag;
    
    /**
     * 项目来源输出字符串
     */
    private String projectSource;

    /**项目简称*/
    private String projectShortName;

    /**项目全称*/
    private String projectFullName;

    /**项目一句话介绍*/
    private String kernelDesc;

    /**项目领域*/
    private String segmentationName;

    /**城市*/
    private String city;

    /**用户名称*/
    private String userName;

    /**公司名称*/
    private String companyName;

    /**公司职位*/
    private String companyDuties;

    /**手机号码*/
    private String phonenumber;
    /*
     * 项目评级
     */
    private Integer ratingStage;
    
    /**
     * 项目评级输出字符串
     */
    private String projectLevel;

    /*
     * 提交时间
     */
    private Date creatTime;

    /**
     * 审核状态标志
     */
    private Integer auditStatusFlag;
    
    /**
     * 审核状态转换字符串
     */
    private String auditStatus;

    /**审核时间*/
    private Date auditTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
    }

    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public String getProjectFullName() {
        return projectFullName;
    }

    public void setProjectFullName(String projectFullName) {
        this.projectFullName = projectFullName;
    }

    public String getKernelDesc() {
        return kernelDesc;
    }

    public void setKernelDesc(String kernelDesc) {
        this.kernelDesc = kernelDesc;
    }

    public String getSegmentationName() {
        return segmentationName;
    }

    public void setSegmentationName(String segmentationName) {
        this.segmentationName = segmentationName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDuties() {
        return companyDuties;
    }

    public void setCompanyDuties(String companyDuties) {
        this.companyDuties = companyDuties;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getProjectLevel() {
        return projectLevel;
    }

    public void setProjectLevel(String projectLevel) {
        this.projectLevel = projectLevel;
    }

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getProjectSourceFlag() {
		return projectSourceFlag;
	}

	public void setProjectSourceFlag(Integer projectSourceFlag) {
		this.projectSourceFlag = projectSourceFlag;
	}

	public Integer getRatingStage() {
		return ratingStage;
	}

	public void setRatingStage(Integer ratingStage) {
		this.ratingStage = ratingStage;
	}

	public Integer getAuditStatusFlag() {
		return auditStatusFlag;
	}

	public void setAuditStatusFlag(Integer auditStatusFlag) {
		this.auditStatusFlag = auditStatusFlag;
	}
	

}
