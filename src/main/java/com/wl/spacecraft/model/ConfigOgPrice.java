package com.wl.spacecraft.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "config_og_price")
public class ConfigOgPrice {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 当前OG基础价格
     */
    @Column(name = "current_base_price")
    private BigDecimal currentBasePrice;

    /**
     * 当前奖金
     */
    @Column(name = "current_bonus")
    private BigDecimal currentBonus;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建者
     */
    private Integer creator;

    /**
     * 游戏类型（后期兼容，1表示飞船游戏，2...）
     */
    private Integer type;

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
     * 获取当前OG基础价格
     *
     * @return current_base_price - 当前OG基础价格
     */
    public BigDecimal getCurrentBasePrice() {
        return currentBasePrice;
    }

    /**
     * 设置当前OG基础价格
     *
     * @param currentBasePrice 当前OG基础价格
     */
    public void setCurrentBasePrice(BigDecimal currentBasePrice) {
        this.currentBasePrice = currentBasePrice;
    }

    /**
     * 获取当前奖金
     *
     * @return current_bonus - 当前奖金
     */
    public BigDecimal getCurrentBonus() {
        return currentBonus;
    }

    /**
     * 设置当前奖金
     *
     * @param currentBonus 当前奖金
     */
    public void setCurrentBonus(BigDecimal currentBonus) {
        this.currentBonus = currentBonus;
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
     * 获取创建者
     *
     * @return creator - 创建者
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * 设置创建者
     *
     * @param creator 创建者
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取游戏类型（后期兼容，1表示飞船游戏，2...）
     *
     * @return type - 游戏类型（后期兼容，1表示飞船游戏，2...）
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置游戏类型（后期兼容，1表示飞船游戏，2...）
     *
     * @param type 游戏类型（后期兼容，1表示飞船游戏，2...）
     */
    public void setType(Integer type) {
        this.type = type;
    }
}