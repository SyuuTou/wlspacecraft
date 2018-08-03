package com.wl.spacecraft.controller;

import javax.annotation.Resource;

import com.wl.spacecraft.controller.common.GenericController;
import com.wl.spacecraft.mapper.AppUserMapper;
import com.wl.spacecraft.mapper.MetaAppMapper;
import com.wl.spacecraft.mapper.UserGameMapper;
import com.wl.spacecraft.model.AppUser;
import com.wl.spacecraft.model.Community;
import com.wl.spacecraft.model.MetaApp;
import com.wl.spacecraft.model.UserGame;
import com.wl.spacecraft.service.common.JedisCommonService;
import com.wl.spacecraft.service.community.CommunityService;
import com.wl.spacecraft.service.user.UserService;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.List;


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
    @Resource
    private CommunityService communityService;
    @Resource
    private AppUserMapper appUserMapper;
    @Resource
    private UserGameMapper userGameMapper;
    @Resource
    private MetaAppMapper metaAppMapper;

    @GetMapping("test")
    public Object test( ){
        List<Community> communities = communityService.selectAll();
        List<AppUser> appUsers = appUserMapper.selectAll();
        List<UserGame> userGames = userGameMapper.selectAll();
        List<MetaApp> metaApps = metaAppMapper.selectAll();
        return metaApps;
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

