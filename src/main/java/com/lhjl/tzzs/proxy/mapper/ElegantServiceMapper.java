package com.lhjl.tzzs.proxy.mapper;

import com.lhjl.tzzs.proxy.model.ElegantService;
import com.lhjl.tzzs.proxy.utils.OwnerMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ElegantServiceMapper extends OwnerMapper<ElegantService> {
    /**
     * 获取精选活动列表
     * @return
     */
    List<Map<String,Object>> findElegantServiceList(@Param("token") String token, @Param("recommendYn") Integer recommendYn,
                                                    @Param("createTimeOrder") Integer createTimeOrder,
                                                    @Param("sortOrder") Integer sortOrder,
                                                    @Param("appid") Integer appid,
                                                    @Param("identityType") Integer[] identityType,
                                                    @Param("serviceType") Integer[] serviceType,
                                                    @Param("searchWord") String searchWord,
                                                    @Param("approveType") List<Integer> approveType, @Param("isLeadInvestor") Integer isLeadInvestor, @Param("isReward") Integer isReward, @Param("memberType") Integer memberType, @Param("startPage") Integer startPage,
                                                    @Param("pageSize") Integer pageSize, @Param("now") Date now);

    /**
     * 根据服务id获取服务详情
     * @param elegantServiceId
     * @return
     */
    Map<String,Object> findElegantServiceById(@Param("elegantServiceId") Integer elegantServiceId);

    List<Map<String,Object>> findBackstageElegantServiceList(@Param("searchWord") String searchWord,
                                                             @Param("appid") Integer appid,
                                                             @Param("beginTime") String beginTime,
                                                             @Param("endTime") String endTime,
                                                             @Param("approveType") Integer approveType, @Param("isLeadInvestor") Integer isLeadInvestor, @Param("isReward") Integer isReward, @Param("memberType") Integer memberType, @Param("startPage") Integer startPage,
                                                             @Param("pageSize") Integer pageSize);

    Integer selectCountBySearch(@Param("searchWord") String searchWord,
                                @Param("appid") Integer appid,
                                @Param("approveType") Integer approveType, @Param("isLeadInvestor") Integer isLeadInvestor, @Param("isReward") Integer isReward, @Param("memberType") Integer memberType,
                                @Param("beginTime") String beginTime,@Param("endTime") String endTime);

    List<Map<String,Object>> findElegantServiceListCustomer(@Param("token") String token, @Param("isReward") Integer isReward, @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize, @Param("now") Date date);
}