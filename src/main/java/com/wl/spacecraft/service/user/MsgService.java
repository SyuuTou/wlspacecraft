package com.wl.spacecraft.service.user;


import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.SendMsgOutputDto;

public interface MsgService {
    /**
     * 发送短信
     * @param phone 电话号码
     * @return
     */
    CommonDto<SendMsgOutputDto> senMsg(String phone) throws Exception;


}
