package com.wl.spacecraft.service.impl;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.UserInfoOutputDto;
import com.wl.spacecraft.mapper.AppUserMapper;
import com.wl.spacecraft.mapper.UserGameMapper;
import com.wl.spacecraft.model.AppUser;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.user.UserService;
import com.wl.spacecraft.utils.DateUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends GenericService implements UserService {

    @Autowired
    AppUserMapper appUserMapper;
    @Autowired
    UserGameMapper userGameMapper;

    /**
     * 确认token的有效性
     * @param token
     * @return
     */
    //TODO 用户token信息的校验
    private boolean validateToken(String token){
        return true;
    }

    @Override
    public CommonDto<UserInfoOutputDto> getUserInfo(String phone ,String token) {
        //TODO 是否进行用户的身份合法性校验


        CommonDto<UserInfoOutputDto> result=new CommonDto<UserInfoOutputDto>();

        UserInfoOutputDto userInfo = userGameMapper.getUserInfo(phone, token, DateUtils.getStartTime(), DateUtils.getEndTime());

        result.setData(userInfo);
        result.setMessage("用户信息查询成功");
        result.setStatus(200);

        return result;
    }
}
