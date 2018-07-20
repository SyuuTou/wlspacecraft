package com.lhjl.tzzs.proxy.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.github.pagehelper.PageHelper;
import com.lhjl.tzzs.proxy.dto.*;
import com.lhjl.tzzs.proxy.mapper.*;
import com.lhjl.tzzs.proxy.model.*;
import com.lhjl.tzzs.proxy.service.UserExistJudgmentService;
import com.lhjl.tzzs.proxy.service.UserInfoService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhjl.tzzs.proxy.service.GenericService;
import com.lhjl.tzzs.proxy.service.ProjectsService;
import tk.mybatis.mapper.entity.Example;

/**
 * 项目
 *
 * @author lmy
 */
@Service
public class ProjectsServiceImpl extends GenericService implements ProjectsService {

    @Value("${statistics.beginTime}")
    private String beginTime;

    @Value("${statistics.endTime}")
    private String endTime;
    
    @Value("${pageNum}")
    private Integer pageNumDefault;
    
    @Value("${pageSize}")
    private Integer pageSizeDefault;
    
    @Resource
    private ProjectsMapper projectsMapper;
    @Resource
    private InvestmentInstitutionsMapper investmentInstitutionsMapper;
    @Autowired
    private Environment environment;
    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private InterviewMapper interviewMapper;

    @Resource
    private ProjectsServiceImplUtil projectsServiceImplUtil;

    @Autowired
    private ProjectFinancingLogMapper projectFinancingLogMapper;

    @Autowired
    private ProjectFinancingHistoryInvestorsMapper projectFinancingHistoryInvestorsMapper;

    @Autowired
    private ProjectTeamMemberMapper projectTeamMemberMapper;

    @Autowired
    private ProjectTeamMemberEducationMapper projectTeamMemberEducationMapper;

    @Autowired
    private ProjectTeamMemberWorkMapper projectTeamMemberWorkMapper;

    @Autowired
    private ProjectSegmentationMapper projectSegmentationMapper;

    @Resource
    private UserExistJudgmentService userExistJudgmentService;

    @Autowired
    private ProjectAdministratorMapper projectAdministratorMapper;

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private MetaFollowStatusMapper metaFollowStatusMapper;
    @Autowired
    private ProjectFollowStatusMapper projectFollowStatusMapper;
    @Autowired
    private MetaDataSourceTypeMapper metaDataSourceTypeMapper;
    @Autowired
    private AdminProjectRatingLogMapper adminProjectRatingLogMapper;
    @Autowired
    private InvestmentInstitutionsProjectMapper investmentInstitutionsProjectMapper;
    @Autowired
    private InvestmentInstitutionsAddressPartMapper investmentInstitutionsAddressPartMapper;
    @Autowired
    private InvestmentInstitutionsAddressMapper investmentInstitutionsAddressMapper;
    @Autowired
    private MetaJobTypeMapper metaJobTypeMapper;
    @Autowired
    private RecruitmentMapper recruitmentMapper;
    @Autowired
    private RecruitmentInfoMapper recruitmentInfoMapper;
    @Autowired
    private ProjectProgressMapper projectProgressMapper;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private DatasOperationManageMapper datasOperationManageMapper;
    
    /**
     * 查询我关注的项目
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public CommonDto<List<Map<String, Object>>> findProjectByUserId(String userId, Integer pageNum, Integer pageSize) {
        CommonDto<List<Map<String, Object>>> result = new CommonDto<List<Map<String, Object>>>();

        //计算查询起始记录
        Integer beginNum = (pageNum - 1) * pageSize;
        //最多返回100条记录
        if (beginNum > 100) {
            result.setStatus(201);
            result.setMessage("查询记录数超出限制（100条）");
            return result;
        } else {
            pageSize = (100 - beginNum) >= pageSize ? pageSize : (100 - beginNum);
        }

        List<Map<String, Object>> list = projectsMapper.findProjectByUserId(userId, beginNum, pageSize);
        //List<Map<String, Object>> list =projectsServiceImplUtil.getSearchBaseMyProject(userId,beginNum,pageSize);
        Follow follow = new Follow();
        Interview interview = new Interview();
        for (Map<String, Object> obj : list) {
            follow.setProjectsId(Integer.valueOf(String.valueOf(obj.get("ID"))));
            follow.setStatus(1);
            follow.setUserId(null);
            obj.put("num", followMapper.selectCount(follow));
            follow.setUserId(userId);
            if (followMapper.selectCount(follow) > 0) {
                obj.put("yn", 1);
            } else {
                obj.put("yn", 0);
            }

            interview.setProjectsId(Integer.valueOf(String.valueOf(obj.get("ID"))));
            obj.put("inum", interviewMapper.selectCount(interview));
        }

        //判断是否还有查询结果
        if (list.size() <= 0) {
            result.setStatus(202);
            result.setMessage("无查询数据");
            return result;
        }
        result.setData(list);
        result.setStatus(200);
        result.setMessage("success");
        return result;
    }

    /**
     * 搜索结果页面值搜查出三条记录
     *
     * @param shortName
     * @param userId
     * @return
     */
    @Override
    public CommonDto<List<Map<String, Object>>> findProjectByShortName(String shortName, String userId) {
        CommonDto<List<Map<String, Object>>> result = new CommonDto<List<Map<String, Object>>>();
        List<Map<String, Object>> list = projectsMapper.findProjectByShortName(shortName, userId);
        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
        Follow follow = new Follow();
        Interview interview = new Interview();
        for (Map<String, Object> obj : list) {
            follow.setProjectsId(Integer.valueOf(String.valueOf(obj.get("ID"))));
            follow.setStatus(1);
            follow.setUserId(null);
            obj.put("num", followMapper.selectCount(follow));
            follow.setUserId(userId);
            if (followMapper.selectCount(follow) > 0) {
                obj.put("yn", 1);
            } else {
                obj.put("yn", 0);
            }

            interview.setProjectsId(Integer.valueOf(String.valueOf(obj.get("ID"))));
            obj.put("inum", interviewMapper.selectCount(interview));
        }
        for (Map<String, Object> map : list) {
            if (map.get("yn") == null) {
                map.put("yn", 0);
            }
            list2.add(map);
        }
        result.setData(list2);
        result.setStatus(200);
        result.setMessage("ok");
        return result;
    }

