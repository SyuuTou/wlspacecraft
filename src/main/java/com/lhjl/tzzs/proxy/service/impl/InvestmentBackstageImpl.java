package com.lhjl.tzzs.proxy.service.impl;

import com.lhjl.tzzs.proxy.mapper.InvestmentInstitutionsMapper;
import com.lhjl.tzzs.proxy.model.InvestmentInstitutions;
import com.lhjl.tzzs.proxy.service.InvestmentBackstageService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.*;

@Service
public class InvestmentBackstageImpl implements InvestmentBackstageService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(InvestmentBackstageImpl.class);

    @Resource
    private InvestmentInstitutionsMapper investmentInstitutionsMapper;

    @Value("${statistics.beginTime}")
    private String beginTime;

    @Value("${statistics.endTime}")
    private String endTime;


    @Transactional
    @Override
    public CommonDto<String> adminAddInvestmentBackstage(InvestmentInstitutions body) {
        CommonDto<String> result = new CommonDto<String>();

        String short_name = body.getShortName();
        String commet = body.getCommet();
        log.info("short_name={}", short_name);
        log.info("commet={}", commet);


        if (StringUtil.isEmpty(short_name)) {
            result.setStatus(50001);
            result.setMessage("请输入机构简称");

            return result;
        }
        if (StringUtil.isEmpty(commet)) {
            result.setStatus(50001);
            result.setMessage("请输入机构备注");

            return result;
        }

        int type = -1;
        if (body.getType() != null) {
            type = body.getType();
            log.info("type={}", type);
        } else {
            result.setMessage("请选择机构类型");
            result.setStatus(50002);
            return result;
        }

        Date now = new Date();

        InvestmentInstitutions investmentInstitutions = new InvestmentInstitutions();
        investmentInstitutions.setShortName(short_name);
        investmentInstitutions.setCreateTime(now);
        investmentInstitutions.setCommet(commet);
        investmentInstitutions.setType(type);

        investmentInstitutionsMapper.insert(investmentInstitutions);

        result.setStatus(200);
        result.setMessage("success");
        return result;
    }

    /**
     * 获取机构信息（50与非50）
     *
     * @return
     */
    @Cacheable(value = "findAllInvestment", keyGenerator = "wiselyKeyGenerator")
//    @CacheEvict(value = "findAllInvestment", allEntries = true)
    @Override
    public CommonDto<List<Map<String, Object>>> findAllInvestment() {
        CommonDto<List<Map<String, Object>>> list = new CommonDto<List<Map<String, Object>>>();
        List<Map<String, Object>> listForfive = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listNotfive = new ArrayList<Map<String, Object>>();
        List<InvestmentInstitutions> listAll = new ArrayList<InvestmentInstitutions>();

//        PageHelper.startPage(1, 80, false);
//
//        Example example = new Example(InvestmentInstitutions.class);
//        example.and().andBetween("type",0,1);
//        //获取非50机构（所有）
//        listNotfive = investmentInstitutionsMapper.selectByExample(example);
//        //获取50机构
//        InvestmentInstitutions query = new InvestmentInstitutions();
//        query.setType(1);
//        listForfive = investmentInstitutionsMapper.select(query);

        //获取50机构
        listForfive = investmentInstitutionsMapper.findInvestment50("1", null, null,beginTime,endTime);
        //获取非50机构（所有）
        listNotfive = investmentInstitutionsMapper.findInvestmentall("-1", 0, 150,beginTime,endTime);
        //组装所有数据
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("xiangmubiaoa", listForfive);
        map.put("xiangmubiaob", listNotfive);
        lists.add(map);

        //返回结果数据组装
        list.setStatus(200);
        list.setMessage("success");
        list.setData(lists);

        return list;
    }

    /**
     * 获取50机构信息（分页）
     *
     * @param pageNum  页数
     * @param pageSize 每页记录数
     * @return
     */
  @Cacheable(value = "findFiveInvestment", keyGenerator = "wiselyKeyGenerator")
   //@CacheEvict(value = "findFiveInvestment", allEntries = true)
    @Override
    public CommonDto<List<Map<String, Object>>> findFiveInvestment(Integer pageNum, Integer pageSize) {
        CommonDto<List<Map<String, Object>>> result = new CommonDto<List<Map<String, Object>>>();

        //计算查询起始记录
        Integer beginNum = (pageNum - 1) * pageSize;
        List<Map<String,Object>> investmentInstitutions = investmentInstitutionsMapper.findInvestment50("1", beginNum, pageSize,beginTime,endTime);
        //判断是否还有查询结果
//        if(investmentInstitutions.size() <= 0){
//            result.setStatus(202);
//            result.setMessage("无查询数据");
//            return result;
//        }

        result.setData(investmentInstitutions);
        result.setStatus(200);
        result.setMessage("success");
        return result;
    }

    /**
     * 获取非50机构信息（分页）
     *
     * @param pageNum  页数
     * @param pageSize 每页记录数
     * @return
     */
    @Cacheable(value = "findNotFiveInvestment", keyGenerator = "wiselyKeyGenerator")
   //@CacheEvict(value = "findNotFiveInvestment", allEntries = true)
    @Override
    public CommonDto<List<Map<String, Object>>> findNotFiveInvestment(Integer pageNum, Integer pageSize) {
        CommonDto<List<Map<String, Object>>> result = new CommonDto<List<Map<String, Object>>>();

        //计算查询起始记录
        Integer beginNum = (pageNum - 1) * pageSize;

        //最多返回160条记录
        if (beginNum > 150) {
            result.setStatus(201);
            result.setMessage("查询记录数超出限制（100条）");
            return result;
        } else {
            pageSize = (150 - beginNum) >= pageSize ? pageSize : (150 - beginNum);
        }

//        PageHelper.startPage(pageNum, pageSize, false);
//        Example example = new Example(InvestmentInstitutions.class);
//        example.and().andEqualTo("type", 0).andNotEqualTo("shortName", null).andNotEqualTo("shortName", "");
//        List<InvestmentInstitutions> investmentInstitutions = investmentInstitutionsMapper.selectByExample(example);

        List<Map<String, Object>> investmentInstitutions = investmentInstitutionsMapper.findInvestmentall("-1", beginNum, pageSize,beginTime,endTime);
        //判断是否还有查询结果
//        if(investmentInstitutions.size() <= 0){
//            result.setStatus(202);
//            result.setMessage("无查询数据");
//            return result;
//        }

        result.setData(investmentInstitutions);
        result.setStatus(200);
        result.setMessage("success");
        return result;
    }
}
