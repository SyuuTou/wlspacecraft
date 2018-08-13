package com.lhjl;

//注意要使用以下JSONObject包

import com.alibaba.fastjson.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wl.spacecraft.utils.httpclient.HttpClientUtil;
import junit.framework.TestCase;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.lhjl.Person;
import sun.security.jgss.HttpCaller;


public class HttpClientTest extends TestCase {

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
    public void testUpload() throws Exception {
        //TODO 实现图片上传
        String url="http://localhost:9090/upload/image/sample";
        String filePath="/Users/syuutousan/Downloads/icon/aixin.png";

        HttpClientUtil.upload(url,filePath);

        System.err.println();
    }
    @Test
    public void testUpload2() {
        String url = "http://localhost:8081/lieni-app-inf-oa/resume/background/education/verify/sync";
        String fileUrl = "/Users/syuutousan/Downloads/icon/aixin.png";
        Map<String, String> map = new HashMap<String, String>();
        map.put("operatorJobNumber", "102800038");
        map.put("operatorEmail", "pengyuqian@aimsen.com1");
        map.put("realed", "1");
        map.put("resumeEducationBackgroundId", "6045");
        try {
            System.out.println(HttpClientUtil.post(url, "degreeIcon", new File(fileUrl), map));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testTest() throws Exception {
        String s = HttpClientUtil.doGet("http://localhost:9090/test");
        System.err.println(s);
    }

}