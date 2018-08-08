package com.wl.spacecraft.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wl.spacecraft.controller.common.GenericController;
import com.wl.spacecraft.mapper.*;
import com.wl.spacecraft.model.*;
import com.wl.spacecraft.service.common.JedisCommonService;
import com.wl.spacecraft.service.community.CommunityService;
import com.wl.spacecraft.service.user.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Resource
    private AppIntergralMapper appIntergralMapper;
    @Resource
    private BlockStationMapper blockStationMapper;


    @GetMapping("test")
    public Object test(){
        List<Community> communities = communityService.selectAllOrderBySort();
        List<AppUser> appUsers = appUserMapper.selectAll();
        List<UserGame> userGames = userGameMapper.selectAll();
        List<MetaApp> metaApps = metaAppMapper.selectAll();
        List<AppIntergral> appIntergrals = appIntergralMapper.selectAll();
        List<BlockStation> blockStations = blockStationMapper.selectAll();

        AppUser appUser = new AppUser();
        appUser.setCommunityId(null);
        List<AppUser> select = appUserMapper.select(appUser);


        Object test = userService.test();
        return blockStations;
    }

    @GetMapping("test2")
    public Object test(HttpServletRequest request, HttpServletResponse response){
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        HttpServletRequest request2 = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("ContentType",request.getContentType());
        map.put("getContentLengthLong",request.getContentLengthLong());

        map.put("getLocale",request.getLocale());
        map.put("getLocalAddr",request.getLocalAddr());

        map.put("getServerName",request.getServerName());
        map.put("getServerPort",request.getServerPort());

        map.put("getRequestURL",request.getRequestURL());
//        map.put("getHeaderNames",request.getHeaderNames());

        map.put("ContextPath",request.getContextPath());
        map.put("SessionId",request.getSession().getId());
        map.put("ApplicationRealPath",request.getServletContext().getRealPath("/"));

        map.put("getRemoteUser",request.getRemoteUser());
        map.put("getRemoteAddr",request.getRemoteAddr());
        map.put("getRemotePort",request.getRemotePort());
        map.put("getRemoteHost",request.getRemoteHost());

        return map;
    }

    /**
     * 测试事务
     * @return
     */
    @GetMapping("test/tran")
    public Object testTran( ){
        try{
            userService.test();
        }catch(Exception e){

        }
        return null;
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

