package com.lhjl.tzzs.proxy.controller.investor.palatform.list;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lhjl.tzzs.proxy.controller.GenericController;
import com.lhjl.tzzs.proxy.dto.ChangePrincipalInputDto;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.PagingOutputDto;
import com.lhjl.tzzs.proxy.dto.investorDto.InvestorListInputDto;
import com.lhjl.tzzs.proxy.dto.investorDto.InvestorsOutputDto;
import com.lhjl.tzzs.proxy.model.Users;
import com.lhjl.tzzs.proxy.service.InvestorService;

@RestController
public class InvestorListController extends GenericController {
	@Resource 
	private InvestorService investorService;
	
	/**
	 * 投资人列表（包括投资人审核列表,一个接口两用）
	 * @param appid
	 * @param body 投资人列表请求体
	 * @return
	 */
    @PostMapping("/v{appid}/list/investors")
    public CommonDto<PagingOutputDto<InvestorsOutputDto>> listInvestorsInfo(@PathVariable Integer appid,@RequestBody InvestorListInputDto body){
    	CommonDto<PagingOutputDto<InvestorsOutputDto>> result =new CommonDto<>();
    	try {
    		result = investorService.listInvestorsInfos(appid,body);
    	}catch(Exception e) {
    		this.LOGGER.error(e.getMessage(), e.fillInStackTrace());
    		
    		result.setData(null);
    		result.setMessage("fail");
    		result.setStatus(500);
    	}
        return result;
    }
    /**
     * 批量或者指定更换投资人的负责人
     * @param appid
     * @param InvestorIds 投资人的id数组
     * @param principal 负责人姓名
     * @return
     */
    @PutMapping("/v{appid}/change/irprincipal/batchorsingle")
    public CommonDto<Boolean> changeIrPrincipalBatchOrSingle(@PathVariable Integer appid,@RequestBody ChangePrincipalInputDto body){
    	CommonDto<Boolean> result =new CommonDto<>();
    	try {
    		result = investorService.changeIrPrincipalBatchOrSingle(appid,body);
    	}catch(Exception e) {
    		this.LOGGER.error(e.getMessage(), e.fillInStackTrace());
    		
    		result.setData(false);
    		result.setMessage("fail");
    		result.setStatus(500);
    	}
        return result;
    }
    /**
     * 投资人的用户匹配
     * @param appid
     * @param userId
     * @return
     */
    @GetMapping("/v{appid}/match/users")
    public CommonDto<Boolean> matchUsers(@PathVariable Integer appid,Integer userId, Integer investorId){
    	CommonDto<Boolean> result =new CommonDto<>();
    	try {
    		result = investorService.matchUsers(appid,userId,investorId);
    	}catch(Exception e) {
    		this.LOGGER.error(e.getMessage(), e.fillInStackTrace());
    		
    		result.setData(false);
    		result.setMessage("fail");
    		result.setStatus(500);
    	}
        return result;
    }
}
