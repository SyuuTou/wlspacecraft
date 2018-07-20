package com.lhjl.tzzs.proxy.dto;

public class ProjectComplexOutputDto {
    /**
     * 项目简称
     */
    private String projectShortName;
    /**
     * 项目id
     */
    private Integer projectId;
    /**
     * 项目一句话介绍
     */
    private String projectDesc;
    /**
     * 项目logo
     */
    private String projectLogo;

    /**
     * 细分领域
     */
    private String segmentation;

    /**
     * 地域
     */
    private String city;

    /**
     * 轮次
     */
    private String round;

    /**
     * 项目投资亮点
     */
    private String projectInvestmentHighlights;

    /**
     * 项目类型：1代表创始人提交项目，0表示数据导入项目
     */
    private Integer projectType;

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public String getProjectInvestmentHighlights() {
        return projectInvestmentHighlights;
    }

    public void setProjectInvestmentHighlights(String projectInvestmentHighlights) {
        this.projectInvestmentHighlights = projectInvestmentHighlights;
    }

    public String getSegmentation() {
        return segmentation;
    }

    public void setSegmentation(String segmentation) {
        this.segmentation = segmentation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public String getProjectLogo() {
        return projectLogo;
    }

    public void setProjectLogo(String projectLogo) {
        this.projectLogo = projectLogo;
    }
}
