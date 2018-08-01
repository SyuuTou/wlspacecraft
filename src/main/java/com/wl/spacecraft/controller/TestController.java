package com.wl.spacecraft.controller;

import javax.annotation.Resource;

import com.wl.spacecraft.controller.common.GenericController;
import com.wl.spacecraft.service.common.JedisCommonService;
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
    @Resource
    private JedisCommonService jedisCommonService;

    @GetMapping("test")
    public Object test( ){

        return userService.test();
    }

    @GetMapping("set/redis")
    public Object setRedis( ){
        jedisCommonService.getJedis().setex("zhou",10,"dong");
        return null;
    }
    @GetMapping("get/redis")
    public Object getRedis( ){
        System.err.println(jedisCommonService.getJedis().get("zhou")==null);
        return jedisCommonService.getJedis().get("zhou");

    }


}

