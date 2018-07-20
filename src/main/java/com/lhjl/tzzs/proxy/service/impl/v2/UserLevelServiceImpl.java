package com.lhjl.tzzs.proxy.service.impl.v2;

import com.lhjl.tzzs.proxy.dto.ActionDto;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.InterviewInputDto;
import com.lhjl.tzzs.proxy.dto.UserLevelDto;
import com.lhjl.tzzs.proxy.service.UserLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("userLevelV2Service")
public class UserLevelServiceImpl  implements UserLevelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLevelService.class);

    @Override
    public CommonDto<List<UserLevelDto>> findUserLevelList(Integer appId, String userId) {
        return null;
    }

    @Override
    public CommonDto<UserLevelDto> findLevelInfo(String userStr, int levelId, Integer appId) {
        return null;
    }

    @Override
    public CommonDto<Map<String, Object>> upLevel(Integer id, String userStr, int levelId, String presentedType, Integer appId) {
        return null;
    }

    @Override
    public CommonDto<Map<String, Object>> consumeTips(ActionDto action, Integer appId) {
        return null;
    }

    @Override
    public CommonDto<Map<String, Object>> consume(ActionDto action, Integer appId) {
        return null;
    }

    @Override
    public CommonDto<Map<String, Object>> cancel(String userId, String sceneKey, Integer appId) {
        return null;
    }

    @Override
    public CommonDto<String> changeLevel(Integer userId, int status, Integer appId) {
        return null;
    }

    @Override
    public CommonDto<Map<String, Object>> getUserLevel(String token, Integer appId) {
        return null;
    }

    @Override
    public CommonDto<Map<String, Object>> interviewTips(InterviewInputDto body, Integer appId) {
        return null;
    }

    @Override
    public CommonDto<Map<String, Object>> interviewCost(InterviewInputDto body, Integer appId) {
        return null;
    }
}
