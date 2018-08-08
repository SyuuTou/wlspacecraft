package com.wl.spacecraft.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lhjl.tzzs.proxy.service.GenericService;
import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.SendMsgOutputDto;
import com.wl.spacecraft.exception.common.DataFormatException;
import com.wl.spacecraft.exception.login.UserNotExistException;
import com.wl.spacecraft.service.community.CommunityService;
import com.wl.spacecraft.service.user.MsgService;
import com.wl.spacecraft.service.user.UserService;
import com.wl.spacecraft.utils.MD5Util;
import com.wl.spacecraft.utils.MsgSendUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource
    private CommunityService communityService;

    @Resource
    private UserService userService;

    @Override
    @Transactional
    public CommonDto<SendMsgOutputDto> senMsg(String phone) throws Exception {

        CommonDto<SendMsgOutputDto> result = new CommonDto<>();
        SendMsgOutputDto sendMsgOutputDto = new SendMsgOutputDto();

        String verifyCode = RandomStringUtils.random(4,"0123456789");//四位随机数
        System.err.println("短信验证码--˘>>>>"+verifyCode);

        Calendar calendar = Calendar.getInstance();
        //设置短信有效时间为20分钟
        calendar.add(Calendar.MINUTE,msgCodeExpireTime);
        Date expire = calendar.getTime();
        System.err.println("过期时间-->>>>>  "+expire.getTime());

        //生成MD5值认证标识
        String verification = MD5Util.md5Encode(KEY + "@" + verifyCode + "@" + expire.toString(),null);
        System.err.println("生成的短信校验码："+verification);

        //短信发送
        HttpResponse httpResponse = MsgSendUtil.sendSms(phone, verifyCode);
        String jsonString = EntityUtils.toString(httpResponse.getEntity());

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

                    System.err.println("验证码发送成功之后返回体："+sendMsgOutputDto);
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
