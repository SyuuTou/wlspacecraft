package com.lhjl.tzzs.proxy.controller.investor.palatform.details;

import com.lhjl.tzzs.proxy.controller.GenericController;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.investorDto.InvestorCertificationDto;
import com.lhjl.tzzs.proxy.service.InvestorCertificationInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 认证投资人模块
 * @author caochuangui
 * @date 2018-1-30 17:24:30
 */
@RestController
public class InvestorCertificationInfoController extends GenericController{

    @Resource
    private InvestorCertificationInfoService investorCertificationInfoService;

    /**
     * 保存或者更新认证投资人模块的信息
     * @param body
     * @return
     */
    @PostMapping("/addorupdateinvestorcertification")
    public CommonDto<Boolean> addOrUpdateInvestorCertification(@RequestBody InvestorCertificationDto body){
        CommonDto<Boolean> result = new CommonDto<>();
        try {
            result = investorCertificationInfoService.addOrUpdateInvestorCertification(body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(null);
            result.setStatus(502);
        }
        return result;
    }

    /**
     * 获取认证投资人模块的信息
     * @param investorId
     * @return
     */
    @GetMapping("/getinvestorcertification")
    public CommonDto<InvestorCertificationDto> getInvestorCertification(Integer investorId){
        CommonDto<InvestorCertificationDto> result = new CommonDto<>();
        try {
            result = investorCertificationInfoService.getInvestorCertification(investorId);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(null);
            result.setStatus(502);
        }
        return result;
    }
}
