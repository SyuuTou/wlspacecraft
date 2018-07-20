package com.wl.spacecraft.controller;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.UserInfoOutputDto;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController extends GenericService {

    @Resource
    UserService userService;

    /**
     * 用户登录成功之后，返回用户的信息
     * @param phone
     * @param token
     * @return
     */
    @GetMapping("user/info")
    public CommonDto<UserInfoOutputDto> getUserInfo(@RequestParam("phone") String phone,@RequestParam("token") String token){
        System.err.println(phone+"****"+token);

        CommonDto<UserInfoOutputDto> result=new CommonDto<UserInfoOutputDto>();
        try{
            result=userService.getUserInfo(phone,token);
        }catch(Exception e){
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());

            result.setData(null);
            result.setMessage("fail");
            result.setStatus(500);
        }
        return result;
    }


}
