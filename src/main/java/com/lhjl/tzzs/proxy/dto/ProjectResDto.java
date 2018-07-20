package com.lhjl.tzzs.proxy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class ProjectResDto {

    /** 项目ID */
    private Integer projectId;
    /** 项目简称 */
    private String shortName;
    /** 项目全称 */
    private String fullName;
    /** 一句话介绍 */
    private String kernelDesc;
    /** 城市 */
    private String city;
    /** 领域 */
    private String segmentation;
    /**  */
    private String evaluationRecommend;
    /** 阶段 */
    private String stage;
    /** 关注数量 */
    private Integer followNum;
    /** 约谈数量 */
    private Integer interviewNum;
    /**  */
    private Integer yn;

    /**
     * 项目关注状态
     */
    private Integer status;

    /**项目logo*/
    private String projectLogo;

    /**融资时间*/
    private String financingTime;





    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getKernelDesc() {
        return kernelDesc;
    }

    public void setKernelDesc(String kernelDesc) {
        this.kernelDesc = kernelDesc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSegmentation() {
        return segmentation;
    }

    public void setSegmentation(String segmentation) {
        this.segmentation = segmentation;
    }

    public String getEvaluationRecommend() {
        return evaluationRecommend;
    }

    public void setEvaluationRecommend(String evaluationRecommend) {
        this.evaluationRecommend = evaluationRecommend;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Integer getInterviewNum() {
        return interviewNum;
    }

    public void setInterviewNum(Integer interviewNum) {
        this.interviewNum = interviewNum;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProjectLogo() {
        return projectLogo;
    }

    public void setProjectLogo(String projectLogo) {
        this.projectLogo = projectLogo;
    }

    public String getFinancingTime() {
        return financingTime;
    }

    public void setFinancingTime(String financingTime) {
        this.financingTime = financingTime;
    }
}
