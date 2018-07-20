package com.lhjl.tzzs.proxy.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "investment_institutions_project")
public class InvestmentInstitutionsProject {
    /**
     * 融资历史轮次ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 机构ID
     */
    @Column(name = "investment_institutions_id")
    private Integer investmentInstitutionsId;

    /**
     * 投资方备注
     */
    @Column(name = "investment_desc")
    private String investmentDesc;

    /**
     * 是否领投方,0代表否；1代表是
     */
    private Integer status;

    /**
     * 投资金额
     */
    @Column(name = "investment_amount")
    private BigDecimal investmentAmount;

    /**
     * 投资币种（0人民币/1美元）
     */
    private Integer currency;

    /**
     * 所占股份
     */
    @Column(name = "stock_share")
    private BigDecimal stockShare;

    /**
     * 投资到账时间
     */
    @Column(name = "accounting_date")
    private Date accountingDate;

    /**
     * 删除标志:0代表有效；1代表无效
     */
    private Integer yn;
    /**
     * 投资机构简称
     */
    @Transient
    private String investmentShortName;
    /**
     * 时间输入转换字符串
     */
    @Transient
    private String accountingDateStr;
    /**
     * 时间输出转换字符串
     */
    @Transient
    private String accountingDateOutputStr;  
    /**
     * 主体类型
     */
    @Transient
    private Integer subjectType;
    
	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

	public String getAccountingDateStr() {
		return accountingDateStr;
	}

	public void setAccountingDateStr(String accountingDateStr) {
		this.accountingDateStr = accountingDateStr;
	}

	public String getAccountingDateOutputStr() {
		return accountingDateOutputStr;
	}

	public void setAccountingDateOutputStr(String accountingDateOutputStr) {
		this.accountingDateOutputStr = accountingDateOutputStr;
	}

	public String getInvestmentShortName() {
		return investmentShortName;
	}

	public void setInvestmentShortName(String investmentShortName) {
		this.investmentShortName = investmentShortName;
	}

	/**
     * 获取项目ID
     *
     * @return project_id - 项目ID
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 设置项目ID
     *
     * @param projectId 项目ID
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取机构ID
     *
     * @return investment_institutions_id - 机构ID
     */
    public Integer getInvestmentInstitutionsId() {
        return investmentInstitutionsId;
    }

    /**
     * 设置机构ID
     *
     * @param investmentInstitutionsId 机构ID
     */
    public void setInvestmentInstitutionsId(Integer investmentInstitutionsId) {
        this.investmentInstitutionsId = investmentInstitutionsId;
    }

    /**
     * 获取投资方备注
     *
     * @return investment_desc - 投资方备注
     */
    public String getInvestmentDesc() {
        return investmentDesc;
    }

    /**
     * 设置投资方备注
     *
     * @param investmentDesc 投资方备注
     */
    public void setInvestmentDesc(String investmentDesc) {
        this.investmentDesc = investmentDesc;
    }

    /**
     * 获取是否领投方,0代表否；1代表是
     *
     * @return status - 是否领投方,0代表否；1代表是
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置是否领投方,0代表否；1代表是
     *
     * @param status 是否领投方,0代表否；1代表是
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取投资金额
     *
     * @return investment_amount - 投资金额
     */
    public BigDecimal getInvestmentAmount() {
        return investmentAmount;
    }

    /**
     * 设置投资金额
     *
     * @param investmentAmount 投资金额
     */
    public void setInvestmentAmount(BigDecimal investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    /**
     * 获取投资币种（0人民币/1美元）
     *
     * @return currency - 投资币种（0人民币/1美元）
     */
    public Integer getCurrency() {
        return currency;
    }

    /**
     * 设置投资币种（0人民币/1美元）
     *
     * @param currency 投资币种（0人民币/1美元）
     */
    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    /**
     * 获取所占股份
     *
     * @return stock_share - 所占股份
     */
    public BigDecimal getStockShare() {
        return stockShare;
    }

    /**
     * 设置所占股份
     *
     * @param stockShare 所占股份
     */
    public void setStockShare(BigDecimal stockShare) {
        this.stockShare = stockShare;
    }

    /**
     * 获取投资到账时间
     *
     * @return accounting_date - 投资到账时间
     */
    public Date getAccountingDate() {
        return accountingDate;
    }

    /**
     * 设置投资到账时间
     *
     * @param accountingDate 投资到账时间
     */
    public void setAccountingDate(Date accountingDate) {
        this.accountingDate = accountingDate;
    }

    /**
     * 获取删除标志:0代表有效；1代表无效
     *
     * @return yn - 删除标志:0代表有效；1代表无效
     */
    public Integer getYn() {
        return yn;
    }

    /**
     * 设置删除标志:0代表有效；1代表无效
     *
     * @param yn 删除标志:0代表有效；1代表无效
     */
    public void setYn(Integer yn) {
        this.yn = yn;
    }

	@Override
	public String toString() {
		return "InvestmentInstitutionsProject [projectId=" + projectId + ", investmentInstitutionsId="
				+ investmentInstitutionsId + ", investmentDesc=" + investmentDesc + ", status=" + status
				+ ", investmentAmount=" + investmentAmount + ", currency=" + currency + ", stockShare=" + stockShare
				+ ", accountingDate=" + accountingDate + ", yn=" + yn + ", investmentShortName=" + investmentShortName
				+ ", accountingDateStr=" + accountingDateStr + ", accountingDateOutputStr="
				+ accountingDateOutputStr + "]";
	}

	
}