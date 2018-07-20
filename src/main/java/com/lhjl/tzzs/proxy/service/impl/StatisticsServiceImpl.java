package com.lhjl.tzzs.proxy.service.impl;

import com.lhjl.tzzs.proxy.dto.StatisticsDto;
import com.lhjl.tzzs.proxy.dto.HistogramList;
import com.lhjl.tzzs.proxy.dto.StatisticsDto;
import com.lhjl.tzzs.proxy.mapper.OpStatisticsNameMapper;
import com.lhjl.tzzs.proxy.mapper.StatisticsMapper;
import com.lhjl.tzzs.proxy.model.OpStatisticsName;
import com.lhjl.tzzs.proxy.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsService.class);

    @Value("${statistics.beginTime}")
    private String beginTime ;

    @Value("${statistics.endTime}")
    private String endTime;

    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private OpStatisticsNameMapper opStatisticsNameMapper;

    @Cacheable(value = "financingCountDistributed",keyGenerator = "wiselyKeyGenerator")
    @Override
    public StatisticsDto<List<HistogramList>> financingCountDistributed(String institutionType, String from , String size) {

        int from1  = Integer.parseInt(from);
        int size1 = Integer.parseInt(size);

        int froma = from1*size1;
        int sizea = size1;

        StatisticsDto<List<HistogramList>> result = new StatisticsDto<List<HistogramList>>();
        Integer type = null;
        if (institutionType.equals("50")){
            type = 1;
        }
        List<HistogramList>  histogramLists = statisticsMapper.financingCountDistributed(type,beginTime,endTime,froma,sizea);
        result.setData(histogramLists);
        OpStatisticsName query = new OpStatisticsName();
        query.setKey("count");
        result.setStatisticsName(opStatisticsNameMapper.selectOne(query));
        result.setMessage("success");
        result.setStatus(200);
        return result;
    }

    @Cacheable(value = "financingAmountDistributed",keyGenerator = "wiselyKeyGenerator")
    @Override
    public StatisticsDto<List<HistogramList>> financingAmountDistributed(String institutionType, String from, String size) {

        int from1  = Integer.parseInt(from);
        int size1 = Integer.parseInt(size);

        int froma = from1*size1;
        int sizea = size1;

        StatisticsDto<List<HistogramList>> result = new StatisticsDto<List<HistogramList>>();
        Integer type = null;
        String flag = null;
        if (institutionType.equals("50")){
            type = 1;
            flag = "amount";
        }else{
            flag = "total_amount";
        }
        List<HistogramList>  histogramLists = statisticsMapper.financingAmountDistributed(type,beginTime,endTime, flag,froma,sizea);
        result.setData(histogramLists);
        OpStatisticsName query = new OpStatisticsName();
        query.setKey("amount");
        result.setStatisticsName(opStatisticsNameMapper.selectOne(query));
        result.setMessage("success");
        result.setStatus(200);
        return result;
    }

    @Cacheable(value = "financingSegmentationDistributed",keyGenerator = "wiselyKeyGenerator")
    @Override
    public StatisticsDto<List<HistogramList>> financingSegmentationDistributed(String institutionType, String from, String size) {

        int from1  = Integer.parseInt(from);
        int size1 = Integer.parseInt(size);

        int froma = from1*size1;
        int sizea = size1;

        StatisticsDto<List<HistogramList>> result = new StatisticsDto<List<HistogramList>>();
        Integer type = null;
        if (institutionType.equals("50")){
            type = 1;
        }
        List<HistogramList>  histogramLists = statisticsMapper.financingSegmentationDistributed(type,beginTime,endTime,froma,sizea);
        result.setData(histogramLists);
        OpStatisticsName query = new OpStatisticsName();
        query.setKey("segmentation");
        result.setStatisticsName(opStatisticsNameMapper.selectOne(query));
        result.setMessage("success");
        result.setStatus(200);
        return result;
    }

    @Cacheable(value = "financingCityDistributed",keyGenerator = "wiselyKeyGenerator")
    @Override
    public StatisticsDto<List<HistogramList>> financingCityDistributed(String institutionType, String from, String size) {

        int from1  = Integer.parseInt(from);
        int size1 = Integer.parseInt(size);

        int froma = from1*size1;
        int sizea = size1;

        StatisticsDto<List<HistogramList>> result = new StatisticsDto<List<HistogramList>>();
        Integer type = null;
        if (institutionType.equals("50")){
            type = 1;
        }
        List<HistogramList>  histogramLists = statisticsMapper.financingCityDistributed(type,beginTime,endTime,froma,sizea);
        result.setData(histogramLists);
        OpStatisticsName query = new OpStatisticsName();
        query.setKey("city");
        result.setStatisticsName(opStatisticsNameMapper.selectOne(query));
        result.setMessage("success");
        result.setStatus(200);
        return result;
    }

    @Cacheable(value = "financingEducationExperienceDistributed",keyGenerator = "wiselyKeyGenerator")
    @Override
    public StatisticsDto<List<HistogramList>> financingEducationExperienceDistributed(String institutionType, String from, String size) {

        int from1  = Integer.parseInt(from);
        int size1 = Integer.parseInt(size);

        int froma = from1*size1;
        int sizea = size1;

        StatisticsDto<List<HistogramList>> result = new StatisticsDto<List<HistogramList>>();
        Integer type = null;
        if (institutionType.equals("50")){
            type = 1;
        }
        List<HistogramList>  histogramLists = statisticsMapper.financingEducationExperienceDistributed(type,beginTime,endTime,froma,sizea);
        result.setData(histogramLists);
        OpStatisticsName query = new OpStatisticsName();
        query.setKey("education_experience");
        result.setStatisticsName(opStatisticsNameMapper.selectOne(query));
        result.setMessage("success");
        result.setStatus(200);
        return result;
    }

    @Cacheable(value = "financingWorkExperienceDistributed",keyGenerator = "wiselyKeyGenerator")
    @Override
    public StatisticsDto<List<HistogramList>> financingWorkExperienceDistributed(String institutionType, String from, String size) {

        int from1  = Integer.parseInt(from);
        int size1 = Integer.parseInt(size);

        int froma = from1*size1;
        int sizea = size1;

        StatisticsDto<List<HistogramList>> result = new StatisticsDto<List<HistogramList>>();
        Integer type = null;
        if (institutionType.equals("50")){
            type = 1;
        }
        List<HistogramList>  histogramLists = statisticsMapper.financingWorkExperienceDistributed(type,beginTime,endTime,froma,sizea);
        result.setData(histogramLists);
        OpStatisticsName query = new OpStatisticsName();
        query.setKey("work_experience");
        result.setStatisticsName(opStatisticsNameMapper.selectOne(query));
        result.setMessage("success");
        result.setStatus(200);
        return result;
    }

