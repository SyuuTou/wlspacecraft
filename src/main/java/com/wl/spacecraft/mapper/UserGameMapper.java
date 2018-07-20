package com.wl.spacecraft.mapper;

import com.wl.spacecraft.dto.responsedto.UserInfoOutputDto;
import com.wl.spacecraft.model.UserGame;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface UserGameMapper extends OwnerMapper<UserGame> {
    /**
     * 获取用户信息
     * @param phone 用户手机
     * @param token 用户token
     * @param beginTime 此日零点
     * @param EndTime 此日24点
     * @return 返回今日已获取og数量
     */
    UserInfoOutputDto getUserInfo(@Param("phone")String phone, @Param("token") String token, @Param("beginTime")Date beginTime, @Param("endTime") Date endTime);
}