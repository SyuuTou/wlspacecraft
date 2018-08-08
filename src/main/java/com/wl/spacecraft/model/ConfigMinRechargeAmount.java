package com.wl.spacecraft.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "config_min_recharge_amount")
public class ConfigMinRechargeAmount {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 最小充值金额
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 运营人员id
     */
    private Integer creator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    @Override
    public String toString() {
        return "ConfigMinRechargeAmount{" +
                "id=" + id +
                ", amount=" + amount +
                ", createTime=" + createTime +
                ", creator=" + creator +
                '}';
    }
}