    /**
     * 搜索更多结果
     *
     * @param shortName
     * @param userId
     * @param size
     * @param from
     * @return
     */
    @Override
    public CommonDto<Map<String, List<Map<String, Object>>>> findProjectByShortNameAll(String shortName, String userId, String size, String from) {
        CommonDto<Map<String, List<Map<String, Object>>>> result = new CommonDto<Map<String, List<Map<String, Object>>>>();
        int sizeb = Integer.parseInt(size);
        int froma = Integer.parseInt(from);
        int sizea = (sizeb - 1) * froma;
        if (sizea > 100) {
            result.setStatus(201);
            result.setMessage("查询记录数超出限制（100条）");
            return result;
        }
        {
            froma = (100 - sizea) >= froma ? froma : (100 - sizea);
        }
        Map<String, List<Map<String, Object>>> dataMap = new HashMap<String, List<Map<String, Object>>>();
        //List<Map<String, Object>> list = projectsMapper.findProjectByShortNameAll(shortName,userId,sizea,froma);
        List<Map<String, Object>> list = projectsServiceImplUtil.getSearchBaseProjectAll(userId, shortName, sizea, froma);
        Follow follow = new Follow();
        Interview interview = new Interview();
        for (Map<String, Object> obj : list) {
            follow.setProjectsId(Integer.valueOf(String.valueOf(obj.get("ID"))));
            follow.setStatus(1);
            follow.setUserId(null);
            obj.put("num", followMapper.selectCount(follow));
            follow.setUserId(userId);
            if (followMapper.selectCount(follow) > 0) {
                obj.put("yn", 1);
            } else {
                obj.put("yn", 0);
            }

            interview.setProjectsId(Integer.valueOf(String.valueOf(obj.get("ID"))));
            obj.put("inum", interviewMapper.selectCount(interview));
        }
        if (list.size() <= 0) {
            result.setStatus(202);
            result.setMessage("无查询数据");
            return result;
        }
        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> map : list) {
            if (map.get("yn") == null) {
                map.put("yn", 0);
            }

            list2.add(map);
        }
       /* List<Map<String, Object>> list3 = investmentInstitutionsMapper.findByInvestmentShortNameAll(shortName,userId);
        dataMap.put("projectskey", list2);
        dataMap.put("investmentkey", list3);*/
        dataMap.put("projectskey", list2);
        result.setData(dataMap);
        result.setStatus(200);
        result.setMessage("ok");
        return result;
    }


    /**
     * 项目筛选
     *
     * @param sereachDto 筛选条件
     * @return
     */
    @Override
    public CommonDto<List<Map<String, Object>>> findProjectBySview(SereachDto sereachDto) {
        CommonDto<List<Map<String, Object>>> result = new CommonDto<List<Map<String, Object>>>();

        String userId = sereachDto.getUserId();
        String type = sereachDto.getInvestment_institutions_type();
        String segmentation = sereachDto.getSegmentation();
        String stage = sereachDto.getStage();
        String city = sereachDto.getCity();
        String working_background_desc = sereachDto.getWorking_background_desc();
        String educational_background_desc = sereachDto.getEducational_background_desc();

        String size = "0";
        String from = "20";
        if (sereachDto.getPageNum() != null && !"".equals(sereachDto.getPageNum())) {
            size = sereachDto.getPageNum();
        }
        if (sereachDto.getPageSize() != null && !"".equals(sereachDto.getPageSize())) {
            from = sereachDto.getPageSize();
        }
        int sizeb = Integer.parseInt(size);
        int froma = 20;
        if (null != from) {
            froma = Integer.parseInt(from);
        }
        int sizea = (sizeb - 1) * froma;
        if (sizea > 100) {
            result.setStatus(201);
            result.setMessage("查询记录数超出限制（100条）");
            return result;
        }else
        {
            froma = (100 - sizea) >= froma ? froma : (100 - sizea);
        }

//	    List citiess = Arrays.asList(cities);
//	        String[] stages = sereachDto.getStage().split(",");

        //查询项目基础数据
        List<Map<String, Object>> list = projectsServiceImplUtil.getBaseProjectInfo(type, segmentation, stage,
                city, working_background_desc, educational_background_desc, sizea, froma);

        //查询项目实时统计数据
        Follow follow = new Follow();
        Interview interview = new Interview();
        for (Map<String, Object> obj : list) {
            follow.setProjectsId(Integer.valueOf(String.valueOf(obj.get("ID"))));
            follow.setStatus(1);
            follow.setUserId(null);
            obj.put("num", followMapper.selectCount(follow));
            follow.setUserId(userId);
            if (followMapper.selectCount(follow) > 0) {
                obj.put("yn", 1);
            } else {
                obj.put("yn", 0);
            }

            interview.setProjectsId(Integer.valueOf(String.valueOf(obj.get("ID"))));
            obj.put("inum", interviewMapper.selectCount(interview));
        }
        if (list.size() <= 0) {
            result.setStatus(202);
            result.setMessage("无查询数据");
            return result;
        }
        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            if (map.get("yn") == null) {
                map.put("yn", 0);
            }
            list2.add(map);
        }
        result.setData(list2);
        result.setStatus(200);
        result.setMessage("success");
        return result;
    }

    /**
     * 图表项目筛选
     *
     * @param sereachDto 筛选条件
     * @return
     */
    @Override
    public CommonDto<List<Map<String, Object>>> findProjectBySviewA(SereachDto sereachDto) {
        CommonDto<List<Map<String, Object>>> result = new CommonDto<List<Map<String, Object>>>();

        try {
            String userId = sereachDto.getUserId();
            String type = sereachDto.getInvestment_institutions_type();
            String segmentation = sereachDto.getSegmentation();
            String stage = sereachDto.getStage();
            String city = sereachDto.getCity();
            String working_background_desc = sereachDto.getWorking_background_desc();
            String educational_background_desc = sereachDto.getEducational_background_desc();

            String size = "0";
            String from = "10";
            if (sereachDto.getPageNum() != null && !"".equals(sereachDto.getPageNum())) {
                size = sereachDto.getPageNum();
            }
            if (sereachDto.getPageSize() != null && !"".equals(sereachDto.getPageSize())) {
                from = sereachDto.getPageSize();
            }
            int sizeb = 0;
            if (null != size) {
                sizeb = Integer.parseInt(size);
            }

            int froma = 10;
            if (null != from) {
                froma = Integer.parseInt(from);
            }
            int sizea = (sizeb - 1) * froma;
            if (sizea > 100) {
                result.setStatus(201);
                result.setMessage("查询记录数超出限制（100条）");
                return result;
            }else
            {
                froma = (100 - sizea) >= froma ? froma : (100 - sizea);
            }

//	    List citiess = Arrays.asList(cities);
//	        String[] stages = sereachDto.getStage().split(",");

            //查询项目基础数据
            List<Map<String, Object>> list = projectsServiceImplUtil.getBaseProjectInfoA( type, segmentation, stage,
                    city, working_background_desc, educational_background_desc, sizea, froma, endTime, beginTime);

            //查询项目实时统计数据
            Follow follow = new Follow();
            Interview interview = new Interview();
            for (Map<String, Object> obj : list) {
                follow.setProjectsId(Integer.valueOf(String.valueOf(obj.get("ID"))));
                follow.setStatus(1);
                follow.setUserId(null);
                obj.put("num", followMapper.selectCount(follow));
                follow.setUserId(userId);
                if (followMapper.selectCount(follow) > 0) {
                    obj.put("yn", 1);
                } else {
                    obj.put("yn", 0);
                }

                interview.setProjectsId(Integer.valueOf(String.valueOf(obj.get("ID"))));
                obj.put("inum", interviewMapper.selectCount(interview));
            }
            if (list.size() <= 0) {
                result.setStatus(202);
                result.setMessage("无查询数据");
                return result;
            }
            List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> map : list) {
                if (map.get("yn") == null) {
                    map.put("yn", 0);
                }
                list2.add(map);
            }
            result.setData(list2);
            result.setStatus(200);
            result.setMessage("success");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取项目详情
     * @param body
     * @return
     */
    @Override
    public CommonDto<ProjectDetailOutputDto> getProjectDetails(Map<String,Object> body){
        CommonDto<ProjectDetailOutputDto> result =new CommonDto<>();
        ProjectDetailOutputDto projectDetailOutputDto = new ProjectDetailOutputDto();

        if (body.get("projectId") == null || "".equals(body.get("projectId"))){
            result.setStatus(50001);
            result.setMessage("项目id不能为空");
            result.setData(null);

            return result;
        }

        if (body.get("token") == null || "".equals(body.get("token"))){
            result.setMessage("用户token不能为空");
            result.setData(null);
            result.setStatus(50001);

            return result;
        }

        Integer xmid = (Integer) body.get("projectId");

        Projects projects = projectsMapper.selectByPrimaryKey(xmid);

        //查不到项目报错就
        if (projects == null ){
            result.setMessage("没有找到项目信息，请检查项目id");
            result.setData(null);
            result.setStatus(50001);

            return result;
        }
        //获取项目融资阶段
        Example projectFinancingLogExample = new Example(ProjectFinancingLog.class);
        projectFinancingLogExample.and().andEqualTo("projectId",xmid).andEqualTo("status",1);
        projectFinancingLogExample.setOrderByClause("financing_time DESC");

        String financingStage = "";

       List<ProjectFinancingLog> projectFinancingLogList = projectFinancingLogMapper.selectByExample(projectFinancingLogExample);
       //看看现在的融资历史是否有，有的话拿到融资阶段
       if (projectFinancingLogList.size()  >  0){
        financingStage = projectFinancingLogList.get(0).getStage();
       }else{
           Example projectFinancingLogListOne = new Example(ProjectFinancingLog.class);
           projectFinancingLogListOne.and().andEqualTo("projectId",xmid);
           projectFinancingLogListOne.setOrderByClause("financing_time DESC");

            List<ProjectFinancingLog> projectFinancingLogList1 = projectFinancingLogMapper.selectByExample(projectFinancingLogListOne);
            if (projectFinancingLogList1.size() > 0){
                financingStage = projectFinancingLogList1.get(0).getStage();
            }
       }



        //获取项目的领域
        String field = "";
        Example projectSegExample = new Example(ProjectSegmentation.class);
        projectSegExample.and().andEqualTo("projectId",xmid);
        PageHelper pageHelper = new PageHelper();
        PageHelper.startPage(0,3);

        List<ProjectSegmentation> projectSegmentationList = projectSegmentationMapper.selectByExample(projectSegExample);
        if (projectSegmentationList.size() > 0){
            for (ProjectSegmentation ps:projectSegmentationList){
                field += ps.getSegmentationName();
                field += " ";
            }
        }

        //设置数据
        projectDetailOutputDto.setField(field);
        projectDetailOutputDto.setFinancingStage(financingStage);
        if (projects.getShortName() == null){
            projects.setShortName("");
        }
        projectDetailOutputDto.setShortName(projects.getShortName());
        if (projects.getFullName() == null){
            projects.setFullName("");
        }
        projectDetailOutputDto.setFullName(projects.getFullName());
        if (projects.getKernelDesc() == null){
            projects.setKernelDesc("");
        }
        projectDetailOutputDto.setKernelDesc(projects.getKernelDesc());
        if (projects.getCommet() == null){
            projects.setCommet("");
        }
        projectDetailOutputDto.setCommet(projects.getCommet());
        if (projects.getUrl() == null){
            projects.setUrl("");
        }
        projectDetailOutputDto.setUrl(projects.getUrl());
        if (projects.getCity() == null){
            projects.setCity("");
        }
        projectDetailOutputDto.setTerritory(projects.getCity());
        if (projects.getProjectLogo() == null){
            projects.setProjectLogo("");
        }
        projectDetailOutputDto.setProjectLogo(projects.getProjectLogo());
        if (projects.getProjectInvestmentHighlights() == null){
            projects.setProjectInvestmentHighlights("");
        }
        projectDetailOutputDto.setProjectInvestmentHighlights(projects.getProjectInvestmentHighlights());



        result.setStatus(200);
        result.setMessage("success");
        result.setData(projectDetailOutputDto);

        return result;
    }

    /**
     * 获取项目当前融资信息
     * @param body
     * @return
     */
    @Override
    public CommonDto<Map<String,Object>> getProjectFinancingNow(Map<String,Object> body){
        CommonDto<Map<String,Object>> result = new CommonDto<>();
        Map<String,Object> obj = new HashMap<>();

        if (body.get("projectId") == null || "".equals(body.get("projectId"))){
            result.setStatus(50001);
            result.setMessage("项目id不能为空");
            result.setData(null);

            return result;
        }

        if (body.get("token") == null || "".equals(body.get("token"))){
            result.setMessage("用户token不能为空");
            result.setData(null);
            result.setStatus(50001);

            return result;
        }

        Integer xmid = (Integer) body.get("projectId");

        ProjectFinancingLog projectFinancingLogForSearch = new ProjectFinancingLog();
        projectFinancingLogForSearch.setProjectId(xmid);
        projectFinancingLogForSearch.setStatus(1);

        List<ProjectFinancingLog> projectFinancingLogList = projectFinancingLogMapper.select(projectFinancingLogForSearch);
        if (projectFinancingLogList.size() > 0){
            ProjectFinancingLog projectFinancingLog = projectFinancingLogList.get(0);
            obj.put("hasData",true);
            String stage = "";
            if (projectFinancingLog.getStage() != null){
                stage = projectFinancingLog.getStage();
            }
            obj.put("stage",stage);

            obj.put("amount",projectFinancingLog.getAmount());
            String stockRight ="未知";
            if (projectFinancingLog.getStockRight() != null){
                BigDecimal bigDecimal =projectFinancingLog.getStockRight().setScale(2,BigDecimal.ROUND_DOWN);
                stockRight = String.valueOf(bigDecimal) + "%";
            }
            obj.put("stockRight",stockRight);
            String projectFinancingUseful = "";
            if (projectFinancingLog.getProjectFinancingUseful() != null){
                projectFinancingUseful = projectFinancingLog.getProjectFinancingUseful();
            }
            obj.put("projectFinancingUseful",projectFinancingUseful);

            String currency = "人民币";
            if (projectFinancingLog.getCurrency() != null){
                if (projectFinancingLog.getCurrency() == 1){
                    currency = "美元";
                }
            }
            obj.put("currency",currency);

            result.setData(obj);
            result.setMessage("success");
            result.setStatus(200);
        } else {
            obj.put("hasData",false);
            obj.put("stage","");
            obj.put("amount","");
            obj.put("stockRight","");
            obj.put("projectFinancingUseful","");
            obj.put("currency","");

            result.setData(obj);
            result.setMessage("success");
            result.setStatus(200);
        }

        return result;
    }

    /**
     * 获取项目历史融资信息
     * @param body
     * @return
     */
    @Override
    public CommonDto<List<Map<String,Object>>> getProjectFinancingHistory(Map<String,Object> body){
        CommonDto<List<Map<String,Object>>> result = new CommonDto<>();
        List<Map<String,Object>> list = new ArrayList<>();



        if (body.get("projectId") == null || "".equals(body.get("projectId"))){
            result.setStatus(50001);
            result.setMessage("项目id不能为空");
            result.setData(null);

            return result;
        }

        if (body.get("token") == null || "".equals(body.get("token"))){
            result.setMessage("用户token不能为空");
            result.setData(null);
            result.setStatus(50001);

            return result;
        }

        Integer xmid = (Integer) body.get("projectId");



        List<ProjectFinancingLog> projectFinancingLogList = projectFinancingLogMapper.selectProjectFinancingLogList(xmid,2);
        //如果融资历史有就返回
        if (projectFinancingLogList.size() > 0){
            for (ProjectFinancingLog pfl:projectFinancingLogList){
                Map<String,Object> obj = new HashMap<>();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                String dateString ="";
                if (pfl.getFinancingTime() != null){
                    dateString = simpleDateFormat.format(pfl.getFinancingTime());
                }

                //获取投资方姓名
                Integer pflid = pfl.getId();
//                ProjectFinancingHistoryInvestors projectFinancingHistoryInvestors = new ProjectFinancingHistoryInvestors();
//                projectFinancingHistoryInvestors.setProjectFinancingHistoryId(pflid);
//
//                List<ProjectFinancingHistoryInvestors> projectFinancingHistoryInvestorsList = projectFinancingHistoryInvestorsMapper.select(projectFinancingHistoryInvestors);
//                List<String> touzifang= new ArrayList<>();
//                if (projectFinancingHistoryInvestorsList.size() > 0){
//                        for (ProjectFinancingHistoryInvestors pfhi:projectFinancingHistoryInvestorsList){
//                            touzifang.add(pfhi.getInvestorName());
//                        }
//                }

                List<String> projectFinancingLogListFor = projectFinancingLogMapper.selectInvestors(pflid);

                obj.put("financingTime",dateString);
                obj.put("stage",pfl.getStage());
                obj.put("touzifang",projectFinancingLogListFor);

                list.add(obj);
            }

            result.setData(list);
            result.setMessage("success");
            result.setStatus(200);
        }else {
            result.setStatus(200);
            result.setMessage("success");
            result.setData(list);
        }


        return result;
    }

    /**
     * 获取团队成员接口
     * @param body
     * @return
     */
    @Override
    public CommonDto<List<Map<String,Object>>> getProjectFinancingTeam(Map<String,Object> body){
        CommonDto<List<Map<String,Object>>> result = new CommonDto<>();
        List<Map<String,Object>> obj = new ArrayList<>();

        if (body.get("projectId") == null || "".equals(body.get("projectId"))){
            result.setStatus(50001);
            result.setMessage("项目id不能为空");
            result.setData(null);

            return result;
        }

        if (body.get("token") == null || "".equals(body.get("token"))){
            result.setMessage("用户token不能为空");
            result.setData(null);
            result.setStatus(50001);

            return result;
        }

        Integer xmid = (Integer) body.get("projectId");

        ProjectTeamMember projectTeamMember =new ProjectTeamMember();
        projectTeamMember.setProjectId(xmid);
        projectTeamMember.setYn(0);


        List<ProjectTeamMember> projectTeamMemberList = projectTeamMemberMapper.select(projectTeamMember);
        if (projectTeamMemberList.size() > 0){

            for (ProjectTeamMember ptm:projectTeamMemberList){
                Map<String,Object> map = new HashMap<>();

                //获取教育经历
                ProjectTeamMemberEducation projectTeamMemberEducation = new ProjectTeamMemberEducation();
                projectTeamMemberEducation.setProjectTeamMemberId(ptm.getId());
                StringBuffer jiaoyujingli = new StringBuffer();
                StringBuffer gongzuojingli = new StringBuffer();

                List<ProjectTeamMemberEducation> projectTeamMemberEducationList = projectTeamMemberEducationMapper.select(projectTeamMemberEducation);
                if (projectTeamMemberEducationList.size() > 0){
                    if (projectTeamMemberEducationList.size() > 3){
                        for (int j = 0;j<2;j++){
                            jiaoyujingli.append(projectTeamMemberEducationList.get(j).getEducationExperience());
                            jiaoyujingli.append(" ");
                        }
                    }else {
                        for (ProjectTeamMemberEducation ptme:projectTeamMemberEducationList){
                            jiaoyujingli.append(ptme.getEducationExperience());
                            jiaoyujingli.append(" ");
                        }
                    }
                }


                //获取工作经历
                ProjectTeamMemberWork projectTeamMemberWork = new ProjectTeamMemberWork();
                projectTeamMemberWork.setProjectTeamMemberId(ptm.getId());

                List<ProjectTeamMemberWork> projectTeamMemberWorkList = projectTeamMemberWorkMapper.select(projectTeamMemberWork);
                if (projectTeamMemberWorkList.size() > 0){
                    if (projectTeamMemberWorkList.size() > 3){
                        for (int k = 0;k<2;k++){
                            gongzuojingli.append(projectTeamMemberWorkList.get(k).getWorkExperience());
                            gongzuojingli.append(" ");
                        }
                    }else {
                        for (ProjectTeamMemberWork ptmw:projectTeamMemberWorkList){
                            gongzuojingli.append(ptmw.getWorkExperience());
                            gongzuojingli.append(" ");
                        }
                    }
                }

                //组合数据返回前端
                map.put("mumberName",ptm.getMumberName());
                map.put("mumberDuties",ptm.getMumberDuties());
                map.put("jiaoyujingli",jiaoyujingli);
                map.put("gongzuojingli",gongzuojingli);
                map.put("shareRato",ptm.getShareRatio());
                map.put("mumberDesc",ptm.getMumberDesc());

                obj.add(map);

                result.setMessage("success");
                result.setStatus(200);
                result.setData(obj);

            }
        }else{
            result.setData(obj);
            result.setStatus(200);
            result.setMessage("success");
        }

        return result;
    }

    @Override
    public CommonDto<List<Map<String,Object>>> getProjectCompeting(Map<String,Object> body){
        CommonDto<List<Map<String,Object>>> result = new CommonDto<>();
        List<Map<String,Object>> list = new ArrayList<>();

        if (body.get("projectId") == null || "".equals(body.get("projectId"))){
            result.setStatus(50001);
            result.setMessage("项目id不能为空");
            result.setData(null);

            return result;
        }

        if (body.get("token") == null || "".equals(body.get("token"))){
            result.setMessage("用户token不能为空");
            result.setData(null);
            result.setStatus(50001);

            return result;
        }

        Integer xmid = (Integer) body.get("projectId");

        Projects projects = projectsMapper.selectByPrimaryKey(xmid);

        //String lunci = projects
        return result;
    }

    /**
     * 判断项目是否是我的 的接口
     * @param xmid 项目id
     * @param token 用户token
     * @return
     */
    @Override
    public CommonDto<Boolean> judgeProjectIsMine(Integer xmid,String token){
    CommonDto<Boolean> result  = new CommonDto<>();
    Integer userid = userExistJudgmentService.getUserId(token);
    if (userid == -1){
        result.setMessage("用户token非法，请检查");
        result.setData(false);
        result.setStatus(502);

        return result;
    }

    Projects projects = projectsMapper.selectByPrimaryKey(xmid);
    if (projects == null){
        result.setMessage("没有找到当前项目id的项目，请检查");
        result.setStatus(502);
        result.setData(false);

        return result;
    }else{
        Integer userIdForDuibi = projects.getUserid();
        if (userid.equals(userIdForDuibi)){
            result.setData(true);
            result.setStatus(200);
            result.setMessage("success");
        }else {
            result.setMessage("项目和用户不匹配");
            result.setStatus(502);
            result.setData(false);
        }
    }


    return result;
    }

    /**
     * 获取项目管理员列表接口
     * @param body
     * @return
     */
    @Override
    public CommonDto<List<ProjectAdministratorOutputDto>> getProjectAdministractorList(Map<String,Integer> body){
        CommonDto<List<ProjectAdministratorOutputDto>> result = new CommonDto<>();
        List<ProjectAdministratorOutputDto> list = new ArrayList<>();

        if (body.get("projectId") == null){
            result.setData(list);
            result.setStatus(50001);
            result.setMessage("项目id不能为空，请检查");

            return result;
        }


        //先获取到项目信息中的简称
        Projects projects = projectsMapper.selectByPrimaryKey(body.get("projectId"));

        if (projects == null){
            result.setMessage("没找到该项目id对应的项目，请检查");
            result.setStatus(50001);
            result.setData(list);

            return result;
        }

        String projectName = projects.getShortName();

        //拿项目id去查项目的管理员
        ProjectAdministrator projectAdministrator = new ProjectAdministrator();
        projectAdministrator.setProjectsId(body.get("projectId"));
        projectAdministrator.setYn(0);

        List<ProjectAdministrator> projectAdministratorList = projectAdministratorMapper.select(projectAdministrator);

        if (projectAdministratorList.size() > 0){
            //获取项目管理员的信息并加入到list中去
            for (ProjectAdministrator pa:projectAdministratorList){
                //获取管理员信息
                Map<String,String> users = usersMapper.findUserInfoAssemble(pa.getUserId());
                if (users != null){
                    //获取到管理员的oppenid,token,真实姓名，昵称，头像

                    String userOppenid =users.get("openId");
                    String userToken = users.get("token");
                    String userNickName = users.get("nick_name");
                    String userRealName = users.get("actual_name");
                    String userHeadpic = "";
                    if (users.get("headpic_real") == null || "".equals(users.get("headpic_real"))){
                        userHeadpic = users.get("headpic");
                    }else {
                        userHeadpic = users.get("headpic_real");
                    }

                    String companyDuties = "";
                    if (users.get("company_duties") != null){
                        companyDuties=users.get("company_duties");
                    }

                    //放到返回值里
                    ProjectAdministratorOutputDto projectAdministratorOutputDto= new  ProjectAdministratorOutputDto();
                    projectAdministratorOutputDto.setCompanyName(projectName);
                    projectAdministratorOutputDto.setHeadpic(userHeadpic);
                    projectAdministratorOutputDto.setNickName(userNickName);
                    projectAdministratorOutputDto.setRealName(userRealName);
                    projectAdministratorOutputDto.setToken(userToken);
                    projectAdministratorOutputDto.setCompanyDuties(companyDuties);

                    list.add(projectAdministratorOutputDto);
                }

            }

            result.setData(list);
            result.setStatus(200);
            result.setMessage("success");
        }else {
            result.setMessage("该项目没有管理员");
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
    @Override
    public CommonDto<ProjectComplexOutputDto> getProjectComplexInfo(Map<String,Integer> body){
        CommonDto<ProjectComplexOutputDto> result = new CommonDto<>();

        if (body.get("projectId") == null){
            result.setData(null);
            result.setMessage("项目id不能为空");
            result.setStatus(50001);

            this.LOGGER.info("通过项目id获取项目信息接口场景");
            this.LOGGER.info("项目id不能为空");

            return result;
        }

        Projects projects = projectsMapper.selectByPrimaryKey(body.get("projectId"));
        if (projects == null){
            result.setStatus(50001);
            result.setMessage("当前项目id没有找到项目信息，请检查项目id");
            result.setData(null);

            this.LOGGER.info("通过项目id获取项目信息接口场景");
            this.LOGGER.info("当前项目id没有找到项目信息，请检查项目id");

            return result;
        }

        Integer projectType = 0;
        if( projects.getProjectSource() != null ){
            projectType = 1;
        }

        Integer xmid = body.get("projectId");
        //获取项目融资阶段
        Example projectFinancingLogExample = new Example(ProjectFinancingLog.class);
        projectFinancingLogExample.and().andEqualTo("projectId",xmid).andEqualTo("status",1);
        projectFinancingLogExample.setOrderByClause("create_time DESC");

        String financingStage = "";

        List<ProjectFinancingLog> projectFinancingLogList = projectFinancingLogMapper.selectByExample(projectFinancingLogExample);
        //看看现在的融资历史是否有，有的话拿到融资阶段
        if (projectFinancingLogList.size()  >  0){
            financingStage = projectFinancingLogList.get(0).getStage();
        }else{
            Example projectFinancingLogListOne = new Example(ProjectFinancingLog.class);
            projectFinancingLogListOne.and().andEqualTo("projectId",xmid);
            projectFinancingLogListOne.setOrderByClause("create_time DESC");

            List<ProjectFinancingLog> projectFinancingLogList1 = projectFinancingLogMapper.selectByExample(projectFinancingLogListOne);
            if (projectFinancingLogList1.size() > 0){
                financingStage = projectFinancingLogList1.get(0).getStage();
            }
        }



        //获取项目的领域
        String field = "";
        Example projectSegExample = new Example(ProjectSegmentation.class);
        projectSegExample.and().andEqualTo("projectId",xmid);
        PageHelper pageHelper = new PageHelper();
        PageHelper.startPage(0,3);
        String fieldEdit = "";

        List<ProjectSegmentation> projectSegmentationList = projectSegmentationMapper.selectByExample(projectSegExample);
        if (projectSegmentationList.size() > 0){
            for (ProjectSegmentation ps:projectSegmentationList){
                field += ps.getSegmentationName();
                field += " ";
            }
            fieldEdit = field.substring(0,field.length()-1);
        }


        ProjectComplexOutputDto projectComplexOutputDto =new ProjectComplexOutputDto();
        projectComplexOutputDto.setProjectDesc(projects.getKernelDesc());
        projectComplexOutputDto.setProjectId(body.get("projectId"));
        String logo = "";
        if (projects.getProjectLogo() != null){
            logo = projects.getProjectLogo();
        }
        projectComplexOutputDto.setProjectLogo(logo);
        projectComplexOutputDto.setProjectShortName(projects.getShortName());
        projectComplexOutputDto.setSegmentation(fieldEdit);
        if (projects.getCity() == null){
            projects.setCity("");
        }
        projectComplexOutputDto.setCity(projects.getCity());
        projectComplexOutputDto.setRound(financingStage);
        projectComplexOutputDto.setProjectType(projectType);

        String projectInvestmentHighlights = "";
        if (projects.getProjectInvestmentHighlights() != null){
            projectInvestmentHighlights = projects.getProjectInvestmentHighlights();
        }
        projectComplexOutputDto.setProjectInvestmentHighlights(projectInvestmentHighlights);


        result.setData(projectComplexOutputDto);
        result.setMessage("success");
        result.setStatus(200);

        return result;
    }
    
    @Transactional(readOnly = true) 
	@Override
	public CommonDto<PagingOutputDto<ProjectsListOutputDto>> listProInfos(Integer appid, ProjectsListInputDto body) {
		CommonDto<PagingOutputDto<ProjectsListOutputDto>> result=new CommonDto<>();
		PagingOutputDto<ProjectsListOutputDto> pod =new PagingOutputDto<>();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//输入参数格式化
		if(body.getCurrentPage()==null) {
			body.setCurrentPage(pageNumDefault);
		}
		if(body.getPageSize()==null) {
			body.setPageSize(pageSizeDefault);
		}
		body.setStart((long)(body.getCurrentPage()-1) * body.getPageSize());
		
		List<ProjectsListOutputDto> list = projectsMapper.findSplit(body);
		Long total = projectsMapper.findSplitCount(body);
		
		//格式化输出的数据结构
		//设置创建时间，更新时间输出字符串格式
		list.forEach((e)->{  
			//设置提交时间
			if(e.getCreateTime()!=null) {  
				e.setCreateTimeOutputStr(sdf.format(e.getCreateTime()));
			}
			//设置更新时间,更新时间为null的时候更新时间为提交时间
			if(e.getUpdateTime()!=null) {
				e.setUpdateTimeOutputStr(sdf.format(e.getUpdateTime()));
				//更新时间设置为提交时间
			}else {
				e.setUpdateTimeOutputStr(e.getCreateTimeOutputStr());
			}
			
		});
		
		pod.setList(list);
		pod.setTotal(total);
		pod.setCurrentPage(body.getCurrentPage());
		pod.setPageSize(body.getPageSize());
		
		result.setData(pod);
		result.setMessage("success");
		result.setStatus(200);
		
		return result;
	}
    
    @Transactional
	@Override
	public CommonDto<Boolean> updateFollowStatus(Integer appid, ProjectsUpdateInputDto body) {
		CommonDto<Boolean> result  =  new CommonDto<>();

		if (body.getId() == null){
		    result.setMessage("项目的id不能为空");
		    result.setStatus(502);
		    result.setData(null);

		    return result;
        }

        if (body.getStatus() == null){
		    result.setMessage("项目跟进状态不能为空");
		    result.setData(null);
		    result.setStatus(502);
		    return result;
        }

		ProjectFollowStatus pfs=new ProjectFollowStatus();
		pfs.setProjectId(body.getId());
		try {
			pfs = projectFollowStatusMapper.selectOne(pfs);
		}catch(Exception e) {
			result.setData(null);
			result.setMessage("项目的跟进状态不存在唯一值");
			result.setStatus(400);
			return result;
		}
		if(pfs != null) { //执行更新
			pfs.setMetaFollowStatusId(body.getStatus());
			pfs.setDescription(body.getDescription());
			
            projectFollowStatusMapper.updateByPrimaryKeySelective(pfs);
		}else {//执行插入
			ProjectFollowStatus pfss=new ProjectFollowStatus();
			pfss.setProjectId(body.getId());
			pfss.setMetaFollowStatusId(body.getStatus());
		    pfss.setDescription(body.getDescription());
            pfss.setCreatTime(new Date());
            
			projectFollowStatusMapper.insertSelective(pfss);
		}

		result.setData(true);
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}

	@Override
	public CommonDto<List<MetaFollowStatus>> getFollowStatusSource(Integer appid) {
		CommonDto<List<MetaFollowStatus>> result=new CommonDto<List<MetaFollowStatus>>();
		
		result.setData(metaFollowStatusMapper.selectAll());
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}

	@Override
	public CommonDto<List<MetaDataSourceType>> getProjectsSource(Integer appid) {
		CommonDto<List<MetaDataSourceType>> result=new CommonDto<>();
		
		result.setData(metaDataSourceTypeMapper.selectAll());
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}

	@Override
	public CommonDto<List<AdminProjectRatingLog>> getProjectsRatingStages(Integer appid) {
		CommonDto<List<AdminProjectRatingLog>> result=new CommonDto<>();
		
		result.setData(adminProjectRatingLogMapper.selectAll());
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}

	@Override
	public CommonDto<List<String>> getFinancingStatus(Integer appid) {
		CommonDto<List<String>> result =new CommonDto<>();
		List<String> list=projectFinancingLogMapper.fetchFinancingStatus();
		result.setData(list);
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}

	@Override
	public CommonDto<List<ProjectFinancingLog>> getFinancingLogs(Integer appid, Integer projectId,Integer subjectType) {
		CommonDto<List<ProjectFinancingLog>> result =new CommonDto<>();
		
		List<ProjectFinancingLog> pfls=null;
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			Example example=new Example(ProjectFinancingLog.class);
			example.and()
			.andIsNotNull("financingTime")
			.andEqualTo("projectId",projectId)
			.andEqualTo("yn",0)
			.andEqualTo("approvalStatus",1);
			
			pfls = projectFinancingLogMapper.selectByExample(example);
			
			if(pfls != null && pfls.size() != 0) {
				pfls.forEach((e)->{
					//获取融资阶段的id
					Integer financingLogId = e.getId();
					InvestmentInstitutionsProject iip=new InvestmentInstitutionsProject();
					iip.setProjectId(financingLogId);
					iip.setYn(0);
					//查询关系表中相关的投资方的相关信息
					List<InvestmentInstitutionsProject> iips = investmentInstitutionsProjectMapper.select(iip);
					
					//用于获取所有的机构信息
//					List<InvestmentInstitutions> investmentInstitutions=new ArrayList<>();
					List<String> institutionShortNames=new ArrayList<>();
					if(iips != null) {
						for(InvestmentInstitutionsProject obj:iips) {
							//根据机构id获取机构的相关信息
							InvestmentInstitutions instiOne = investmentInstitutionsMapper.selectByPrimaryKey(obj.getInvestmentInstitutionsId());
							if(instiOne != null) {
//								investmentInstitutions.add(instiOne);
								institutionShortNames.add(instiOne.getShortName());
							}
						}
					}
//					e.setInstitutions(investmentInstitutions);
					e.setInstitutionsShortNames(institutionShortNames);
				});
			}
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
        	//TODO 机构的融资历史列表设置
        }
		
		
		result.setData(pfls);
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}
	
	@Transactional
	@Override
	public CommonDto<Boolean> removeFinancingLogById(Integer appid, Integer id,Integer subjectType) {
		CommonDto<Boolean> result=new CommonDto<Boolean>();
		
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			ProjectFinancingLog pfl=new ProjectFinancingLog();
			pfl.setId(id);
			pfl.setYn(1);
			projectFinancingLogMapper.updateByPrimaryKeySelective(pfl);
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
        	//TODO 机构的融资历史删除
        }
		
		result.setData(true);
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}

    @Override
    public CommonDto<ProjectFollowStatus> getFollowStatus(Integer projectId, Integer appid) {
        CommonDto<ProjectFollowStatus> result = new CommonDto<>();

        if (projectId == null){
            result.setMessage("项目id不能为空");
            result.setData(null);
            result.setStatus(502);

            return result;
        }

        ProjectFollowStatus projectFollowStatusForSearch = new ProjectFollowStatus();
        projectFollowStatusForSearch.setProjectId(projectId);
        List<ProjectFollowStatus> projectFollowStatusList = projectFollowStatusMapper.select(projectFollowStatusForSearch);

        result.setData(projectFollowStatusList.size() > 0?projectFollowStatusList.get(0):new ProjectFollowStatus());
        result.setStatus(200);
        result.setMessage("success");

        return result;
    }
    
    @Transactional
	@Override
	public CommonDto<Boolean> updateFinancingLog(Integer appid, ProjectFinancingLog body) {
		CommonDto<Boolean> result =new CommonDto<>();
		if(body.getSubjectType()==null) {
			result.setData(false);
			result.setMessage("请输入主体类型");
			result.setStatus(500);
			return result;
		}
		
		//对body进行数据格式化处理
		try {
			Date financingTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(body.getFinancingStr());
			body.setFinancingTime(financingTime);
		}catch(Exception e) {
			result.setData(false);
	        result.setStatus(500);
	        result.setMessage("日期格式解析错误");
	        return result;
		}
		
		if(Integer.valueOf(1).equals(body.getSubjectType())) {//项目
			//保存在融资历史保存或者更新后的主键id
			Integer afterUpdateLogId=null;
			if(body.getId() !=null) { //执行相关的更新操作
				body.setUpdateTime(new Date());
				
				projectFinancingLogMapper.updateByPrimaryKeySelective(body);
				afterUpdateLogId=body.getId();
			}else {//执行相关的插入操作,增加的话肯定会传递一个projectId作为body的属性
				Date now=new Date();
				
				body.setYn(0);
				body.setCreateTime(now);
				//设置审核状态,创建即默认审核通过
				body.setApprovalStatus(1);
				body.setApprovalTime(now);;
				
				//数据来源为运营人员后台添加
				body.setDataSoruceTypeId(3);
				
				projectFinancingLogMapper.insertSelective(body);
				afterUpdateLogId=body.getId();
			}
			
			//获取该融资历史信息的相关的投资方的相关信息
			List<String> shortNames = body.getInstitutionsShortNames();
			if((shortNames != null) && (shortNames.size()!=0)) {
				//收集该融资历史同机构的相关信息
				List<Integer> logRelativeInstitutions=new ArrayList<>();
				
				for(String tmp:shortNames) {
					InvestmentInstitutions ii=new InvestmentInstitutions();
					ii.setShortName(tmp);
					//作为查询结果的实体判断
					try { 
						ii = investmentInstitutionsMapper.selectOne(ii);
						if(ii != null) {//取得该机构的id
							logRelativeInstitutions.add(ii.getId());
						}else {//创建该机构
							InvestmentInstitutions createIi=new InvestmentInstitutions();
							createIi.setShortName(tmp);
							investmentInstitutionsMapper.insertSelective(createIi);
							//获取自增长id
							logRelativeInstitutions.add(createIi.getId());
						}
					}catch(Exception e) {
						this.LOGGER.info(e.getMessage(),e.fillInStackTrace());
						
						result.setData(false);
				        result.setStatus(500);
				        result.setMessage("机构的简称不唯一，数据存在错误");
				        return result;
					}
				}
				//统一设置该融资历史信息同机构的关联关系
				if((logRelativeInstitutions != null) && (logRelativeInstitutions.size()!=0)) {
					InvestmentInstitutionsProject iip=new InvestmentInstitutionsProject();
					//设置融资历史的id
					iip.setProjectId(afterUpdateLogId);
					//删除所有同之前投资机构的关系
					investmentInstitutionsProjectMapper.delete(iip);
					logRelativeInstitutions.forEach((e)->{
						//设置融资历史记录同投资机构的关系
						iip.setInvestmentInstitutionsId(e);
						iip.setYn(0);
						investmentInstitutionsProjectMapper.insertSelective(iip);
					});
				}
			}else {//执行原关联的投资机构的删除操作
				InvestmentInstitutionsProject iip=new InvestmentInstitutionsProject();
				//设置融资历史的id  
				iip.setProjectId(afterUpdateLogId);
				//删除所有同之前投资机构的关系
				investmentInstitutionsProjectMapper.delete(iip);  
			}
        }else if(Integer.valueOf(2).equals(body.getSubjectType())) {//机构
        	//TODO 机构的融资历史信息更新
        }
		
		result.setData(true);
        result.setStatus(200);
        result.setMessage("success");
		return result;
	}

	@Override
	public CommonDto<List<InvestmentInstitutionsProject>> getFinancingLogDetails(Integer appid, Integer financingLogId,Integer subjectType) {
		CommonDto<List<InvestmentInstitutionsProject>> result =new CommonDto<>();
		if(subjectType==null) {
			result.setData(null);
	        result.setStatus(500);
	        result.setMessage("主体类型不能为null");
			return result;
		}
		List<InvestmentInstitutionsProject> iips=null;
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			InvestmentInstitutionsProject iip=new InvestmentInstitutionsProject();
			iip.setProjectId(financingLogId);
			//设置有效标志为有效
//			iip.setYn(0);
			iips = investmentInstitutionsProjectMapper.select(iip);
			//融资历史记录的详细信息进行进一步的输出格式化
			if(iips !=null) {  
				for(InvestmentInstitutionsProject temp:iips) {
					InvestmentInstitutions ii = investmentInstitutionsMapper.selectByPrimaryKey(temp.getInvestmentInstitutionsId()); 
					if(ii != null) {
						temp.setInvestmentShortName(ii.getShortName());
					}
					/*if(temp.getAccountingDate() != null) {
						temp.setAccountingDateOutputStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(temp.getAccountingDate()));
					}*/
				}
			}
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
        	//TODO 机构的融资历史
        }
		
		result.setData(iips);
        result.setStatus(200);
        result.setMessage("success");
		return result;
	}
	
	@Transactional
	@Override
	public CommonDto<Boolean> removeSingleInvestment(Integer appid, Integer projectId,Integer investmentInstitutionsId,Integer subjectType) {
		CommonDto<Boolean> result=new CommonDto<>();
		if(projectId==null) {
			result.setData(null);
	        result.setStatus(500);
	        result.setMessage("主体id不能为null");
			return result;
		}
		if(investmentInstitutionsId==null) {
			result.setData(null);
	        result.setStatus(500);
	        result.setMessage("机构id不能为null");
			return result;
		}
		if(subjectType==null) {
			result.setData(null);
	        result.setStatus(500);
	        result.setMessage("主体类型不能为null");
			return result;
		}
		
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			investmentInstitutionsProjectMapper.updateDelStatus(projectId,investmentInstitutionsId);
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
        	//TODO 机构的融资历史对应的投资方信息
        }
		
		result.setData(true);
        result.setStatus(200);
        result.setMessage("success");
		return result;
	}
	
	@Transactional
	@Override
	public CommonDto<Boolean> updateRelativeInvestmentInfo(Integer appid, InvestmentInstitutionsProject body) {
		CommonDto<Boolean> result=new CommonDto<>();
		if(body.getSubjectType()==null) {
			result.setData(null);
	        result.setStatus(500);
	        result.setMessage("主体类型不能为null");
			return result;
		}
		if(body.getProjectId()==null) {
			result.setData(null);
	        result.setStatus(500);
	        result.setMessage("融资历史id不能为null");
			return result;
		}
		//Deprecated
		/*try {
			body.setAccountingDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(body.getAccountingDateStr()));
		}catch(Exception e) {
			result.setData(false);
	        result.setStatus(500);
	        result.setMessage("日期字符串不正确");
		}*/
		
		
		if(Integer.valueOf(1).equals(body.getSubjectType())) {//项目
			investmentInstitutionsProjectMapper.updateLogRelativeInvestmentInfo(body);
        }else if(Integer.valueOf(2).equals(body.getSubjectType())) {//机构
        	//TODO 机构的融资历史对应的投资方的信息编辑
        }
		
		result.setData(true);
        result.setStatus(200);
        result.setMessage("success");
        
		return result;
	}

	@Override
	public CommonDto<CommentAndHighlightsOutputDto> getProInfoById(Integer appid, Integer subjectId,Integer subjectType) {
		CommonDto<CommentAndHighlightsOutputDto> result=new CommonDto<>();
		CommentAndHighlightsOutputDto cah=new CommentAndHighlightsOutputDto();
		if(subjectType.equals(1)) {//项目
			Projects pro = projectsMapper.selectByPrimaryKey(subjectId);
			cah.setComment(pro.getCommet());
			cah.setInvestmentHighlights(pro.getProjectInvestmentHighlights());
			
			result.setData(cah);
			result.setStatus(200);;
			result.setMessage("success");
		}else if(subjectType.equals(2)) {//机构
			InvestmentInstitutions ii = investmentInstitutionsMapper.selectByPrimaryKey(subjectId);
			cah.setComment(ii.getComment());
			cah.setInvestmentHighlights("");
			
			result.setData(cah);
			result.setStatus(200);;
			result.setMessage("success");
		}
		return result;
	}
	
	@Transactional
	@Override
	public CommonDto<Boolean> updateProInfos(Integer appid, CommentAndHighlightsInputDto body) {
		CommonDto<Boolean> result=new CommonDto<>();
		
		if(body.getSubjectId()==null) {
			result.setData(false);
			result.setStatus(500);;
			result.setMessage("请输入主体id"); 
		}
		if(body.getSubjectType() == null) {
			result.setData(false);
			result.setStatus(500);;
			result.setMessage("请输入主体类型，1项目，2机构"); 
		}
		
		if (body.getSubjectType().equals(1)) {//项目相关信息更新
			Projects pro=new Projects();
			pro.setId(body.getSubjectId());
			pro.setCommet(body.getComment());
			pro.setProjectInvestmentHighlights(body.getInvestmentHighlights());
			
			projectsMapper.updateByPrimaryKeySelective(pro);
			
		}else if(body.getSubjectType().equals(2)) {//机构相关信息更新
			InvestmentInstitutions ii=new InvestmentInstitutions();
			ii.setId(body.getSubjectId());
			ii.setComment(body.getComment());
			//TODO 机构主体中不存在投资亮点的相关数据结构
				
			investmentInstitutionsMapper.updateByPrimaryKeySelective(ii);
		}
		
		result.setData(true);
		result.setStatus(200);;
		result.setMessage("success");  
		
		return result;
	}
	@Transactional(readOnly=true)
	@Override
	public CommonDto<Object> listProPartsByCompanyIdAndProtype(Integer appid, Integer subjectType,Integer companyId) {
		CommonDto<Object> result=new CommonDto<>();
		
		Object list=new ArrayList<>();
		
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			//TODO 项目分部的后台数据结构待完善
			
			result.setData(list);
			result.setStatus(200);
			result.setMessage("success");  
			
			return result;
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
			//获取该机构的所有分部信息
			List<InvestmentInstitutionsAddressPart> iiapLists = investmentInstitutionsAddressPartMapper.selectAllOrderByWeight(companyId);
			Integer i=0;
			for(InvestmentInstitutionsAddressPart e:iiapLists) {
				e.setSort(++i);
			}
			
			list=iiapLists; 
		}
		
		result.setData(list);
		result.setStatus(200);
		result.setMessage("success");  
		
		return result;
	}
	
	@Transactional
	@Override
	public CommonDto<Boolean> removePartInfoById(Integer appid, Integer partId,Integer subjectType) {
		
		CommonDto<Boolean> result =new CommonDto<Boolean>();
		
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			//TODO 删除项目的分部信息
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
			InvestmentInstitutionsAddressPart iiap =new InvestmentInstitutionsAddressPart();
			iiap.setId(partId);
			iiap.setYn(1);
			investmentInstitutionsAddressPartMapper.updateByPrimaryKeySelective(iiap);
		}
		
		result.setData(true);
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}
	
	@Transactional
	@Override
	public CommonDto<Boolean> saveOrUpdayePart(Integer appid, InvestmentInstitutionsAddressPart body) {
		CommonDto<Boolean> result =new CommonDto<Boolean>();
		if(Integer.valueOf(1).equals(body.getSubjectType())) {//项目
			//TODO 项目分部信息，后台数据结构有待完善
        }else if(Integer.valueOf(2).equals(body.getSubjectType())) {//机构
			if(body.getId()!=null) {//更新操作
				investmentInstitutionsAddressPartMapper.updateByPrimaryKeySelective(body);
			}else {//插入操作
				investmentInstitutionsAddressPartMapper.insertSelective(body);
			}
		}
		
		result.setData(true);
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}

	@Override
	public CommonDto<List<MetaJobType>> getMetaJobTypes(Integer appid) {
		CommonDto<List<MetaJobType>> result =new CommonDto<>();
		
		result.setData(metaJobTypeMapper.selectAll());
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}
	@Transactional
	@Override
	public CommonDto<Boolean> saveOrUpdateRecruitment(Integer appid, Recruitment body) {
		CommonDto<Boolean> result =new CommonDto<Boolean>();
		
		if(Integer.valueOf(1).equals(body.getSubjectType())) {//项目
			if(body.getId()!=null) {//更新操作,此时用户id要设置到createdUserId中
				body.setLastUpdateTime(new Date());
				recruitmentMapper.updateByPrimaryKeySelective(body);
			}else {//插入操作，此时用户id要设置到updatedUserId中
				body.setCreateTime(new Date());
				body.setYn(0);//默认设置为有效
				recruitmentMapper.insertSelective(body);
			}
        }else if(Integer.valueOf(2).equals(body.getSubjectType())) {//机构
        	//TODO 机构招聘信息的更新
        }
		
		result.setData(true);
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}
	@Transactional
	@Override
	public CommonDto<Boolean> removeRecruInfoById(Integer appid, Integer id,Integer subjectType) {
		CommonDto<Boolean> result =new CommonDto<Boolean>();
		
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			Recruitment rec =new Recruitment();
			rec.setId(id);
			rec.setYn(1);
			recruitmentMapper.updateByPrimaryKeySelective(rec);
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
        	//TODO 机构招聘信息删除
        }
		
		result.setData(true);
		result.setMessage("success");
		result.setStatus(200);
		return result;
	}

	@Override
	public CommonDto<List<Recruitment>> listRecruInfos(Integer appid, Integer companyId,Integer subjectType) {
		CommonDto<List<Recruitment>> result=new CommonDto<>();
		List<Recruitment> recruitments=null;
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			Recruitment rec=new Recruitment();
			rec.setYn(0);
			rec.setCompanyId(companyId);
			recruitments = recruitmentMapper.select(rec);
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
        	//TODO 机构的招聘列表回显
        }
		
		result.setData(recruitments);
		result.setStatus(200);
		result.setMessage("success");  
		
		return result;
	}

	@Override
	public CommonDto<RecruitmentInfo> echoRequirementInfo(Integer appid, Integer proId,Integer subjectType) {
		CommonDto<RecruitmentInfo> result=new CommonDto<>();
		
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			RecruitmentInfo reci=new RecruitmentInfo();
			reci.setCompanyId(proId);
			List<RecruitmentInfo> query = recruitmentInfoMapper.select(reci);
			if(query !=null && query.size() !=0) {
				result.setData(query.get(0));
				 	
			}
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
        	//TODO 机构的招聘需求设置
        }
		
		result.setStatus(200);
		result.setMessage("success");
		return result;
	}
	
	@Transactional
	@Override
	public CommonDto<Boolean> saveOrUpdateRecruInfo(Integer appid, RecruitmentInfo body) {
		CommonDto<Boolean> result=new CommonDto<Boolean>();
		if(body.getCompanyId()==null) {
			result.setData(false);
			result.setStatus(500);
			result.setMessage("请关联主体id");
			return result;
		}
		
		if(Integer.valueOf(1).equals(body.getSubjectType())) {//项目
			if(body.getId()!=null) {
				recruitmentInfoMapper.updateByPrimaryKeySelective(body);
			}else {
				recruitmentInfoMapper.insertSelective(body);
			}
        }else if(Integer.valueOf(2).equals(body.getSubjectType())) {//机构
        	//TODO 机构的招聘信息更新
        }
		
		
		result.setData(true);
		result.setStatus(200);
		result.setMessage("success");
		return result;
	}

	@Override
	public CommonDto<List<ProjectProgress>> listProProgress(Integer appid, Integer companyId,Integer subjectType) {
		CommonDto<List<ProjectProgress>> result=new CommonDto<>();
		
		List<ProjectProgress> pps=null;
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			ProjectProgress pp=new ProjectProgress();
			pp.setCompanyId(companyId);
			pp.setYn(0);
			pps = projectProgressMapper.select(pp);
			
			if(pps!=null) {
				pps.forEach((e)->{
					Users user = usersMapper.selectByPrimaryKey(e.getOperationUser());
//					if(user != null) {
						e.setUserName(user.getActualName());
//					}
				});
			}
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
        	//TODO 机构的进展跟踪
        }
		
		result.setData(pps);
		result.setStatus(200);
		result.setMessage("success");  
		
		return result;
	}

	@Override
	public CommonDto<Boolean> saveOrUpdateProgressInfo(Integer appid, ProjectProgress body) {
		CommonDto<Boolean> result=new CommonDto<>();
		
		if(Integer.valueOf(1).equals(body.getSubjectType())) {//项目
			if(body.getId()!=null) {//更新
				body.setOperationTime(new Date());
				projectProgressMapper.updateByPrimaryKeySelective(body);
			}else {//插入
				body.setOperationTime(new Date());
				body.setYn(0);
				projectProgressMapper.insertSelective(body);
			}
        }else if(Integer.valueOf(2).equals(body.getSubjectType())) {//机构
        	//TODO 机构的进展信息更新
        }
		
		
		result.setData(true);
		result.setStatus(200);
		result.setMessage("success");  
		
		return result;
	}

	@Override
	public CommonDto<Boolean> removeProgressInfoById(Integer appid, Integer id,Integer subjectType) {
		CommonDto<Boolean> result=new CommonDto<>();
		
		if(Integer.valueOf(1).equals(subjectType)) {//项目
			ProjectProgress pp=new ProjectProgress();
			pp.setId(id);
			pp.setYn(1);
			projectProgressMapper.updateByPrimaryKeySelective(pp);
        }else if(Integer.valueOf(2).equals(subjectType)) {//机构
        	//TODO 机构的进展信息删除
        }
		
		result.setData(true);
		result.setStatus(200);
		result.setMessage("success"); 
		return result;
	}

	@Override
	public CommonDto<List<InvestmentInstitutions>> intelligentSearch(Integer appid, String keyword) {
		CommonDto<List<InvestmentInstitutions>> result=new CommonDto<>();
		
		result.setData(investmentInstitutionsMapper.blurScan(keyword));
		result.setStatus(200);
		result.setMessage("success"); 
		return result;  
	}

	@Override
	public CommonDto<DatasOperationManage> echoManagementInfo(Integer appid, Integer subjectId ,Integer subjectType) {
		CommonDto<DatasOperationManage> result =new CommonDto<>();
		if(subjectType==null) {
			result.setData(null);
			result.setMessage("请输入主体类型");
			result.setStatus(500);
			return result;
		}
		DatasOperationManage dom =new DatasOperationManage();
		dom.setDataId(subjectId);
		if(subjectType.equals(1)) {
			dom.setDataType("PROJECT");
		}else if(subjectType.equals(2)) {
			dom.setDataType("INVESTMENT_INSTITUTIONS");
		}
		
		//一个项目只有一条的运营管理记录
		try {
			dom = datasOperationManageMapper.selectOne(dom);
		}catch(Exception e) {
			result.setData(null);  
	        result.setStatus(500); 
	        result.setMessage("由于请求参数不正确导致数据库查询出多条相关的运营纪录");
			return result;
		}
		if(dom !=null) {
			Integer basicsRecommend = dom.getBasicsRecommend();
			if(basicsRecommend==null) {
				basicsRecommend=0;
			}
			Integer dynamicRecommand = dom.getDynamicRecommand();
			if(dynamicRecommand==null) {
				dynamicRecommand=0;
			}
			Integer operationRecommend = dom.getOperationRecommend();
			if(operationRecommend==null) {
				operationRecommend=0;  
			}
			dom.setRecommand(basicsRecommend + dynamicRecommand+ operationRecommend );
		}
		
		result.setData(dom !=null ? dom : new DatasOperationManage());
        result.setStatus(200); 
        result.setMessage("success");
		return result;
	}

	@Override
	public CommonDto<Boolean> saveOrUpdateManagement(Integer appid, DatasOperationManage body) {
		CommonDto<Boolean> result =new CommonDto<>();
		
		DatasOperationManage dom =new DatasOperationManage();
		dom.setDataId(body.getDataId());
		
		try {
			
			if(body.getSubjectType().equals(1)) {
				dom.setDataType("PROJECT");
				body.setDataType("PROJECT");
			}else if(body.getSubjectType().equals(2)) {
				dom.setDataType("INVESTMENT_INSTITUTIONS");
				body.setDataType("INVESTMENT_INSTITUTIONS");
			}
			
			if( datasOperationManageMapper.selectOne(dom) != null) {//执行更新操作
				body.setUpdateTime(new Date());
				datasOperationManageMapper.updateByPrimaryKeySelective(body);
			}else {//执行增加
				body.setCreateTime(new Date());
				datasOperationManageMapper.insertSelective(body);
			}
		}catch(Exception e ) {
			result.setData(true);
	        result.setStatus(200); 
	        result.setMessage("运营管理表中存在主题类型不唯一的冗余数据，DB数据存在问题");
			return result;
		}
		result.setData(true);
        result.setStatus(200); 
        result.setMessage("success");
        
		return result;
	}
}