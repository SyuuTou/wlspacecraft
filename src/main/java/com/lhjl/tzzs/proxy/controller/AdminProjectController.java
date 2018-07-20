package com.lhjl.tzzs.proxy.controller;

import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.service.AdminProjectService;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AdminProjectController extends GenericController {

    @Resource
    private AdminProjectService adminProjectService;

    /**
     * 后台获取项目列表接口
     * @param pageNum 页数
     * @param pageSize 每页显示数量
     * @param shortName 项目简称（搜索用）
     * @param projectType 项目类型：0数据导入项目，1表示用户提交项目
     * @return
     */
    @GetMapping("admin/get/project/list")
    public CommonDto<List<Map<String,Object>>> adminGetProjectList(Integer pageNum,Integer pageSize,String shortName,Integer projectType){
            CommonDto<List<Map<String,Object>>> result = new CommonDto<>();
            List<Map<String,Object>> list = new ArrayList<>();

            try {
                result = adminProjectService.getProjectList(pageNum,pageSize,shortName,projectType);
            }catch (Exception e){
                this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
                result.setData(list);
                result.setMessage("服务器端发生错误");
                result.setStatus(502);
            }

            return result;
    }

    /**
     * 一键设置没有管理员项目的管理员
     * @param administractorId
     * @return
     */
    @GetMapping("set/project/admin/onestep")
    public CommonDto<String> setProjectAdminOnestep(Integer administractorId){
        CommonDto<String> result = new CommonDto<>();

        try {
            result = adminProjectService.setProjectAdminOnestep(administractorId);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setData(null);
            result.setStatus(502);
            result.setMessage("服务器端发生错误");
        }
        return result;
    }

    /**
     * 给项目添加管理员
     * @param projectId 项目id
     * @param administractorsId 管理员组
     * @return
     */
    @GetMapping("set/project/administractors")
    public  CommonDto<String> setProjectAdministractors(Integer projectId,String administractorsId){
        CommonDto<String> result = new CommonDto<>();
        try {
            result=adminProjectService.creatProjectAdministractors(projectId,administractorsId);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setData(null);
            result.setStatus(502);
            result.setMessage("服务器端发生错误");
        }

        return result;
    }

}
