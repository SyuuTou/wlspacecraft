package com.lhjl.tzzs.proxy.controller;

import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.InvestmentInstitutionComplexOutputDto;
import com.lhjl.tzzs.proxy.dto.InvestmentInstitutionSearchOutputDto;
import com.lhjl.tzzs.proxy.dto.InvestmentInstitutionsDto2;
import com.lhjl.tzzs.proxy.model.InvestmentInstitutions;
import com.lhjl.tzzs.proxy.model.InvestmentInstitutionsAddressPart;
import com.lhjl.tzzs.proxy.model.MetaProjectStage;
import com.lhjl.tzzs.proxy.model.MetaSegmentation;
import com.lhjl.tzzs.proxy.service.InvestmentInstitutionsService;
import com.lhjl.tzzs.proxy.service.bluewave.UserLoginService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class InvestmentInstitutionsController extends GenericController {

    private static final Logger log = LoggerFactory.getLogger(InvestmentInstitutionsController.class);

    @Resource
    private InvestmentInstitutionsService investmentInstitutionsService;
    
    /**
     * 根据机构id获取机构信息的接口
     * @param body
     * @return
     */
    @GetMapping("get/investmentinstitutons/complexinfo")
    public CommonDto<InvestmentInstitutionComplexOutputDto> getInvestmentInstitutionsComplexinfo(String investmentInstitumentId){
        CommonDto<InvestmentInstitutionComplexOutputDto> result = new CommonDto<>();

        try {
//            result =  investmentInstitutionsService.getInvestmentInstitutionsComlexInfo(investmentInstitumentId);

        }catch (Exception e){
            log.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
            result.setData(null);
        }  

        return result;
    }
    /**
     * 回显机构信息的数据,
     *   如果没有相关的机构，则返回空数据
     *   如果存在相关的机构，则直接回显
     * @Param id 作为机构id
     * @return
     */
    @GetMapping("/v{appid}/echoinstiinfo")
    public CommonDto<InvestmentInstitutionsDto2> echoinstiinfo(Integer id,@PathVariable("appid") Integer appid){
    	CommonDto<InvestmentInstitutionsDto2> result =new CommonDto<>();
    	try {
    		
    		result = investmentInstitutionsService.echoinstiinfo(id, appid);
    	}catch(Exception e){
    		log.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
            result.setData(null);
    	}
    	return result;
    }
    
    /**
     * 点击保存按钮的实现接口
     * 	如果存在相关的机构信息 则执行更新操作
     * 	如果不存在相关的机构信息，则执行插入操作
     * @param investmentInstitumentId
     * @return
     */
    @PostMapping("/v{appid}/institution/saveOrUpdate")
    public CommonDto<Boolean> institutionSaveOrUpdate(@PathVariable("appid") Integer appid,@RequestBody InvestmentInstitutionsDto2 body){
    	CommonDto<Boolean> result =new CommonDto<>();
    	try {
    		result = investmentInstitutionsService.saveOrUpdate(appid,body);
    	}catch(Exception e){
    		log.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
            result.setData(null);
    	}
    	return result;  
    }
    
    /**
     * 获取所有的投资阶段信息--zd
     * @return
     */
    @GetMapping("/v{appid}/invest_step")
    public CommonDto<List<MetaProjectStage>> getAllInvestementStages(@PathVariable("appid") Integer appid){
    	CommonDto<List<MetaProjectStage>> result=new CommonDto<>();
    	
        try {
            result =  investmentInstitutionsService.listInvestementStages();

        }catch (Exception e){
            log.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生 ");
            result.setStatus(502);
            result.setData(null);
        }
    	return result;
    }
    /**
     * 获取所有的投资领域信息--zd
     * @return
     */
    @GetMapping("/v{appid}/invest_field")
    public CommonDto<List<MetaSegmentation>> getAllInvestementFields(@PathVariable("appid") Integer appid){
    	CommonDto<List<MetaSegmentation>> result=new CommonDto<>();
    	
        try {
            result =  investmentInstitutionsService.listInvestementFields();

        }catch (Exception e){
            log.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
            result.setData(null);
        }
    	return result;
    }
    /**
     * 获取该投资机构的所有分部信息
     * @return
     */
    @GetMapping("get/addresspart")
    public CommonDto<List<InvestmentInstitutionsAddressPart>> getAllAddressPartsById(Integer investmentInstitutionId){
    	CommonDto<List<InvestmentInstitutionsAddressPart>> result=new CommonDto<>();
    	
        try {
            result =  investmentInstitutionsService.listAllAddressPartsById(investmentInstitutionId);

        }catch (Exception e){
            log.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
            result.setData(null);
        }
    	return result;
    }
    /**
     * 根据输入词搜索机构信息接口
     * @param inputsWords 输入的词
     * @return
     */
    @GetMapping("institutions/intelligent/search")
    public CommonDto<List<InvestmentInstitutionSearchOutputDto>> institutionsIntelligentSearch(String inputsWords,Integer pageSize){
        CommonDto<List<InvestmentInstitutionSearchOutputDto>> result =new CommonDto<>();

        try {
            result = investmentInstitutionsService.getInstitutionIntelligentSearch(inputsWords,pageSize);
        }catch (Exception e){
            log.error(e.getMessage(),e.fillInStackTrace());

            result.setStatus(502);
            result.setMessage("服务器端发生错误");
            result.setData(null);
        }

        return result;
    }

    /**
     * 获取机构详情的接口
     * @param institutionId
     * @return
     */
    @GetMapping("institutions/details")
    public CommonDto<Map<String,Object>> findInstitutionDetails(Integer institutionId){
        CommonDto<Map<String,Object>> result  = new CommonDto<>();

        try {
            result = investmentInstitutionsService.findInstitutionDetails(institutionId);
        }catch (Exception e){
            log.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(null);
            result.setStatus(502);
        }

        return result;
    }

    @GetMapping("institutions/flliters")
    public CommonDto<Map<String,Object>> findInstitutionFlliters(Integer institutionId){
        CommonDto<Map<String,Object>> result  = new CommonDto<>();
        try {
            result = investmentInstitutionsService.findFliterInfo(institutionId);
        }catch (Exception e){
            log.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
            result.setData(null);
        }

        return result;
    }
}
