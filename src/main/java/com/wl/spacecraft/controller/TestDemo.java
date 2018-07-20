package com.wl.spacecraft.controller;



import java.util.List;

import javax.annotation.Resource;

import com.wl.spacecraft.controller.common.GenericController;
import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.requestdto.TestBody;
import com.wl.spacecraft.model.AppIntergral;
import com.wl.spacecraft.service.user.UserService;
import com.wl.spacecraft.utils.DateUtils;
import org.springframework.web.bind.annotation.*;

import com.lhjl.tzzs.proxy.dto.Test;
import com.lhjl.tzzs.proxy.mapper.InvestmentInstitutionsMapper;
import com.lhjl.tzzs.proxy.mapper.InvestorSegmentationMapper;
import com.lhjl.tzzs.proxy.mapper.MetaDataSourceTypeMapper;
import com.lhjl.tzzs.proxy.model.InvestorSegmentation;
import com.lhjl.tzzs.proxy.model.MetaDataSourceType;
import com.lhjl.tzzs.proxy.service.InvestorSegmentationService;



/**
 * just for test , no other values
 * @author syuutou
 *
 */
@RestController
public class TestDemo extends GenericController {
    @Resource
    private UserService userServiceImpl;

    @GetMapping("/demo")
    public CommonDto<Object> demo(@RequestParam("id") int id){
        System.err.println("id");
        CommonDto<Object> result=new CommonDto<>();
        Object obj =null;
        try{
              result.setData(obj);
        }catch(Exception e){
            result.setData(obj);
            result.setMessage("qwe");
            result.setStatus(200);
        }
        return result;
    }

    @GetMapping("/dateUtilsTest")
    public CommonDto<Object> demo(){
        System.err.println("id");
        CommonDto<Object> result=new CommonDto<>();
        Object obj =null;
        try{
            System.err.println(DateUtils.getEndTime());;
            System.err.println(DateUtils.getStartTime());;
            System.err.println(DateUtils.getTomorrow());;
            System.err.println(DateUtils.getYestoday());;
        }catch(Exception e){
            result.setData(obj);
            result.setMessage("qwe");
            result.setStatus(200);
        }
        return result;
    }

    @PostMapping("/getSegs")
    public Object getSegmentations(@RequestBody TestBody body){
        System.err.println("body->"+body);
        return body;
//        return investorSegmentationMapper.getInvestorSegmentations(body.getSegs());
//		return "qwe";
    }

    //测试事务
    @PostMapping("test/Transantion")
    public Object testTransantion(@RequestBody InvestorSegmentation body){
        Integer result=-1;
        try {
            System.err.println(body);
//            result = investorSegmentationService.edit(body);
            System.out.println("qwe");
            System.out.println("asdasd");
        }catch(Exception e) {
            return "异常发生";
        }

        return result;
    }

    @GetMapping("getId")
    public Object get() {
        Integer ids=-1;
        try{
//            ids = investmentInstitutionsMapper.selectByCompanyName("隆领投资");
            System.out.println("测试");
        }catch(Exception e) {
            e.printStackTrace();
            return "false";
        }
        return ids;
    }
    //获取实体映射
	/*@GetMapping("getEntityMap")
	public Object getMap() {
		List<MetaDataSourceType> result = metaDataSourceTypeMapper.getEntityTest();
		return result;
	}*/
}

