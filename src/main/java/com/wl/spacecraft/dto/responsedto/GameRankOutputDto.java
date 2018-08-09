package com.wl.spacecraft.dto.responsedto;

import com.wl.spacecraft.dto.commondto.GameRankEntity;
import com.wl.spacecraft.dto.commondto.PagingOutputDto;
import com.wl.spacecraft.model.Community;
import com.wl.spacecraft.model.CommunityGroup;


public class GameRankOutputDto {

    PagingOutputDto<GameRankEntity> rankList;

    /**
     * OG赠送总数
     *  存在社区id时表示本社区内赠送总量
     *  社区id不存在时表示所有赠送总量
     */
    private Integer ogRewardAmount;

    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 用户的当前排名
     *  存在社区id时候表示社区排名
     *  不存在社区id时表示世界排名
     */
    private Long myRank;
    /**
     * 我的排名说明
     */
    private String myRankNote;
    /**
     * 我的og总量
     */
    private Integer myOgAmount;

    /**
     * 我的社区信息
     *  只有存在社区id时会存在该对象
     */
    private Community myCommunity;
    /**
     * 我的群组信息
     *  只有存在群组id时会存在该对象
     */
    private CommunityGroup myGroup;

    public CommunityGroup getMyGroup() {
        return myGroup;
    }


    public void setMyGroup(CommunityGroup myGroup) {
        this.myGroup = myGroup;
    }

    public String getMyRankNote() {
        return myRankNote;
    }

    public void setMyRankNote(String myRankNote) {
        this.myRankNote = myRankNote;
    }

    public Community getMyCommunity() {
        return myCommunity;
    }

    public void setMyCommunity(Community myCommunity) {
        this.myCommunity = myCommunity;
    }

    public Long getMyRank() {
        return myRank;
    }

    public void setMyRank(Long myRank) {
        this.myRank = myRank;
    }

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
                ", myRank=" + myRank +
                ", myRankNote='" + myRankNote + '\'' +
                ", myOgAmount=" + myOgAmount +
                ", myCommunity=" + myCommunity +
                ", myGroup=" + myGroup +
                '}';
    }
}
