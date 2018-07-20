package com.wl.spacecraft.service.user;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.UserInfoOutputDto;
import com.wl.spacecraft.model.AppIntergral;

public interface UserService {
    /**
     * 返回用户大厅界面 的基本信息
     * @param phone 用户手机号
     * @param token 用户token
     * @return
     */
    CommonDto<UserInfoOutputDto> getUserInfo(String phone,String token);

}
