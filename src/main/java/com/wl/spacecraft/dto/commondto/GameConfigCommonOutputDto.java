package com.wl.spacecraft.dto.commondto;

import java.math.BigDecimal;

public class GameConfigCommonOutputDto {

    /**
     * 生成时间间隔 默认0.5 越小生成越快
     */
    //Deprecated
//    private BigDecimal stoneCreateSpeed;

    /**
     * 移动速率 默认5 越小移动速度越快
     */
    //Deprecated
//    private BigDecimal stoneMoveSpeed;

    /**
     * 生成时间间隔 默认5 越小 生成越快 score-币值 默认0.5
     */
    //Deprecated
//    private BigDecimal ogCreateSpeed;

    /**
     * 游戏难度
     */
    private Integer difficulty;

//    /**
//     * 基础价格
//     */
//    private BigDecimal currentBasePrice;
//    /**
//     * 当前奖金
//     */
//    private BigDecimal currentBonus;



    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "GameConfigCommonOutputDto{" +
                "difficulty=" + difficulty +
                '}';
    }
}
