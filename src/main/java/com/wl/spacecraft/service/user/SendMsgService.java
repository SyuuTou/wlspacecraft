package com.wl.spacecraft.service.user;


import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.LoginInputInfoDto;
import com.wl.spacecraft.dto.responsedto.SendMsgOutputDto;

public interface SendMsgService {
    /**
     * 发送短信
     * @param phone 电话号码
     * @return
     */
    CommonDto<SendMsgOutputDto> senMsg(String phone);

    /**
     * 登录注册
     * @param loginInputInfoDto
     * @return
     */
    CommonDto login(LoginInputInfoDto loginInputInfoDto);
}
