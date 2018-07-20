package com.lhjl.tzzs.proxy.mapper;

import com.lhjl.tzzs.proxy.dto.HistogramList;
import com.lhjl.tzzs.proxy.dto.LabelList;
import com.lhjl.tzzs.proxy.model.MetaFinancing;
import com.lhjl.tzzs.proxy.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface MetaFinancingMapper  extends OwnerMapper<MetaFinancing> {

    List<LabelList> queryHotCity();

    List<LabelList> queryHotEducation();

    List<LabelList> queryHotWork();

    List<LabelList> queryHotIndustry();

    List<HistogramList> queryValuation(@Param("investment") String investment, @Param("roundName") String roundName, @Param("industryName") String industryName, @Param("cityName") String cityName, @Param("educationName") String educationName, @Param("workName") String workName, @Param("granularity") Integer granularity, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("index") Integer index, @Param("size") Integer size);

    List<HistogramList> queryFinancingAmount(@Param("investment") String investment, @Param("roundName") String roundName, @Param("industryName") String industryName, @Param("cityName") String cityName, @Param("educationName") String educationName, @Param("workName") String workName, @Param("granularity") Integer granularity, @Param("flag") String flag, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("index") Integer index, @Param("size") Integer size, @Param("startIndex") Integer startIndex, @Param("limit") Integer limit);

    Map<String,Object> queryFinancingCount(@Param("investment") String investment, @Param("roundName") String roundName, @Param("industryName") String industryName, @Param("cityName") String cityName, @Param("educationName") String educationName, @Param("workName") String workName, @Param("granularity") Integer granularity, @Param("flag") String flag, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    Map<String,Object> queryValuationCount(@Param("investment") String investment, @Param("roundName") String roundName, @Param("industryName") String industryName, @Param("cityName") String cityName, @Param("educationName") String educationName, @Param("workName") String workName, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    Map<String,Object> queryFinancingCount(Map<String, String> params);

    List<HistogramList> queryFinancingAmount(Map<String, String> queryAmount);


    BigDecimal queryFinancingAvgAmount(@Param("investment") String investment, @Param("roundName") String roundName, @Param("industryName") String industryName, @Param("cityName") String cityName, @Param("educationName") String educationName, @Param("workName") String workName, @Param("granularity") Integer granularity, @Param("flag") String flag, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("limit") Integer limit);

    BigDecimal queryValuationAvg(@Param("investment") String investment, @Param("roundName") String roundName, @Param("industryName") String industryName, @Param("cityName") String cityName, @Param("educationName") String educationName, @Param("workName") String workName, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("limit") Integer limit);

    BigDecimal queryValuationAvg(@Param("investment") String investment, @Param("roundName") String roundName, @Param("industryName") String industryName, @Param("cityName") String cityName, @Param("educationName") String educationName, @Param("workName") String workName, @Param("beginTime") String avgBeginTime, @Param("endTime") String avgEndTime, @Param("granularity") Integer granularity, @Param("minAmount") String minAmount, @Param("maxAmount") String maxAmount);

    BigDecimal queryFinancingAvgAmount(@Param("investment") String investment, @Param("roundName") String roundName, @Param("industryName") String industryName, @Param("cityName") String cityName, @Param("educationName") String educationName, @Param("workName") String workName, @Param("granularity") Integer granularity, @Param("flag") String flag, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("minAmount") String minAmount, @Param("maxAmount") String maxAmount);

}