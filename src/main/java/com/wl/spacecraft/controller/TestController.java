package com.wl.spacecraft.controller;

import javax.annotation.Resource;

import com.wl.spacecraft.controller.common.GenericController;
import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.requestdto.CoinToAccountInputDto;
import com.wl.spacecraft.dto.responsedto.CoinToAccountOutputDto;
import com.wl.spacecraft.exception.project.ProjectException;
import com.wl.spacecraft.service.user.UserService;
import org.springframework.web.bind.annotation.*;




/**
 * just for test , no other values
 * @author syuutou
 *
 */
@RestController
public class TestController extends GenericController {
    @Resource
    private UserService userService;

    @GetMapping("test")
    public Object test( ){

//        return userService.test();
        return "qwe";
    }


}

