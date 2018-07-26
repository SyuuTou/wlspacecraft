package com.wl.spacecraft.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lhjl.tzzs.proxy.service.GenericService;
import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.SendMsgOutputDto;
import com.wl.spacecraft.service.user.MsgService;
import com.wl.spacecraft.utils.HttpUtils;
import com.wl.spacecraft.utils.MD5Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MsgServiceImpl extends GenericService implements MsgService {
    private static final String KEY = "SPACECRAFT"; // 自定义秘钥

    /**
     * 短信验证码失效时间
     */
    @Value("${msgCodeExpireTime}")
    private Integer msgCodeExpireTime;

    @Autowired
    private Environment env;

    @Override
    @Transactional
    public CommonDto<SendMsgOutputDto> senMsg(String phone) throws Exception {

        String host = env.getProperty("msghost");
        String path = env.getProperty("msgpath");
        String method = env.getProperty("method");
        String appcode = env.getProperty("appcode");


        CommonDto<SendMsgOutputDto> result = new CommonDto<>();
        SendMsgOutputDto sendMsgOutputDto = new SendMsgOutputDto();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);

        String verifyCode = RandomStringUtils.random(4,"0123456789");//四位随机数
        Map<String, String> map = new HashMap<>();

        map.put("param", verifyCode);
        map.put("phone", phone);
        map.put("sign", "45862");
        map.put("skin", "34724");

        System.err.println(map);
        System.err.println("短信验证码--˘>>>>"+verifyCode);

        Calendar calendar = Calendar.getInstance();
        //设置短信有效时间为20分钟
        calendar.add(Calendar.MINUTE,msgCodeExpireTime);
        Date expire = calendar.getTime();

        System.err.println("过期时间-->>>>>  "+expire.getTime());

        //生成MD5值认证标识
        String verification = MD5Util.md5Encode(KEY + "@" + verifyCode + "@" + expire.toString(),null);

        //短信发送
        HttpResponse response = HttpUtils.doGet(host, path, method, headers, map);
        String jsonString = EntityUtils.toString(response.getEntity());

        System.err.println("jsonString是否为空字符串--{"+jsonString.equals("")+"}");
        System.err.println("jsonString是否等于null--{"+jsonString == null +"}");

        if ( StringUtils.isNotBlank(jsonString) ){
            JSONObject jsonObject = JSONObject.parseObject(jsonString);

            System.err.println("**jsonObject**"+jsonObject);

            String str = String.valueOf(jsonObject.get("Code"));

            switch(str){
                case ("OK"):{
                    result.setMessage("success");//发送成功
                    result.setStatus(200);

                    sendMsgOutputDto.setState(true);
                    sendMsgOutputDto.setNote("短信发送成功");
                    //设置过期时间已经MD5校验码
                    sendMsgOutputDto.setExpire(expire);
                    sendMsgOutputDto.setMsgValidateStr(verification);
                } break;
                default : { //发送失败
                    result.setMessage("failed");
                    result.setStatus(500);

                    sendMsgOutputDto.setState(false);
                    sendMsgOutputDto.setNote("短信发送失败");
                }
            }
        }else {
            result.setMessage("failed");//发送失败
            result.setStatus(500);

            sendMsgOutputDto.setState(false);
            sendMsgOutputDto.setNote("短信服务中止使用，请联系客服确认");
        }

        result.setData(sendMsgOutputDto);
        return result;
    }

}
