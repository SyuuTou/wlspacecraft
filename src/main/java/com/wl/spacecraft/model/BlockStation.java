package com.wl.spacecraft.model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "block_station")
public class BlockStation {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "userid")
    private Integer userid;
    /**
     * 用户钱包地址
     */
    @Column(name = "wallet")
    private String wallet;
    /**
     * 交易号，区块站
     */
    @Column(name = "txhash")
    private String txhash;
    /**
     * 交易类型:充提币类型，1充币，2提币
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 交易金额
     */
    @Column(name = "og_change")
    private BigDecimal ogChange;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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

    public BigDecimal getOgChange() {
        return ogChange;
    }

    public void setOgChange(BigDecimal ogChange) {
        this.ogChange = ogChange;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getTxhash() {
        return txhash;
    }

    public void setTxhash(String txhash) {
        this.txhash = txhash;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "BlockStation{" +
                "id=" + id +
                ", userid=" + userid +
                ", wallet='" + wallet + '\'' +
                ", txhash='" + txhash + '\'' +
                ", type=" + type +
                ", ogChange=" + ogChange +
                ", createTime=" + createTime +
                ", creator=" + creator +
                ", delFlag=" + delFlag +
                '}';
    }
}
