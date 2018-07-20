package com.lhjl.tzzs.proxy.controller.project.platform.details;

import com.lhjl.tzzs.proxy.controller.GenericController;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.LabelList;
import com.lhjl.tzzs.proxy.dto.ProjectTeamMemberDto.ProjectTeamMemberInputDto;
import com.lhjl.tzzs.proxy.dto.ProjectTeamMemberDto.ProjectTeamMemberOutputDto;
import com.lhjl.tzzs.proxy.model.ProjectTeamMember;
import com.lhjl.tzzs.proxy.service.EvaluateService;
import com.lhjl.tzzs.proxy.service.ProjectAdminTeamService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by lanhaijulang on 2018/1/18.
 */
@RestController
public class ProjectAdminTeamController extends  GenericController{

    @Resource
    private ProjectAdminTeamService projectAdminTeamService;

    /**
     * 获取平台团队成员列表
     * @param subjectId 主体id
     * @param subjectType 主体类型
     * @return
     */
    @GetMapping("/getprojectteammemberList")
    public CommonDto<List<ProjectTeamMemberOutputDto>> getprojectteammemberList(Integer subjectId,Integer subjectType){

        CommonDto<List<ProjectTeamMemberOutputDto>> result = new CommonDto<>();
        try {
            result = projectAdminTeamService.getProjectTeamMemberList(subjectId,subjectType);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(null);
            result.setStatus(502);
        }
        return result;
    }

    /**
     * 增加或更新团队成员信息
     * @param body
     * @return
     */
    @PostMapping("/addOrUpdatePojectTeamMember")
    public CommonDto<String> addOrUpdatePojectTeamMember(@RequestBody ProjectTeamMemberInputDto body){
        CommonDto<String> result = new CommonDto<>();
        try {
            result = projectAdminTeamService.addOrUpdatePojectTeamMember(body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(null);
            result.setStatus(502);
        }
        return result;
    }

    /**
     * 删除项目成员
     * @param memberId
     * @param subjectType
     * @return
     */
    @DeleteMapping("deleteprojectteammember")
    public CommonDto<String> deleteProjectTeamMember(Integer memberId,Integer subjectType){

        CommonDto<String> result = new CommonDto<>();
        try {
            result = projectAdminTeamService.deleteProjectTeamMember(memberId,subjectType);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setData(null);
            result.setStatus(502);
        }
        return result;
    }
    
    /**
     * 团队成员热点城市
     * @return
     */
    @GetMapping("gethotCity")
    public CommonDto<Map<String,List<LabelList>>> hotCity(){
        CommonDto<Map<String,List<LabelList>>> result = new CommonDto<>();
        try {
            result = projectAdminTeamService.queryHotCity();
        }catch (Exception e){
            result .setData(null);
            result.setStatus(510);
            result.setMessage("数据检索异常，请稍后再试");
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
        }
        return result;
    }

    /**
     * 城市智能检索
     * @param searchWord
     * @return
     */
    @GetMapping("city/elegant/search")
    public CommonDto<List<String>> cityElegantSearch(String searchWord){
        CommonDto<List<String>> result = new CommonDto<>();
        try {
            result = projectAdminTeamService.cityElegantSearch(searchWord);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setStatus(502);
            result.setData(null);
            result.setMessage("服务器端发生错误");
        }
        return result;
    }

    /**
     * 姓名智能检索
     * @param searchWord
     * @return
     */
    @GetMapping("user/elegant/search")
    public CommonDto<List<Map<String, Object>>> userElegantSearch(String searchWord){
        CommonDto<List<Map<String, Object>>> result = new CommonDto<>();
        try {
            result = projectAdminTeamService.userElegantSearch(searchWord);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setStatus(502);
            result.setData(null);
            result.setMessage("服务器端发生错误");
        }
        return result;
    }

}
