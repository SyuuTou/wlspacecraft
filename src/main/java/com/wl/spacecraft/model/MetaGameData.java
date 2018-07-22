package com.wl.spacecraft.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "meta_game_data")
public class MetaGameData {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 生成时间间隔 默认0.5 越小生成越快
     */
    @Column(name = "stone_create_speed")
    private BigDecimal stoneCreateSpeed;

    /**
     * 移动速率 默认5 越小移动速度越快
     */
    @Column(name = "stone_move_speed")
    private BigDecimal stoneMoveSpeed;

    /**
     * 生成时间间隔 默认5 越小 生成越快 score-币值 默认0.5
     */
    @Column(name = "og_create_speed")
    private BigDecimal ogCreateSpeed;

    /**
     * 游戏难度
     */
    private BigDecimal difficulty;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 1eth等于多少og 
     */
    @Column(name = "eth_value")
    private Integer ethValue;

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
     * 获取生成时间间隔 默认0.5 越小生成越快
     *
     * @return stone_create_speed - 生成时间间隔 默认0.5 越小生成越快
     */
    public BigDecimal getStoneCreateSpeed() {
        return stoneCreateSpeed;
    }

    /**
     * 设置生成时间间隔 默认0.5 越小生成越快
     *
     * @param stoneCreateSpeed 生成时间间隔 默认0.5 越小生成越快
     */
    public void setStoneCreateSpeed(BigDecimal stoneCreateSpeed) {
        this.stoneCreateSpeed = stoneCreateSpeed;
    }

    /**
     * 获取移动速率 默认5 越小移动速度越快
     *
     * @return stone_move_speed - 移动速率 默认5 越小移动速度越快
     */
    public BigDecimal getStoneMoveSpeed() {
        return stoneMoveSpeed;
    }

    /**
     * 设置移动速率 默认5 越小移动速度越快
     *
     * @param stoneMoveSpeed 移动速率 默认5 越小移动速度越快
     */
    public void setStoneMoveSpeed(BigDecimal stoneMoveSpeed) {
        this.stoneMoveSpeed = stoneMoveSpeed;
    }

    /**
     * 获取生成时间间隔 默认5 越小 生成越快 score-币值 默认0.5
     *
     * @return og_create_speed - 生成时间间隔 默认5 越小 生成越快 score-币值 默认0.5
     */
    public BigDecimal getOgCreateSpeed() {
        return ogCreateSpeed;
    }

    /**
     * 设置生成时间间隔 默认5 越小 生成越快 score-币值 默认0.5
     *
     * @param ogCreateSpeed 生成时间间隔 默认5 越小 生成越快 score-币值 默认0.5
     */
    public void setOgCreateSpeed(BigDecimal ogCreateSpeed) {
        this.ogCreateSpeed = ogCreateSpeed;
    }

    /**
     * 获取游戏难度
     *
     * @return difficulty - 游戏难度
     */
    public BigDecimal getDifficulty() {
        return difficulty;
    }

    /**
     * 设置游戏难度
     *
     * @param difficulty 游戏难度
     */
    public void setDifficulty(BigDecimal difficulty) {
        this.difficulty = difficulty;
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
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取1eth等于多少og 
     *
     * @return eth_value - 1eth等于多少og 
     */
    public Integer getEthValue() {
        return ethValue;
    }

    /**
     * 设置1eth等于多少og 
     *
     * @param ethValue 1eth等于多少og 
     */
    public void setEthValue(Integer ethValue) {
        this.ethValue = ethValue;
    }
}