package com.wl.spacecraft.utils.msg;

import org.apache.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信验证码
 * syuutou
 */
public class MsgSendUtil {

    private static String host = "https://feginesms.market.alicloudapi.com";
    private static String path = "/codeNotice";
    private static String method = "GET";
    private static String appcode = "01698074d91f482583cf4eff001752fd";

    /**
     * @param phone      用户手机号
     * @param verifyCode 短信验证码
     * @return
     */
    public static HttpResponse sendSms(String phone, String verifyCode) throws Exception {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> map = new HashMap<>();
        map.put("param", verifyCode);
        map.put("phone", phone);
        map.put("sign", "45862");
        map.put("skin", "34724");

        HttpResponse httpResponse = HttpUtils.doGet(host, path, method, headers, map);

        return httpResponse;
    }
}
