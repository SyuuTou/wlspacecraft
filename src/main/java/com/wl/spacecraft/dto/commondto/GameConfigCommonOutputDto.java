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
    private BigDecimal difficulty;

    /**
     * 1eth等于多少og
     */
    private Integer ethValue;

    public BigDecimal getStoneCreateSpeed() {
        return stoneCreateSpeed;
    }

    public BigDecimal getStoneMoveSpeed() {
        return stoneMoveSpeed;
    }

    public BigDecimal getOgCreateSpeed() {
        return ogCreateSpeed;
    }

    public BigDecimal getDifficulty() {
        return difficulty;
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

    public void setDifficulty(BigDecimal difficulty) {
        this.difficulty = difficulty;
    }

    public void setEthValue(Integer ethValue) {
        this.ethValue = ethValue;
    }

    @Override
    public String toString() {
        return "GameConfigOutputDto{" +
                "stoneCreateSpeed=" + stoneCreateSpeed +
                ", stoneMoveSpeed=" + stoneMoveSpeed +
                ", ogCreateSpeed=" + ogCreateSpeed +
                ", difficulty=" + difficulty +
                ", ethValue=" + ethValue +
                '}';
    }
}
