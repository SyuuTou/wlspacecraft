package com.wl.spacecraft.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lhjl.tzzs.proxy.service.GenericService;
import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.LoginInputInfoDto;
import com.wl.spacecraft.dto.responsedto.SendMsgOutputDto;
import com.wl.spacecraft.service.user.SendMsgService;
import com.wl.spacecraft.utils.HttpUtils;
import com.wl.spacecraft.utils.MD5Util;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SendMsgImpl extends GenericService implements SendMsgService {
    private static final String KEY = "SPACECRAFT"; // 自定义秘钥

    @Override
    public CommonDto<SendMsgOutputDto> senMsg(String phone) {
        SendMsgOutputDto sendMsgOutputDto = new SendMsgOutputDto();
        CommonDto<SendMsgOutputDto> result = new CommonDto<SendMsgOutputDto>();
        Map<String, String> map = new HashMap<String, String>();
        String host = "http://smsbanling.market.alicloudapi.com";
        String path = "/smsapis";
        String method = "GET";
        String appcode = "5022e749e4514bac907b6c3001adf5c9";
        String verifyCode = String.valueOf(new Random().nextInt(899999)+10000);//随机六位数字验证码
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        map.put("msg","亲爱的，您好，您的验证码是："+verifyCode+",请妥善保管，丢失那天就是分手之时！");//验证码
        map.put("mobile",phone);//手机号码
        map.put("sign","号外号外");//签名
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE,5);//设置短信有效时间为5分钟
            String deadTime = sdf.format(calendar.getTime());//生成验证码5分钟后，用户校验是否失效
            String verification = MD5Util.md5Encode(KEY + "@" + deadTime + "@" + verifyCode,"");//生成MD5值认证标识
            sendMsgOutputDto.setDeadTime(deadTime);
            sendMsgOutputDto.setVerification(verification);
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, map);
            String jsonString = EntityUtils.toString(response.getEntity());
            if (!"".equals(jsonString) && jsonString != null){
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                String str = String.valueOf(jsonObject.get("result"));
                if ("1024".equals(str)){
                    result.setMessage("手机号1小时频率限制");//限制发送
                    sendMsgOutputDto.setState("1");
                }else if ("0".equals(str)){
                    result.setMessage("success");//发送成功
                    sendMsgOutputDto.setState("0");
                }else if ("1014".equals(str)){
                    result.setMessage("fail");//发送失败
                   sendMsgOutputDto.setState("1");
                }else {
                    result.setMessage("fail");//发送失败
                    sendMsgOutputDto.setState("1");
                }
            }else {
                result.setMessage("短信已欠费,请联系管理员");//发送失败
                sendMsgOutputDto.setState("1");
            }
            result.setStatus(200);
        } catch (Exception e) {
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());
            sendMsgOutputDto.setState("1");
            result.setMessage("fail");
            result.setStatus(500);
        }
        result.setData(sendMsgOutputDto);
        return result;
    }

    @Override
    public CommonDto login(LoginInputInfoDto loginInputInfoDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String currentTime = sdf.format(calendar.getTime());//获取当前时间
        String deadTime = loginInputInfoDto.getDeadTime();//获取验证码失效时间
        String verification = loginInputInfoDto.getVerification();//获取认证标识
        String verifyCode = loginInputInfoDto.getVerifyCode();//获取用户填写的验证码
        String newVerification = MD5Util.md5Encode(KEY + "@" + deadTime + "@" + verifyCode,"");//根据用户填写的验证码生成MD5值新的认证标识
        if (deadTime.compareTo(currentTime)>0) {//没有超时
            if (verification.equals(newVerification)) {//两次认证相同，则验证成功
                //TODO
                //判断用户是否存在，不存在新增，存在更新token和登录时间
                // 使用uuid作为源token，生成token值
                String token = UUID.randomUUID().toString().replace("-", "");
                String tokenSecret = MD5Util.md5Encode(KEY + "@" + token + "@" + loginInputInfoDto.getPhone(),"");//生成MD5值认证标识

            }else {
                //验证失败
            }
        }else {
            //验证码超时，重新获取
        }

        return null;
    }

    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String ccurrentTime = sdf.format(calendar.getTime());//获取当前时间

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE,1);
        String deadTime = sdf.format(calendar1.getTime());

        System.out.println("当前时间"+ccurrentTime);
        System.out.println("失效时间"+deadTime);
        if (deadTime.compareTo(ccurrentTime)<0) {
            System.out.println("过期");

        }else {
            System.out.println("不过期");
        }

//        String token = UUID.randomUUID().toString().replace("-", "");
//        String tokenSecret = MD5Util.md5Encode(KEY + "@" + token + "@" + ccurrentTime,"");//生成MD5值认证标识
//        System.out.println(token);
//        System.out.println(tokenSecret);
    }
}
