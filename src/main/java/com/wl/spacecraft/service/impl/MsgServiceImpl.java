package com.wl.spacecraft.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lhjl.tzzs.proxy.service.GenericService;
import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.SendMsgOutputDto;
import com.wl.spacecraft.service.user.MsgService;
import com.wl.spacecraft.utils.HttpUtils;
import com.wl.spacecraft.utils.MD5Util;
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

        String host = env.getProperty("host");
        String path = env.getProperty("path");
        String method = env.getProperty("method");
        String appcode = env.getProperty("appcode");


        CommonDto<SendMsgOutputDto> result = new CommonDto<>();
        SendMsgOutputDto sendMsgOutputDto = new SendMsgOutputDto();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);

        String verifyCode = String.valueOf(new Random().nextInt(899999)+10000);//随机六位数字验证码
        Map<String, String> map = new HashMap<>();
        map.put("sign","Only Game");//签名
        map.put("msg","验证码为："+verifyCode+",请妥善保管，丢失那天就是分手之时！"+msgCodeExpireTime+"分钟内有效");//验证码
        map.put("mobile",phone);//手机号码

        System.err.println("短信验证码--˘>>>>"+verifyCode);

        Calendar calendar = Calendar.getInstance();
        //设置短信有效时间为5分钟
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
            String str = String.valueOf(jsonObject.get("result"));

            System.err.println("**jsonObject**"+jsonObject);

            switch(str){
                case ("0"):{
                    result.setMessage("success");//发送成功
                    result.setStatus(200);

                    sendMsgOutputDto.setState(true);
                    sendMsgOutputDto.setNote("短信发送成功");
                    //设置过期时间已经MD5校验码
                    sendMsgOutputDto.setExpire(expire);
                    sendMsgOutputDto.setMsgValidateStr(verification);
                } break;
                case ("1024"):{
                    result.setMessage("failed");//限制发送
                    result.setStatus(500);

                    sendMsgOutputDto.setState(false);
                    sendMsgOutputDto.setNote("手机号1小时频率限制");
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