//    @Cacheable(value = "financingInvestmentDistributed",keyGenerator = "wiselyKeyGenerator")
    @Override
    public StatisticsDto<List<HistogramList>> financingInvestmentDistributed(String institutionType, String from, String size) {

        int from1  = Integer.parseInt(from);
        int size1 = Integer.parseInt(size);

        int froma = from1*size1;
        int sizea = size1;

        StatisticsDto<List<HistogramList>> result = new StatisticsDto<List<HistogramList>>();
        Integer type = null;
        if (institutionType.equals("50")){
            type = 1;
        }
        List<HistogramList>  histogramLists = statisticsMapper.financingInvestmentDistributed(type,beginTime,endTime,froma,sizea);
        List<HistogramList>  projectList = statisticsMapper.financingCountDistributed(type,beginTime,endTime,froma,sizea);
        Integer sum = 0;
        Integer citySum = 0 ;
        if (null != histogramLists && histogramLists.size() > 0) {
            for (HistogramList histogramList : histogramLists) {
                sum += Integer.valueOf(histogramList.getX());
            }

            for (HistogramList histogramList : projectList) {
                citySum += Integer.valueOf(histogramList.getX());
            }

            if (Integer.valueOf(histogramLists.get(histogramLists.size() - 1).getX()) + (citySum - sum) > 0)
                histogramLists.get(histogramLists.size() - 1).setY("其它");
            histogramLists.get(histogramLists.size() - 1).setX(String.valueOf(Integer.valueOf(histogramLists.get(histogramLists.size() - 1).getX()) + (citySum - sum)));
        }
        result.setData(histogramLists);
        OpStatisticsName query = new OpStatisticsName();
        query.setKey("investment_allocation");
        result.setStatisticsName(opStatisticsNameMapper.selectOne(query));
        result.setMessage("success");
        result.setStatus(200);
        return result;
    }

    @Override
    public StatisticsDto<List<HistogramList>> financingInvestmentCharacteristicDistributed(String institutionType, String from, String size) {
        int from1  = Integer.parseInt(from);
        int size1 = Integer.parseInt(size);

        int froma = from1*size1;
        int sizea = size1;

        StatisticsDto<List<HistogramList>> result = new StatisticsDto<List<HistogramList>>();
        Integer type = null;
        if (institutionType.equals("50")){
            type = 1;
        }
        List<HistogramList>  histogramLists = statisticsMapper.financingInvestmentCharacteristicDistributed(type,beginTime,endTime,froma,sizea);
        result.setData(histogramLists);
        OpStatisticsName query = new OpStatisticsName();
        query.setKey("characteristic");
        result.setStatisticsName(opStatisticsNameMapper.selectOne(query));
        result.setMessage("success");
        result.setStatus(200);
        return result;
    }

    @Override
    public StatisticsDto<List<HistogramList>> financingInvestmentSegmentationDistributed(String institutionType, String from, String size) {
        int from1  = Integer.parseInt(from);
        int size1 = Integer.parseInt(size);

        int froma = from1*size1;
        int sizea = size1;

        StatisticsDto<List<HistogramList>> result = new StatisticsDto<List<HistogramList>>();
        Integer type = null;
        if (institutionType.equals("50")){
            type = 1;
        }
        List<HistogramList>  histogramLists = statisticsMapper.financingInvestmentFocsSegmentationDistributed(type,beginTime,endTime,froma,sizea);
        result.setData(histogramLists);
        OpStatisticsName query = new OpStatisticsName();
        query.setKey("racing_track");
        result.setStatisticsName(opStatisticsNameMapper.selectOne(query));
        result.setMessage("success");
        result.setStatus(200);
        return result;
    }
}
