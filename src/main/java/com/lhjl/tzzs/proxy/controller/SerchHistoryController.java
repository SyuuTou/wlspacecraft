package com.lhjl.tzzs.proxy.controller;

import com.lhjl.tzzs.proxy.dto.SerchHistoryDto;
import com.lhjl.tzzs.proxy.model.MetaHotSearchWord;
import com.lhjl.tzzs.proxy.model.UserSearchLog;
import com.lhjl.tzzs.proxy.service.SerchHistoryService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.lhjl.tzzs.proxy.dto.CommonDto;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
public class SerchHistoryController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SerchHistoryController.class);

    @Resource
    private SerchHistoryService serchHistoryService;

    /**
     * 读搜索历史
     * user_id 用户id
     * @return
     */
    @GetMapping("user/log/rsearch/{user_id}")
    public CommonDto<List<UserSearchLog>> getSerchHistory(@PathVariable String user_id){
        CommonDto<List<UserSearchLog>> result = new CommonDto<List<UserSearchLog>>();

        try{
           result = serchHistoryService.rsearchHistory(user_id);
        }catch (Exception e){
             result = new CommonDto<List<UserSearchLog>>();
             result.setStatus(501);
             result.setMessage(e.getMessage());
             log.error(e.getMessage(),e.fillInStackTrace());
        }

        return result;
    }

    /**
     * 插入搜索记录
     * @userId 用户id
     * @searchContent 搜索内容
     * @return
     */
    @PostMapping("user/log/csearch")
    public CommonDto<String> insertOne(@RequestBody UserSearchLog body){
        CommonDto<String> result = new CommonDto<String>();
        String user_id = body.getUserId();
        String search_content = "";
        if (body.getSearchContent() != null){
            search_content = body.getSearchContent();
        }
        log.info("user_id={}",user_id);
        log.info("search_content={}",search_content);
        try{
            result = serchHistoryService.addSearchHistoryLog(user_id,search_content);
        }catch (Exception e){
            result.setStatus(501);
            result.setMessage(e.getMessage());
            result.setData(null);

            log.error(e.getMessage(),e.fillInStackTrace());
        }

        return result;
    }

    /**
     * 删除用户搜索历史
     * @param id
     * @return
     */

    @GetMapping("user/log/dsearch/{id}")
    public CommonDto<String> updateSearchHistoyYn(@PathVariable Integer id){
        CommonDto<String> result = new CommonDto<String>();
        try {
        result=serchHistoryService.updateUserSearchLogYn(id);

        }catch (Exception e){
            result.setMessage(e.getMessage());
            result.setStatus(200);
            result.setData(null);
        }
        return result;
    }


}
