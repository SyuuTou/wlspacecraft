package com.wl.spacecraft.controller.util;

import com.wl.spacecraft.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UtilController {

    @Resource
    private UserService userService;

    /**
     *
     * @return
     */
    @GetMapping("virture/user/del")
    public Object getGameConfig(){

        try{
            userService.delVirtualUser();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
