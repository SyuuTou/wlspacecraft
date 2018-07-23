package com.wl.spacecraft.dto.responsedto;

import com.wl.spacecraft.dto.commondto.GameRankEntity;
import com.wl.spacecraft.dto.commondto.PagingOutputDto;


public class GameRankOutputDto {

    PagingOutputDto<GameRankEntity> rankList;

    /**
     * OG游戏赠送赠送总数
     */
    private Integer ogRewardAmount;

    public PagingOutputDto<GameRankEntity> getRankList() {
        return rankList;
    }

    public void setRankList(PagingOutputDto<GameRankEntity> rankList) {
        this.rankList = rankList;
    }

    public Integer getOgRewardAmount() {
        return ogRewardAmount;
    }

    public void setOgRewardAmount(Integer ogRewardAmount) {
        this.ogRewardAmount = ogRewardAmount;
    }

    @Override
    public String toString() {
        return "GameRankOutputDto{" +
                "rankList=" + rankList +
                ", ogRewardAmount=" + ogRewardAmount +
                '}';
    }
}
