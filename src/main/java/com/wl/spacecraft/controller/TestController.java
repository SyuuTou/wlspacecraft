package com.wl.spacecraft.controller;

import javax.annotation.Resource;

import com.wl.spacecraft.controller.common.GenericController;
import com.wl.spacecraft.service.common.JedisCommonService;
import com.wl.spacecraft.service.user.UserService;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;


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

    {
        name="qwer";
    }
    private String name="123";

//    private Jedis jedis= jedisCommonService.getJedis();
{
    System.err.println("初始化块执行");
    System.err.println(userService);
    System.err.println(jedisCommonService);
    System.err.println(name);
}
    @GetMapping("test")
    public Object test( ){
        System.err.println(this.userService);;
        System.err.println(this.jedisCommonService);;
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
    @GetMapping("hash/redis")
    public Object hashRedis( ){
        System.err.println("redis--hash");
        jedisCommonService.getJedis().hset("dong","age","18");
        jedisCommonService.getJedis().hset("dong","name","zhoudong");
        jedisCommonService.getJedis().hset("dong","weight",String.valueOf(65));
         return jedisCommonService.getJedis().hget("dong","weight").getClass();

    }


}

