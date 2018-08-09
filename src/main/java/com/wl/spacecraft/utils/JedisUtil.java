package com.wl.spacecraft.utils;

public class JedisUtil {
    /**
     * 用户msg的存储方式
     * @param phonenum 手机号
     * @return msg存储key
     */
    public static String formatMsgKey(String phonenum){
        return "user_"+phonenum+"_msg";
    }

    /**
     * 用户token的存储方式
     * @param phonenum 手机号
     * @return token存储key
     */
    public static String formatTokenKey(String phonenum){
        return "user_"+phonenum+"_token";
    }

    public static String parseMsgKey(String msgKey){
        return msgKey.substring(msgKey.indexOf("_")+1,msgKey.lastIndexOf("_"));
    }
    public static String parseTokenKey(String tokenKey){
        return tokenKey.substring(tokenKey.indexOf("_")+1,tokenKey.lastIndexOf("_"));
    }

    public static void main(String[] args) {

        String s = formatMsgKey("124");
        String s1 = formatTokenKey("132");

        String s2 = parseMsgKey("user_124_msg");
        String s3 = parseTokenKey("user_132_token");

        System.err.println(s);
        System.err.println(s1);
        System.err.println(s2);
        System.err.println(s3);


    }

}
