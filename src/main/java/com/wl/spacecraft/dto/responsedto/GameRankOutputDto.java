package com.wl.spacecraft.dto.responsedto;

import com.wl.spacecraft.dto.commondto.GameRankEntity;
import com.wl.spacecraft.dto.commondto.PagingOutputDto;


public class GameRankOutputDto {

    PagingOutputDto<GameRankEntity> rankList;

    /**
     * OG赠送总数
     */
    private Integer ogRewardAmount;

    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 我的og总量
     */
    private Integer myOgAmount;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getMyOgAmount() {
        return myOgAmount;
    }

    public void setMyOgAmount(Integer myOgAmount) {
        this.myOgAmount = myOgAmount;
    }

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
                ", phone='" + phone + '\'' +
                ", myOgAmount=" + myOgAmount +
                '}';
    }
}
