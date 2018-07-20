package com.lhjl.tzzs.proxy.controller.investor.palatform.details;

import com.lhjl.tzzs.proxy.controller.GenericController;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.investorDto.InvestorBasicInfoInputDto;
import com.lhjl.tzzs.proxy.dto.investorDto.InvestorBasicInfoOutputDto;
import com.lhjl.tzzs.proxy.dto.investorDto.InvestorIntroductionDto;
import com.lhjl.tzzs.proxy.model.MetaDiploma;
import com.lhjl.tzzs.proxy.model.MetaRegion;
import com.lhjl.tzzs.proxy.service.InvestorBasicinfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author caochuangui'
 * @date 2018-1-30 17:24:11
 */
@RestController
public class InvestorBasicinfoController extends GenericController{

    @Resource
    private InvestorBasicinfoService investorBasicinfoService;
    /**
     * 添加或者更新投资人的基本信息
     * @param body
     * @return
     */
    @PostMapping("/addorupdateinvestorbasicinfo")
    public CommonDto<Boolean> addOrUpdateInvestorBasicInfo(@RequestBody InvestorBasicInfoInputDto body){
        CommonDto<Boolean> result = new CommonDto<>();
        try {
            result = investorBasicinfoService.addOrUpdateInvestorBasicInfo(body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(false);
            result.setStatus(502);
        }
        return result;
    }
    /**
     * 获取投资人基本信息
     * @param investorId
     * @return
     */
    @GetMapping("/getinvestorbasicinfo")
    public CommonDto<InvestorBasicInfoOutputDto> getInvestorBasicInfo(Integer investorId){
        CommonDto<InvestorBasicInfoOutputDto> result = new CommonDto<>();
        try {
            result = investorBasicinfoService.getInvestorBasicInfo(investorId);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(null);
            result.setStatus(502);
        }
        return result;
    }

    /**
     * 获取城市信息元数据
     * @return
     */
    @GetMapping("/getallcontry")
    public CommonDto<List<MetaRegion>> getAllContry(){
        CommonDto<List<MetaRegion>> result = new CommonDto<>();
        try {
            result = investorBasicinfoService.getAllContry();
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(null);
            result.setStatus(502);
        }
        return result;
    }
    /**
     * 获取学历元数据
     * @return
     */
    @GetMapping("/getalldiploma")
    public CommonDto<List<MetaDiploma>> getAllDiploma(){
        CommonDto<List<MetaDiploma>> result = new CommonDto<>();
        try {
            result = investorBasicinfoService.getAllDiploma();
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(null);
            result.setStatus(502);
        }
        return result;
    }

}
