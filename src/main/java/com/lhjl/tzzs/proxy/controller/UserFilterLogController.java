package com.lhjl.tzzs.proxy.controller;

import com.lhjl.tzzs.proxy.mapper.UserFilterLogMapper;
import com.lhjl.tzzs.proxy.model.UserFilterLog;
import com.lhjl.tzzs.proxy.service.UserFilterLogService;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.lhjl.tzzs.proxy.dto.CommonDto;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class UserFilterLogController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserFilterLogController.class);

    @Resource
    private UserFilterLogService userFilterLogService;

    /**
     * 创建筛选记录接口
     * @param body
     * @return
     */
    @PostMapping("user/filter/clog")
    public CommonDto<String> userFilterClog(@RequestBody Map<String,String> body){
        CommonDto<String> result = new CommonDto<String>();
        String investment_institutions_type = body.get("investment_institutions_type");
        String investment_institutions_field = body.get("investment_institutions_field");
        String financing_stage = body.get("financing_stage");
        String city = body.get("city");
        String education = body.get("education");
        String work = body.get("work");
        String user_id = body.get("user_id");
        String financingYear = body.get("financingYear");

        try {
            result = userFilterLogService.userFilterAddLog( investment_institutions_type, investment_institutions_field, financing_stage, city, education, work, user_id,financingYear);

        }catch (Exception e){
            result.setMessage(e.getMessage());
            result.setStatus(501);
            result.setData(null);
            log.error(e.getMessage(),e.fillInStackTrace());
        }

        return result;

    }


    /**
     * 读取上次筛选结果接口
     * @param id
     * @return
     */
    @GetMapping("user/filter/rlog/{id}")
    public CommonDto<Map<String,Object>> getLastLog(@PathVariable String id){
        CommonDto<Map<String,Object>> result =new CommonDto<>();

        try {
            result = userFilterLogService.userFilterSearchLog(id);
        }catch (Exception e){
            result.setMessage(e.getMessage());
            result.setStatus(501);
            result.setData(null);
            log.error(e.getMessage(),e.fillInStackTrace());
        }

        return result;
    }
}