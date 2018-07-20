package com.lhjl.tzzs.proxy.service.impl;


import com.lhjl.tzzs.proxy.dto.SearchLimitDto;
import com.lhjl.tzzs.proxy.mapper.UserSearchLogMapper;
import com.lhjl.tzzs.proxy.model.UserSearchLog;
import com.lhjl.tzzs.proxy.service.SerchHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SerchHistoryImpl implements SerchHistoryService {


    private static final Logger LOGGER = LoggerFactory.getLogger( SerchHistoryImpl.class);
    @Resource
    private UserSearchLogMapper userSearchLogMapper;

    @Value("${usersearch.limits}")
    private int limits;

    //@Cacheable(value = "rsearchHistory",keyGenerator = "wiselyKeyGenerator")
    //取消缓存
    @Override
    public CommonDto<List<UserSearchLog>> rsearchHistory(String user_id){
        CommonDto<List<UserSearchLog>> result = new CommonDto<List<UserSearchLog>>();
        SearchLimitDto searchLimitDto = new SearchLimitDto();
        searchLimitDto.setUser_id(user_id);
        searchLimitDto.setLimits(limits);
        List<UserSearchLog> userSearchLogs = userSearchLogMapper.selectNewRecords(searchLimitDto);


        result.setMessage("success");
        result.setData(userSearchLogs);
        result.setStatus(200);

        return result;
    }

    @Transactional
   @Override
  public  CommonDto<String> addSearchHistoryLog(String user_id,String search_content){
        CommonDto<String> result = new CommonDto<String>();

       Date search_time = new Date();
       int amount = 1;
       int yn = 0;


       //用来录入的实例
       UserSearchLog userSearchLog = new UserSearchLog();

       userSearchLog.setAmount(amount);
       userSearchLog.setSearchContent(search_content);
       userSearchLog.setSearchTime(search_time);
       userSearchLog.setUserId(user_id);
       userSearchLog.setYn(yn);

       //用来查询的实例
       UserSearchLog usl = new UserSearchLog();
       usl.setSearchContent(search_content);
       usl.setUserId(user_id);



        //根据实例查询
        List<UserSearchLog> userSearchLogs =  userSearchLogMapper.select(usl);

        //判断实例是否存在，存在就更新，不存在就添加
        if(userSearchLogs.size() != 0) {
            for (UserSearchLog uslTemp : userSearchLogs) {
                int amountnum = uslTemp.getAmount();
                amountnum += 1;
                uslTemp.setAmount(amountnum);
                uslTemp.setYn(0);
                userSearchLogMapper.updateByPrimaryKey(uslTemp);
            }
        }else{
            userSearchLogMapper.insert(userSearchLog);
        }

        //添加成功返回结果
        result.setMessage("success");
        result.setStatus(200);
        result.setData(null);

        return result;

  }

  @Transactional
  @Override
    public CommonDto<String> updateUserSearchLogYn(Integer id){
      CommonDto<String> result = new CommonDto<String>();

      //构造查询的实例
      UserSearchLog userSearchLog = new UserSearchLog();
      userSearchLog.setId(id);

      List<UserSearchLog> userSearchLogs = userSearchLogMapper.select(userSearchLog);

      for (UserSearchLog usltemp:userSearchLogs){
          usltemp.setYn(1);
          userSearchLogMapper.updateByPrimaryKey(usltemp);
      }

      result.setStatus(200);
      result.setMessage("success");
      result.setData(null);

      return result;
  }

}

