package com.lhjl.tzzs.proxy.controller.projectfinancinglog.list;

import com.lhjl.tzzs.proxy.controller.GenericController;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.PagingOutputDto;
import com.lhjl.tzzs.proxy.dto.ProjectFinancingLogInputDto;
import com.lhjl.tzzs.proxy.dto.ProjectFinancingLogOutputDto;
import com.lhjl.tzzs.proxy.service.ProjectFinancingLogService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class ProjectFinancingLogListController extends GenericController{

    @Resource
    private ProjectFinancingLogService projectFinancingLogService;

    /**
     * 后台读取项目融资历史列表的接口
     * 一个接口两用
     * @param body
     * @return 
     */
    @PostMapping("get/projectfinancinglog/list")
    public CommonDto<PagingOutputDto<ProjectFinancingLogOutputDto>> getProjectFinancingLog(@RequestBody ProjectFinancingLogInputDto body){
        CommonDto<PagingOutputDto<ProjectFinancingLogOutputDto>> result = new CommonDto<>();
        try {
            result = projectFinancingLogService.getProjectFinancingLogList(body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setStatus(502);
            result.setData(null);
            result.setMessage("服务器端发生错误");
        }

        return result;
    }
}
