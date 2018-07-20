package com.lhjl.tzzs.proxy.controller.project.collect.details;

import com.lhjl.tzzs.proxy.controller.GenericController;
import com.lhjl.tzzs.proxy.dto.CollectProjectAuditCompanyIntroDto;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.service.CollectProjectAuditCompanyIntroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lanhaijulang on 2018/2/1.
 */
@RestController
public class CollectProjectAuditCompanyIntroController extends GenericController{

    @Autowired
    private CollectProjectAuditCompanyIntroService collectProjectAuditCompanyIntroService;
    
    /**
     * 回显采集项目详情的公司简介
     * @param projectId
     * @return
     */
    @GetMapping("/collectprojectauditcompanyintro")
    public CommonDto<CollectProjectAuditCompanyIntroDto> getCollectProjectAuditBasicInfo(Integer projectId){
        CommonDto<CollectProjectAuditCompanyIntroDto> result = new CommonDto<>();
        try {
            result = collectProjectAuditCompanyIntroService.getCollectProjectAuditCompanyIntro(projectId);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(null);
            result.setStatus(502);
        }
        return result;
    }
}
