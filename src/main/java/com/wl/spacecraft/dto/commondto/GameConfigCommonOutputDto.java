package com.wl.spacecraft.dto.commondto;

import java.math.BigDecimal;

public class GameConfigCommonOutputDto {

    /**
     * 生成时间间隔 默认0.5 越小生成越快
     */
    private BigDecimal stoneCreateSpeed;

    /**
     * 移动速率 默认5 越小移动速度越快
     */
    private BigDecimal stoneMoveSpeed;

    /**
     * 生成时间间隔 默认5 越小 生成越快 score-币值 默认0.5
     */
    private BigDecimal ogCreateSpeed;

    /**
     * 游戏难度
     */
    private Integer difficulty;

    /**
     * 1eth等于多少og
     */
    private Integer ethValue;
    /**
     * 基础价格
     */
    private BigDecimal currentBasePrice;
    /**
     * 当前奖金
     */
    private BigDecimal currentBonus;


    public BigDecimal getStoneCreateSpeed() {
        return stoneCreateSpeed;
    }

    public BigDecimal getStoneMoveSpeed() {
        return stoneMoveSpeed;
    }

    public BigDecimal getOgCreateSpeed() {
        return ogCreateSpeed;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public BigDecimal getCurrentBasePrice() {
        return currentBasePrice;
    }

    public void setCurrentBasePrice(BigDecimal currentBasePrice) {
        this.currentBasePrice = currentBasePrice;
    }

    public BigDecimal getCurrentBonus() {
        return currentBonus;
    }

    public void setCurrentBonus(BigDecimal currentBonus) {
        this.currentBonus = currentBonus;
    }

    public Integer getEthValue() {
        return ethValue;
    }

    public void setStoneCreateSpeed(BigDecimal stoneCreateSpeed) {
        this.stoneCreateSpeed = stoneCreateSpeed;
    }

    public void setStoneMoveSpeed(BigDecimal stoneMoveSpeed) {
        this.stoneMoveSpeed = stoneMoveSpeed;
    }

    public void setOgCreateSpeed(BigDecimal ogCreateSpeed) {
        this.ogCreateSpeed = ogCreateSpeed;
    }


    public void setEthValue(Integer ethValue) {
        this.ethValue = ethValue;
    }

    @Override
    public String toString() {
        return "GameConfigCommonOutputDto{" +
                "stoneCreateSpeed=" + stoneCreateSpeed +
                ", stoneMoveSpeed=" + stoneMoveSpeed +
                ", ogCreateSpeed=" + ogCreateSpeed +
                ", difficulty=" + difficulty +
                ", ethValue=" + ethValue +
                ", currentBasePrice=" + currentBasePrice +
                ", currentBonus=" + currentBonus +
                '}';
    }
}
