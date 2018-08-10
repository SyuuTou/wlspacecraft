package com.lhjl;

//注意要使用以下JSONObject包

import com.alibaba.fastjson.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wl.spacecraft.utils.httpclient.HttpClientUtil;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import com.lhjl.Person;


public class HttpClientTest extends TestCase {

    @Test
    public void testJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", "13691066251");
        map.put("msgCode", "4343434");

        /**
         * 将普通对象转换为json字符串,然后再转换成json对象
         */
        String mapJsonStr = new Gson().toJson(map);
        System.err.println(mapJsonStr);
        //将json字符串转换为json对象
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(mapJsonStr);
        System.err.println(jsonObject.get("phone"));
        /**
         * 直接将普通对象转换为json对象
         */
        net.sf.json.JSONObject jsonObject1 = net.sf.json.JSONObject.fromObject(map);


        /**
         * 将json对象转换为map
         */
        Map jsonObjectToMap = jsonObject;
        System.err.println(jsonObjectToMap+"--"+jsonObject.getClass());

        /**
         * 将Json对象转换为字符串
         */
        String jsonObjectToString = new Gson().toJson(jsonObject);
        System.err.println(jsonObjectToString);

    }

    @Test
    public void testMetaApp() {
        String s = HttpClientUtil.doGet("http://120.79.143.55:9090/meta/app");
        System.err.println(s);
    }

    @Test
    //登录
    public void testLogin() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", "13691066251");
        map.put("msgCode", "728099");
        map.put("msgValidateStr", "bb3ac9070ab2035fa4b6c2e303632d90");
        map.put("expire", 2032516011308L);

        String jsonStr = new Gson().toJson(map);
        System.err.println(jsonStr);

        String s = HttpClientUtil.doPost("http://120.79.143.55:9090/login", jsonStr);
        System.err.println(s);
    }

    @Test
    //排行
    public void testOgRank() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", 1);
        map.put("pageSize", 10);
        map.put("communityId", 1);
        map.put("groupId", 1);

        String jsonStr = new Gson().toJson(map);
        System.err.println(jsonStr);

        String s = HttpClientUtil.doPost("http://120.79.143.55:9090/og/rank13691066251", jsonStr);
        System.err.println(s);
    }

    @Test
    //测试图片上传
    public void upload() throws Exception {

//        String jsonStr = new Gson().toJson();
        System.err.println();

//        String s = HttpClientUtil.doPost("http://120.79.143.55:9090/og/rank13691066251");
        System.err.println();
    }

}