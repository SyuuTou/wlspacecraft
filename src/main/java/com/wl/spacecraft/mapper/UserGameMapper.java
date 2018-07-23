package com.wl.spacecraft.mapper;

import com.wl.spacecraft.dto.commondto.GameRankEntity;
import com.wl.spacecraft.dto.commondto.PagingInputDto;
import com.wl.spacecraft.model.UserGame;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserGameMapper extends OwnerMapper<UserGame> {
    /**
     * 获取今日已经获取的og总数，不需要传递token，因为一天内token可能改变
     * @param phone 用户手机
     * @param beginTime 此日零点
     * @param endTime 此日24点
     * @return 返回今日已获取og数量
     */
    Integer getLimite(@Param("phone")String phone, @Param("beginTime")Date beginTime, @Param("endTime") Date endTime);

    /**
     * 用户金币获得排行榜
     * @param body 请求参数
     * @return
     */
    List<GameRankEntity> gameRankList(PagingInputDto body);

    /**
     * 获取金币排行榜的人员总数
     * @return
     */
    Integer getRankTotal();

    /**
     * 获取游戏金币总共赠送总量
     * @return
     */
    Integer getOgRewardAmount();


}