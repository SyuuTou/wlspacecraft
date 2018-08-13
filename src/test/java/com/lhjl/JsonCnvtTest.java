package com.lhjl;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JsonCnvtTest {
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
}
