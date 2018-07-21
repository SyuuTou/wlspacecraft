package com.wl.spacecraft.controller;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.LoginInputInfoDto;
import com.wl.spacecraft.dto.responsedto.SendMsgOutputDto;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.user.SendMsgService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 短信发送
 */
@RestController
public class SendMsgController extends GenericService {

    @Resource
    SendMsgService sendMsgService;

    /**
     * 短信发送
     * @param phone
     * @return
     */
    @GetMapping("msg1/info")
    public CommonDto<SendMsgOutputDto> sendMsg(String phone){
        CommonDto<SendMsgOutputDto> result = sendMsgService.senMsg(phone);
        return result;
    }

    /**
     * 登录注册
     * @param loginInputInfoDto
     * @return
     */
    @PostMapping("login/info")
    public CommonDto login(@RequestBody LoginInputInfoDto loginInputInfoDto){
        CommonDto result = new CommonDto();
        try {
            result = sendMsgService.login(loginInputInfoDto);
        }catch (Exception e){
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());
            result.setData(null);
            result.setMessage("fail");
            result.setStatus(500);
        }
        return result;
    }
}
