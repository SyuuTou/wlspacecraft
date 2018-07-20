package com.wl.spacecraft.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "app_intergral")
public class AppIntergral {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 钱包地址
     */
    private String wallet;

    /**
     * 1充币
            2提币
     */
    private Integer type;

    /**
     * og币更改数量
     */
    @Column(name = "integral_change")
    private String integralChange;

    /**
     * 备注
     */
    private String note;

    /**
     * 1飞船游戏
     */
    private Integer system;

    /**
     * 1待审核
2已审核
     */
    @Column(name = "audit_status")
    private Integer auditStatus;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private Date auditTime;

    /**
     * 获取记录id
     *
     * @return id - 记录id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置记录id
     *
     * @param id 记录id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户id
     *
     * @return userid - 用户id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置用户id
     *
     * @param userid 用户id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取钱包地址
     *
     * @return wallet - 钱包地址
     */
    public String getWallet() {
        return wallet;
    }

    /**
     * 设置钱包地址
     *
     * @param wallet 钱包地址
     */
    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    /**
     * 获取1充币
            2提币
     *
     * @return type - 1充币
            2提币
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置1充币
            2提币
     *
     * @param type 1充币
            2提币
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取og币更改数量
     *
     * @return integral_change - og币更改数量
     */
    public String getIntegralChange() {
        return integralChange;
    }

    /**
     * 设置og币更改数量
     *
     * @param integralChange og币更改数量
     */
    public void setIntegralChange(String integralChange) {
        this.integralChange = integralChange;
    }

    /**
     * 获取备注
     *
     * @return note - 备注
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置备注
     *
     * @param note 备注
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 获取1飞船游戏
     *
     * @return system - 1飞船游戏
     */
    public Integer getSystem() {
        return system;
    }

    /**
     * 设置1飞船游戏
     *
     * @param system 1飞船游戏
     */
    public void setSystem(Integer system) {
        this.system = system;
    }

    /**
     * 获取1待审核
2已审核
     *
     * @return audit_status - 1待审核
2已审核
     */
    public Integer getAuditStatus() {
        return auditStatus;
    }

    /**
     * 设置1待审核
2已审核
     *
     * @param auditStatus 1待审核
2已审核
     */
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
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
     * 获取审核时间
     *
     * @return audit_time - 审核时间
     */
    public Date getAuditTime() {
        return auditTime;
    }

    /**
     * 设置审核时间
     *
     * @param auditTime 审核时间
     */
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }
}