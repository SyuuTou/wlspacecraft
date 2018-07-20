package com.lhjl.tzzs.proxy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.lhjl.tzzs.proxy.dto.*;
import com.lhjl.tzzs.proxy.mapper.ProjectsMapper;
import com.lhjl.tzzs.proxy.model.*;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lhjl.tzzs.proxy.service.ProjectsService;
import com.lhjl.tzzs.proxy.service.UserInfoService;
import com.lhjl.tzzs.proxy.service.bluewave.UserLoginService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
/**
 *
 * @author lmy
 *
 */
@RestController
public class ProjectsController extends GenericController{

    @Resource
    private ProjectsService projectsService;
    
    @Value("${pageNum}")  
    private Integer defaultPageNum;

    @Value("${pageSize}")  
    private Integer defaultPageSize;
    /**
     * ZYY
     * 查询我关注项目
     * @return
     */
    @ApiOperation(value = "查询我关注的项目列表", notes = "根据用户ID标注请求的唯一")
    @PostMapping("search/myfollow")
    public CommonDto<List<Map<String, Object>>> findProjectByUserId(@RequestBody MyFollowDto body){

        CommonDto<List<Map<String, Object>>> result =new CommonDto<List<Map<String, Object>>>();
        //Users user = (Users) request.getSession().getAttribute("loging_user");
        
        try {
            //初始化分页信息
        	if (body.getPageNum() == null){
                body.setPageNum(defaultPageNum);
            }
            if (body.getPageSize() == null){
                body.setPageSize(defaultPageSize);
            }
            result = projectsService.findProjectByUserId(body.getUserId(), body.getPageNum(), body.getPageSize());
        } catch (Exception e) {
            result.setStatus(5101);
            result.setMessage("项目显示页面异常，请稍后再试");
            this.LOGGER.error(e.getMessage(),e);
        }
        return result;
    }
    /**
     * 搜索结果集显示三条记录
     * @return
     */
    @ApiOperation(value = "搜索结果记录", notes = "根据用户的id与项目的名字来标识请求的唯一性")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "body", dataType = "String", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "shortName", value = "项目的名字", required = true)
    })
    @PostMapping("search/Mprojects")
    public CommonDto<List<Map<String, Object>>> findProjectByShortName(@RequestBody ShuruDto body){
        CommonDto<List<Map<String,Object>>> result =new CommonDto<List<Map<String, Object>>>();
        String shortName=body.getShortName();
        String userId =body.getUserId();
        try {
            result = projectsService.findProjectByShortName(shortName,userId);
        } catch (Exception e) {
            result.setStatus(5102);
            result.setMessage("页面显示异常，请稍后再试");
            this.LOGGER.error(e.getMessage());
        }


        return result;

    }
    /**
     * 搜索结果项目/机构的更多

     * @return
     */
    @ApiOperation(value = "搜索醒目列表的接口", notes = "根据用户的id与项目的名字来标识请求的唯一性")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "body", dataType = "String", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "shortName", value = "项目的名字", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "pageNum", value = "初始页数", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "pageSize", value = "每页数量", required = true)
    })
    @PostMapping("search/pandi")
    public CommonDto<Map<String,List<Map<String, Object>>>> findProjectAndInvestmentByShortNameAll(@RequestBody ShuruDto body){
        CommonDto <Map<String,List<Map<String, Object>>>> result =new CommonDto <Map<String,List<Map<String, Object>>>>();
        String shortName =body.getShortName();
        String userId =body.getUserId();
        String size ="0";
        String from ="20";
        if(body.getPageNum() != null ){
            size=body.getPageNum();

        }
        if(body.getPageSize() != null){
            from=body.getPageSize();
        }

        try {
            result = projectsService.findProjectByShortNameAll(shortName,userId,size,from);
        } catch (Exception e) {
            result.setStatus(5103);
            result.setMessage("搜索异常，请稍后再试");
            this.LOGGER.error(e.getMessage());
        }
        return result;
    }
    /**
     * 筛选功能
     * @return
     */
    @ApiOperation(value = "筛选结果页面", notes = "根据用户的id、机构类型、领域、城市、轮次、教育背景、工作背景来标注请求的唯一")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "body", dataType = "String", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "Integer", name = "investment_institutions_type", value = "机构类型", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "segmentation", value = "行业领域", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "stage", value = "轮次", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "city", value = "城市", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "educational_background_desc", value = "教育背景", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "working_background_desc", value = "工作背景", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "pageNum", value = "初始页数", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "pageSize", value = "每页数量", required = true)
    })
    @PostMapping("choose/sview")
    public CommonDto<List<Map<String,Object>>>findProjectBySview(@RequestBody SereachDto body)
    {
        CommonDto<List<Map<String,Object>>> result =new CommonDto<>();
        try {
            result = projectsService.findProjectBySview(body);

        } catch (Exception e) {
            result.setStatus(5104);
            result.setMessage("页面显示异常，请稍后再试");
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
        }
        return result;
    }
    /**
     * 图表筛选功能
     * @return
     */
    @ApiOperation(value = "筛选结果页面", notes = "根据用户的id、机构类型、领域、城市、轮次、教育背景、工作背景来标注请求的唯一")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "body", dataType = "String", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "Integer", name = "investment_institutions_type", value = "机构类型", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "segmentation", value = "行业领域", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "stage", value = "轮次", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "city", value = "城市", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "educational_background_desc", value = "教育背景", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "working_background_desc", value = "工作背景", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "pageNum", value = "初始页数", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "pageSize", value = "每页数量", required = true)
    })
    @PostMapping("choose/sviewa")
    public CommonDto<List<Map<String,Object>>>findProjectBySviewA(@RequestBody SereachDto body)
    {
        CommonDto<List<Map<String,Object>>> result =new CommonDto<List<Map<String, Object>>>();
        try {
            result = projectsService.findProjectBySviewA(body);

        } catch (Exception e) {
            result.setStatus(5104);
            result.setMessage("页面显示异常，请稍后再试");
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
        }
        return result;
    }


    /**
     * 获取项目详情接口
     * @param body
     * @return
     */
    @PostMapping("get/project/details")
    public CommonDto<ProjectDetailOutputDto> getProjectDetails(@RequestBody Map<String,Object> body){
        CommonDto<ProjectDetailOutputDto> result  =new CommonDto<>();
        try {
            result = projectsService.getProjectDetails(body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setData(null);
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
        }
        return result;
    }

    /**
     * 获取项目当前融资信息
     * @param body
     * @return
     */

    @PostMapping("get/project/financing/now")
    public CommonDto<Map<String,Object>> getProgectFinancingNow(@RequestBody Map<String,Object> body){
        CommonDto<Map<String,Object>> result  = new CommonDto<>();
        try {
            result = projectsService.getProjectFinancingNow(body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setStatus(502);
            result.setMessage("服务器端发生错误");
            result.setData(null);
        }

        return result;
    }

    /**
     * 获取项目的历史融资信息
     * @param body
     * @return
     */
    @PostMapping("get/project/financing/history")
    public CommonDto<List<Map<String,Object>>> getProgectFinancingHistory(@RequestBody Map<String,Object> body){
        CommonDto<List<Map<String,Object>>> result  = new CommonDto<>();
        try {
            result = projectsService.getProjectFinancingHistory(body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setStatus(502);
            result.setMessage("服务器端发生错误");
            result.setData(null);
        }

        return result;
    }

    /**
     * 获取项目团队成员接口
     * @param body
     * @return
     */
    @PostMapping("get/project/financing/team")
    public CommonDto<List<Map<String,Object>>> getProgectFinancingTeam(@RequestBody Map<String,Object> body){
        CommonDto<List<Map<String,Object>>> result  = new CommonDto<>();
        try {
            result = projectsService.getProjectFinancingTeam(body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setStatus(502);
            result.setMessage("服务器端发生错误");
            result.setData(null);
        }

        return result;
    }


    /**
     * 获取项目竞品列表接口
     * @param body
     * @return
     */
    @PostMapping("get/project/competing")
    public CommonDto<List<Map<String,Object>>> getProgectCompeting(@RequestBody Map<String,Object> body){
        CommonDto<List<Map<String,Object>>> result  = new CommonDto<>();
        try {

        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setStatus(502);
            result.setMessage("服务器端发生错误");
            result.setData(null);
        }

        return result;
    }


    /**
     * 判断项目是否是我的 的接口
     * @param xmid 项目id
     * @param token 用户token
     * @return
     */
    @GetMapping("get/project/ismine")
    public CommonDto<Boolean> judgeProjectIsMine(Integer xmid,String token){
        CommonDto<Boolean> result = new CommonDto<>();
        try {
            result = projectsService.judgeProjectIsMine(xmid,token);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setStatus(502);
            result.setMessage("服务器端发生错误");
            result.setData(false);
        }

        return result;
    }

    /**
     * 获取项目管理员列表接口
     * @param body
     * @return
     */
    @PostMapping("project/administrator/list")
    public CommonDto<List<ProjectAdministratorOutputDto>> getProjectAdministratorList(@RequestBody Map<String,Integer> body){
        CommonDto<List<ProjectAdministratorOutputDto>> result = new CommonDto<>();
        List<ProjectAdministratorOutputDto> list = new ArrayList<>();

        try {
            result = projectsService.getProjectAdministractorList(body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
            result.setData(list);
        }

        return result;
    }

    /**
     * 通过项目id获取项目信息接口
     * @param body
     * @return
     */
    @PostMapping("project/complexinfo")
    public CommonDto<ProjectComplexOutputDto> getProjectComplexInfo(@RequestBody Map<String,Integer> body){
        CommonDto<ProjectComplexOutputDto> result =new CommonDto<>();

        try {
            result = projectsService.getProjectComplexInfo(body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setStatus(502);
            result.setMessage("服务器端发生错误");
            result.setData(null);
        }

        return result;
    }
}