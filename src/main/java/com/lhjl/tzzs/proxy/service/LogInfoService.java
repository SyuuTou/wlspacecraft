package com.lhjl.tzzs.proxy.service;

import com.lhjl.tzzs.proxy.dto.CommonDto;

import java.util.Map;

public interface LogInfoService {
    /**
     * 记录用户操作记录的信息
     * @param token 用户token
     * @param data 对应数据
     * @param sceneKey 场景
     * @return
     */
    CommonDto<String> saveUserInfo(String token,String data,String sceneKey);

    /**
     * 记录精选活动操作记录接口
     * @param body
     * @return
     */
    CommonDto<String> saveElegantServiceLog(Map<String,Object> body);
}
