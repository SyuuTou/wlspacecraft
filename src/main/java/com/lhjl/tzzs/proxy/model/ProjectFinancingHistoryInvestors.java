package com.lhjl.tzzs.proxy.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "project_financing_history_investors")
public class ProjectFinancingHistoryInvestors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 融资历史表id
     */
    @Column(name = "project_financing_history_id")
    private Integer projectFinancingHistoryId;

    /**
     * 投资人类型
     */
    @Column(name = "investor_name")
    private String investorName;

    /**
     * 股份占比
     */
    @Column(name = "share_ratio")
    private BigDecimal shareRatio;

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
     * 获取融资历史表id
     *
     * @return project_financing_history_id - 融资历史表id
     */
    public Integer getProjectFinancingHistoryId() {
        return projectFinancingHistoryId;
    }

    /**
     * 设置融资历史表id
     *
     * @param projectFinancingHistoryId 融资历史表id
     */
    public void setProjectFinancingHistoryId(Integer projectFinancingHistoryId) {
        this.projectFinancingHistoryId = projectFinancingHistoryId;
    }

    /**
     * 获取投资人类型
     *
     * @return investor_name - 投资人类型
     */
    public String getInvestorName() {
        return investorName;
    }

    /**
     * 设置投资人类型
     *
     * @param investorName 投资人类型
     */
    public void setInvestorName(String investorName) {
        this.investorName = investorName;
    }

    /**
     * 获取股份占比
     *
     * @return share_ratio - 股份占比
     */
    public BigDecimal getShareRatio() {
        return shareRatio;
    }

    /**
     * 设置股份占比
     *
     * @param shareRatio 股份占比
     */
    public void setShareRatio(BigDecimal shareRatio) {
        this.shareRatio = shareRatio;
    }
}