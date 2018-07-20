package com.lhjl.tzzs.proxy.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.lhjl.tzzs.proxy.mapper.*;
import com.lhjl.tzzs.proxy.model.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.LabelList;
import com.lhjl.tzzs.proxy.dto.ProjectsSendDto;
import com.lhjl.tzzs.proxy.dto.TeamDto;
import com.lhjl.tzzs.proxy.dto.TeamDto1;
import com.lhjl.tzzs.proxy.service.EvaluateService;
import com.lhjl.tzzs.proxy.service.ProjectsSendService;
import com.lhjl.tzzs.proxy.service.common.CommonUserService;

import tk.mybatis.mapper.entity.Example;

/**
 * Created by 蓝海巨浪 on 2017/10/23.
 */
@Service
public class ProjectsSendServiceImpl implements ProjectsSendService{

    @Resource
    private UsersMapper usersMapper;
    @Resource
    private ProjectSendLogsMapper projectSendLogsMapper;
    //关系表
    @Resource
    private ProjectFinancingInvestmentInstitutionRelationshipMapper projectFinancingInvestmentInstitutionRelationshipMapper;
    
    //项目融资申请表
    @Resource
    private ProjectFinancingApprovalMapper projectFinancingApprovalMapper;
    //历史表
    @Resource
    private ProjectFinancingHistoryMapper projectFinancingHistoryMapper;
    @Resource
    private EvaluateService evaluateService;
    //创收人
    @Resource
    private FoundersMapper foundersMapper;
    //创始人教育
    @Resource
    private FoundersEducationMapper foundersEducationMapper;
    //创始人工作
    @Resource
    private FoundersWorkMapper foundersWorkMapper;
    //相似精品
    @Resource
    private ProjectSendLogCompetingMapper projectSendLogCompetingMapper;

    @Resource
    private ProjectSendTeamMemberMapper projectSendTeamMemberMapper;

    //团队教育背景
    @Resource
    private ProjectSendTeamMemberEducationMapper projectSendTeamMemberEducationMapper;

    //团队工作背景
    @Resource
    private ProjectSendTeamMemberWorkMapper projectSendTeamMemberWorkMapper;

    @Resource
    private ProjectsMapper projectsMapper;

    @Resource
    private ProjectSegmentationMapper projectSegmentationMapper;

    @Resource
    private ProjectTeamMemberMapper projectTeamMemberMapper;

    @Resource
    private ProjectTeamMemberEducationMapper projectTeamMemberEducationMapper;


    @Resource
    private ProjectTeamMemberWorkMapper projectTeamMemberWorkMapper;
    
    @Autowired
    private CommonUserService  commonUserService;

    @Resource
    private ProjectCompetitiveProductsMapper projectCompetitiveProductsMapper;
    /**
     * 项目投递
     * @param params 投递参数
     * @param userId 用户ID
     * @return
     */
    @Transactional
    @Override
    public CommonDto<String> ctuisongsecond(ProjectsSendDto params, int userId) {
        CommonDto<String> result = new CommonDto<>();



        if (params.getXmid().length() > 0) {
                ProjectFinancingInvestmentInstitutionRelationship p = new ProjectFinancingInvestmentInstitutionRelationship();
                p.setProjectSendLogId(Integer.parseInt(params.getXmid()));
                List<ProjectFinancingInvestmentInstitutionRelationship> list = projectFinancingInvestmentInstitutionRelationshipMapper.select(p);
                List<String> a = new LinkedList<String>();
                String[] educationArray = null;
                for (ProjectFinancingInvestmentInstitutionRelationship d : list) {
                    a.add(d.getInvestmentInstitutionId().toString());
                    educationArray = new String[a.size()];
                    educationArray = a.toArray(educationArray);
                }
                int flag = 0;
                String[] split = params.getTsid().split(",");
                for (int i = 0; i < split.length; i++) {
                    for (int j = 0; j < educationArray.length; j++) {
                        if (split[i].equals(educationArray[j])) {
                            flag = flag + 1;
                        }
                    }
                }
                //判断条件1
                if (flag == split.length && flag ==educationArray.length) {
                    ProjectSendLogs projectSendLogs1 = new ProjectSendLogs();
                    projectSendLogs1.setId(Integer.parseInt(params.getXmid()));
                    projectSendLogs1 = projectSendLogsMapper.selectOne(projectSendLogs1);
                    Date creatTime = projectSendLogs1.getCreatTime();
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(creatTime);
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    Date end = calendar.getTime();
                    Date now = new Date();
                    //判断条件2
                    if (end.getTime() > now.getTime()) {
                        //判断条件3
                        if (projectSendLogs1.getCompanyShortName().equals(params.getCompanyshortname())) {
                            Users users1 = new Users();
                            users1.setId(userId);
                            users1 = usersMapper.selectByPrimaryKey(users1);
                            //判断条件4
                            if (users1.getActualName().equals(params.getTuisongxiangmubiao7realname())) {
                                Projects projects = new Projects();
                                projects.setShortName(params.getCompanyshortname());
                                projects = projectsMapper.selectOne(projects);
                                //更新主库
                                if (projects != null) {
                                    projects.setShortName(params.getCompanyshortname());
                                    projects.setFullName(params.getTuisongxiangmubiao7companywhe());
                                    projects.setKernelDesc(params.getOneintroduct());
                                    projects.setCommet(params.getTuisongxiangmubiao7companypro());
                                    projects.setUrl(params.getWebsite());
                                    projects.setItemLabel(params.getTuisongxiangmubiao7projecttag());
                                    projects.setCity(params.getTuisongxiangmubiao7city());
                                    projects.setProjectLogo(params.getLogo());
                                    projects.setProjectInvestmentHighlights(params.getHighlights());
                                    projectsMapper.updateByPrimaryKey(projects);
                                   /* ProjectSendLogs pp =new ProjectSendLogs();
                                    pp.setId(Integer.parseInt(params.getXmid()));
                                    pp=projectSendLogsMapper.selectByPrimaryKey(pp);*/

                                    ProjectSegmentation ps = new ProjectSegmentation();
                                    ps.setProjectId(projects.getId());
                                    projectSegmentationMapper.delete(ps);
                                    String[] fieldArry = params.getTuisongxiangmubiao7industryfi().split(",");
                                    List<ProjectSegmentation> dataLogWorklist = new Page<ProjectSegmentation>();
                                    for (int b = 0; b < fieldArry.length; b++) {
                                        ProjectSegmentation ps1 = new ProjectSegmentation();
                                        ps1.setSegmentationName(fieldArry[b]);
                                        ps1.setProjectId(projects.getId());
                                        dataLogWorklist.add(ps1);
                                    }
                                    projectSegmentationMapper.insertList(dataLogWorklist);
                                    //团队成员
                                    ProjectSendTeamMember projectSendTeamMember = new ProjectSendTeamMember();
                                    projectSendTeamMember.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                                    List<ProjectSendTeamMember> projectSendTeamMemberList = projectSendTeamMemberMapper.select(projectSendTeamMember);
                                    ProjectTeamMember t = new ProjectTeamMember();
                                    t.setProjectId(projects.getId());
                                    projectTeamMemberMapper.delete(t);
                                    if (projectSendTeamMemberList.size() > 0) {
                                        for (ProjectSendTeamMember pst : projectSendTeamMemberList) {
                                            ProjectTeamMember projectTeamMember = new ProjectTeamMember();
                                            projectTeamMember.setCreateTime(pst.getCreateTime());
                                            projectTeamMember.setMumberDesc(pst.getMemberDesc());
                                            projectTeamMember.setMumberDuties(pst.getMemberDuties());
                                            projectTeamMember.setMumberName(pst.getMemberName());
                                            projectTeamMember.setProjectId(projects.getId());
                                            projectTeamMember.setShareRatio(pst.getShareRatio());
                                            projectTeamMember.setYn(0);

                                            projectTeamMemberMapper.insertSelective(projectTeamMember);

                                            //获取到刚创建的团队成员id
                                            Integer ptmid = projectTeamMember.getId();

                                            //获取提交审核中的团队成员教育经历，工作经历
                                            ProjectSendTeamMemberEducation projectSendTeamMemberEducationForSearch = new ProjectSendTeamMemberEducation();
                                            projectSendTeamMemberEducationForSearch.setProjectSendTeamMemberId(pst.getId());

                                            ProjectSendTeamMemberWork projectSendTeamMemberWorkForSearch = new ProjectSendTeamMemberWork();
                                            projectSendTeamMemberWorkForSearch.setProjectSendTeamMemberId(pst.getId());

                                            List<ProjectSendTeamMemberEducation> projectSendTeamMemberEducationList = projectSendTeamMemberEducationMapper.select(projectSendTeamMemberEducationForSearch);
                                            List<ProjectSendTeamMemberWork> projectSendTeamMemberWorkList = projectSendTeamMemberWorkMapper.select(projectSendTeamMemberWorkForSearch);

                                            ProjectTeamMemberEducation projectSendTeamMemberEducationForSearch1 = new ProjectTeamMemberEducation();
                                            projectSendTeamMemberEducationForSearch1.setProjectTeamMemberId(pst.getId());


                                            ProjectTeamMemberWork projectSendTeamMemberWorkForSearch1 = new ProjectTeamMemberWork();
                                            projectSendTeamMemberWorkForSearch1.setProjectTeamMemberId(pst.getId());

                                            List<ProjectTeamMemberEducation> projectSendTeamMemberEducationList1 = projectTeamMemberEducationMapper.select(projectSendTeamMemberEducationForSearch1);
                                            List<ProjectTeamMemberWork> projectSendTeamMemberWorkList1 = projectTeamMemberWorkMapper.select(projectSendTeamMemberWorkForSearch1);
                                            if (projectSendTeamMemberEducationList1.size() > 0) {
                                                projectTeamMemberEducationMapper.delete(projectSendTeamMemberEducationForSearch1);

                                            }
                                            if (projectSendTeamMemberWorkList1.size() > 0) {
                                                projectTeamMemberWorkMapper.delete(projectSendTeamMemberWorkForSearch1);

                                            }
                                            //创建教育经历
                                            if (projectSendTeamMemberEducationList.size() > 0) {
                                                for (ProjectSendTeamMemberEducation pstme : projectSendTeamMemberEducationList) {
                                                    ProjectTeamMemberEducation projectTeamMemberEducation = new ProjectTeamMemberEducation();
                                                    projectTeamMemberEducation.setProjectTeamMemberId(ptmid);
                                                    projectTeamMemberEducation.setEducationExperience(pstme.getEducationExperience());

                                                    projectTeamMemberEducationMapper.insertSelective(projectTeamMemberEducation);
                                                }
                                            }
                                            //创建工作经历
                                            if (projectSendTeamMemberWorkList.size() > 0) {
                                                for (ProjectSendTeamMemberWork pstmw : projectSendTeamMemberWorkList) {
                                                    ProjectTeamMemberWork projectTeamMemberWork = new ProjectTeamMemberWork();
                                                    projectTeamMemberWork.setProjectTeamMemberId(ptmid);
                                                    projectTeamMemberWork.setWorkExperience(pstmw.getWorkExperience());

                                                    projectTeamMemberWorkMapper.insert(projectTeamMemberWork);
                                                }
                                            }

                                        }
                                    }
                                    //相似竞品
                                    ProjectSendLogCompeting projectSendLogCompeting = new ProjectSendLogCompeting();
                                    List<ProjectSendLogCompeting> dataLogWorklist1 = new Page<ProjectSendLogCompeting>();
                                    String competing = params.getCompeting();
                                    projectSendLogCompeting.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                                    List<ProjectSendLogCompeting> one = projectSendLogCompetingMapper.select(projectSendLogCompeting);
                                    if(competing ==null || "".equals(competing)){
                                        projectSendLogCompetingMapper.delete(projectSendLogCompeting);
                                    }else {
                                        if (one.size() > 0) {
                                            projectSendLogCompetingMapper.delete(projectSendLogCompeting);
                                            ProjectSendLogCompeting dataLogEducation2 = new ProjectSendLogCompeting();
                                            dataLogEducation2.setProjectSendLogsId(projects.getId());
                                            projectSendLogCompetingMapper.delete(dataLogEducation2);
                                            String[] fieldArry1 = competing.split(",");
                                            for (int b = 0; b < fieldArry1.length; b++) {
                                                ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                                projectSendLogCompeting1.setCompetingProductName(fieldArry1[b]);
                                                projectSendLogCompeting1.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                                                dataLogWorklist1.add(projectSendLogCompeting1);
                                            }
                                            projectSendLogCompetingMapper.insertList(dataLogWorklist1);
                                        } else {
                                            String[] fieldArry1 = competing.split(",");
                                            for (int b = 0; b < fieldArry1.length; b++) {
                                                ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                                projectSendLogCompeting1.setCompetingProductName(fieldArry1[b]);
                                                projectSendLogCompeting1.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                                                dataLogWorklist1.add(projectSendLogCompeting1);
                                            }
                                            projectSendLogCompetingMapper.insertList(dataLogWorklist1);
                                        }
                                    }
                                    ProjectCompetitiveProducts projectCompetitiveProducts =new ProjectCompetitiveProducts();
                                    projectCompetitiveProducts.setProjectId(projects.getId());
                                    List<ProjectCompetitiveProducts> list1 =projectCompetitiveProductsMapper.select(projectCompetitiveProducts);
                                    if(list1.size()>0) {
                                        projectCompetitiveProductsMapper.delete(projectCompetitiveProducts);
                                    }
                                    List<ProjectCompetitiveProducts> dataLogWorklist2 = new Page<ProjectCompetitiveProducts>();
                                    for(ProjectSendLogCompeting ee :dataLogWorklist1){
                                        ProjectCompetitiveProducts projectCompetitiveProducts1 =new ProjectCompetitiveProducts();
                                        projectCompetitiveProducts1.setProjectId(projects.getId());
                                        projectCompetitiveProducts1.setCompetitiveProductsName(ee.getCompetingProductName());
                                        dataLogWorklist2.add(projectCompetitiveProducts1);

                                    }
                                    projectCompetitiveProductsMapper.insertList(dataLogWorklist2);
                                }else{
                                    ProjectSendLogCompeting projectSendLogCompeting = new ProjectSendLogCompeting();
                                    List<ProjectSendLogCompeting> dataLogWorklist1 = new Page<ProjectSendLogCompeting>();
                                    String competing = params.getCompeting();
                                    projectSendLogCompeting.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                                    List<ProjectSendLogCompeting> one = projectSendLogCompetingMapper.select(projectSendLogCompeting);
                                    if(competing ==null && "".equals(competing)){
                                        projectSendLogCompetingMapper.delete(projectSendLogCompeting);
                                    }else {
                                        if (one.size() > 0) {
                                            projectSendLogCompetingMapper.delete(projectSendLogCompeting);
                                            String[] fieldArry1 = competing.split(",");
                                            for (int b = 0; b < fieldArry1.length; b++) {
                                                ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                                projectSendLogCompeting1.setCompetingProductName(fieldArry1[b]);
                                                projectSendLogCompeting1.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                                                dataLogWorklist1.add(projectSendLogCompeting1);
                                            }
                                            projectSendLogCompetingMapper.insertList(dataLogWorklist1);
                                        } else {
                                            String[] fieldArry1 = competing.split(",");
                                            for (int b = 0; b < fieldArry1.length; b++) {
                                                ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                                projectSendLogCompeting1.setCompetingProductName(fieldArry1[b]);
                                                projectSendLogCompeting1.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                                                dataLogWorklist1.add(projectSendLogCompeting1);
                                            }
                                            projectSendLogCompetingMapper.insertList(dataLogWorklist1);
                                        }
                                    }
                                }
                                //更新用户信息
                                Users users = new Users();
                                users.setId(userId);
                                users = usersMapper.selectByPrimaryKey(users);
                                users.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                                users.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                                users.setCompanyDuties(params.getTuisongxiangmubiao7assumeoffi());
                                users.setActualName(params.getTuisongxiangmubiao7realname());
                                users.setCity(params.getTuisongxiangmubiao7city());
                                users.setDesc(params.getTuisongxiangmubiao7profile());
                                if (params.getTuisongxiangmubiao7mailbox() != null && !"".equals(params.getTuisongxiangmubiao7mailbox())) {
                                    users.setEmail(params.getTuisongxiangmubiao7mailbox());
                                }
                                if (params.getTuisongxiangmubiao7wechatnumb() != null && !"".equals(params.getTuisongxiangmubiao7wechatnumb())) {
                                    users.setWechat(params.getTuisongxiangmubiao7wechatnumb());
                                }
                                usersMapper.updateByPrimaryKey(users);
                                ProjectSendLogs projectSendLogs = new ProjectSendLogs();
                                projectSendLogs.setId(Integer.parseInt(params.getXmid()));
                                projectSendLogs = projectSendLogsMapper.selectByPrimaryKey(projectSendLogs);
                                projectSendLogs.setUserid(userId);
                                projectSendLogs.setCity(params.getTuisongxiangmubiao7city());
                                projectSendLogs.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                                projectSendLogs.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                                projectSendLogs.setField(params.getTuisongxiangmubiao7industryfi());
                                String [] woel ={};
                                if (params.getTuisongxiangmubiao7projecttag() != null && !"".equals(params.getTuisongxiangmubiao7projecttag())) {
                                    projectSendLogs.setProjectTags(params.getTuisongxiangmubiao7projecttag());
                                }else{
                                    projectSendLogs.setProjectTags("");
                                }
                                projectSendLogs.setCompanyLogo(params.getLogo());
                                if (params.getCompanyshortname() != null || "".equals(params.getCompanyshortname())) {
                                    projectSendLogs.setCompanyShortName(params.getCompanyshortname());
                                } else {
                                    result.setStatus(50001);
                                    result.setMessage("公司简称不能为空");
                                }

                                if (params.getOneintroduct() != null || "".equals(params.getOneintroduct())) {
                                    projectSendLogs.setCompanyOneSentenceIntroduct(params.getOneintroduct());
                                } else {
                                    result.setStatus(50001);
                                    result.setMessage("一句话介绍不能为空");
                                }

                                if (params.getHighlights() != null || "".equals(params.getHighlights())) {
                                    projectSendLogs.setCompanyInvestmentHighlights(params.getHighlights());
                                } else {
                                    result.setStatus(50001);
                                    result.setMessage("投资亮点不能为空");
                                }
                                projectSendLogs.setCompanyOfficialWebsite(params.getWebsite());
                                projectSendLogsMapper.updateByPrimaryKey(projectSendLogs);
                                int projectId = projectSendLogs.getId();
                                projectSendTeamMemberMapper.updateTeame(projectId,userId);
                                ProjectFinancingHistory projectFinancingHistory =new ProjectFinancingHistory();
                                projectFinancingHistory.setProjectSendLogId(params.getXmid());
                                projectFinancingHistory.setUserId(userId);
                                projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory);
                                if(projectFinancingHistory != null){
                                    ProjectFinancingHistory projectFinancingHistory3 =new ProjectFinancingHistory();
                                    projectFinancingHistory3.setProjectSendLogId("-1");
                                    projectFinancingHistory3.setUserId(userId);
                                    projectFinancingHistory3=projectFinancingHistoryMapper.selectOne(projectFinancingHistory3);
                                    if( projectFinancingHistory3 !=null) {
                                        projectFinancingHistoryMapper.delete(projectFinancingHistory);
                                        projectFinancingHistoryMapper.updateHistory(projectId, userId);
                                    }
                                }else {
                                    ProjectFinancingHistory projectFinancingHistory3 =new ProjectFinancingHistory();
                                    projectFinancingHistory3.setProjectSendLogId("-1");
                                    projectFinancingHistory3.setUserId(userId);
                                    projectFinancingHistory3=projectFinancingHistoryMapper.selectOne(projectFinancingHistory3);
                                    if( projectFinancingHistory3 !=null) {
                                        projectFinancingHistoryMapper.updateHistory(projectId, userId);
                                    }
                                }
                                //更新融资历史
                                ProjectFinancingApproval oldProjectFinancingApproval = new ProjectFinancingApproval();
                                oldProjectFinancingApproval.setProjectSendLogId(projectId);
                                oldProjectFinancingApproval.setUserId(userId);
                                oldProjectFinancingApproval = projectFinancingApprovalMapper.selectOne(oldProjectFinancingApproval);
                                if (oldProjectFinancingApproval != null) {
                                    if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                        oldProjectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                                    }
                                    oldProjectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                                    oldProjectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                                    oldProjectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                                    if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                        oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                    }
                                    projectFinancingApprovalMapper.updateByPrimaryKey(oldProjectFinancingApproval);
                                } else {
                                    ProjectFinancingApproval projectFinancingApproval1 = new ProjectFinancingApproval();
                                    projectFinancingApproval1.setProjectSendLogId(projectId);
                                    projectFinancingApproval1.setUserId(userId);
                                    if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                        projectFinancingApproval1.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                                    }
                                    projectFinancingApproval1.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                                    projectFinancingApproval1.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                                    projectFinancingApproval1.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                                    if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                        projectFinancingApproval1.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                    }
//                                    projectFinancingApproval1.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                    projectFinancingApprovalMapper.insert(projectFinancingApproval1);
                                }
                                int foundersId = 0;
                                Founders founders = new Founders();
                                founders.setUserId(userId);
                                founders = foundersMapper.selectOne(founders);
                                if (founders != null) {
                                    foundersId = founders.getId();
                                } else {
                                    //插入
                                    Founders newFounders = new Founders();
                                    newFounders.setUserId(userId);
                                    foundersMapper.insert(newFounders);
                                    foundersId = newFounders.getId();

                                }

                                //更新教育经历
                                if (params.getTuisongxiangmubiao7educationa() != null && !"".equals(params.getTuisongxiangmubiao7educationa())) {
                                    String[] educations = params.getTuisongxiangmubiao7educationa().split(",");
                                    //删除之前的记录
                                    FoundersEducation old = new FoundersEducation();
                                    old.setFounderId(foundersId);
                                    foundersEducationMapper.delete(old);
                                    for (String education : educations) {
                                        FoundersEducation foundersEducation = new FoundersEducation();
                                        foundersEducation.setFounderId(foundersId);
                                        foundersEducation.setEducationExperience(education);
                                        foundersEducationMapper.insert(foundersEducation);
                                    }
                                }else{
                                    FoundersEducation foundersEducation = new FoundersEducation();
                                    foundersEducation.setFounderId(foundersId);
                                    foundersEducationMapper.delete( foundersEducation);
                                }

                                //更新工作经历
                                if (params.getTuisongxiangmubiao7workexperi() != null && !"".equals(params.getTuisongxiangmubiao7workexperi())) {
                                    String[] works = params.getTuisongxiangmubiao7workexperi().split(",");
                                    //删除之前的记录
                                    FoundersWork old = new FoundersWork();
                                    old.setFounderId(foundersId);
                                    foundersWorkMapper.delete(old);
                                    for (String work : works) {
                                        FoundersWork foundersWork = new FoundersWork();
                                        foundersWork.setFounderId(foundersId);
                                        foundersWork.setWorkExperience(work);
                                        foundersWorkMapper.insert(foundersWork);
                                    }
                                }else{
                                    FoundersWork foundersWork = new FoundersWork();
                                    foundersWork.setFounderId(foundersId);
                                    foundersWorkMapper.delete(foundersWork);
                                }

                            }else{
                                ProjectSendLogs p2 = new ProjectSendLogs();
                                p2.setCompanyShortName(params.getCompanyshortname());
                                p2 = projectSendLogsMapper.selectOne(p2);
                                if (p2 == null) {
                                    Users users = new Users();
                                    users.setId(userId);
                                    users = usersMapper.selectByPrimaryKey(users);
                                    users.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                                    users.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                                    users.setCompanyDuties(params.getTuisongxiangmubiao7assumeoffi());
                                    users.setActualName(params.getTuisongxiangmubiao7realname());
                                    users.setCity(params.getTuisongxiangmubiao7city());
                                    users.setDesc(params.getTuisongxiangmubiao7profile());
                                    if (params.getTuisongxiangmubiao7mailbox() != null && !"".equals(params.getTuisongxiangmubiao7mailbox())) {
                                        users.setEmail(params.getTuisongxiangmubiao7mailbox());
                                    }
                                    if (params.getTuisongxiangmubiao7wechatnumb() != null && !"".equals(params.getTuisongxiangmubiao7wechatnumb())) {
                                        users.setWechat(params.getTuisongxiangmubiao7wechatnumb());
                                    }
                                    usersMapper.updateByPrimaryKey(users);
                                    //更新项目投递记录
                                    ProjectSendLogs projectSendLogs = new ProjectSendLogs();
                                    projectSendLogs.setUserid(userId);
                                    projectSendLogs.setCity(params.getTuisongxiangmubiao7city());
                                    projectSendLogs.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                                    projectSendLogs.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                                    projectSendLogs.setField(params.getTuisongxiangmubiao7industryfi());
                                    if (params.getTuisongxiangmubiao7projecttag() != null && !"".equals(params.getTuisongxiangmubiao7projecttag())) {
                                        projectSendLogs.setProjectTags(params.getTuisongxiangmubiao7projecttag());
                                    }else{
                                        projectSendLogs.setProjectTags("");
                                    }
                                    projectSendLogs.setCreatTime(new Date());
                                    //logo图
                                    projectSendLogs.setCompanyLogo(params.getLogo());
                                    if (params.getCompanyshortname() != null || "".equals(params.getCompanyshortname())) {
                                        projectSendLogs.setCompanyShortName(params.getCompanyshortname());
                                    } else {
                                        result.setStatus(50001);
                                        result.setMessage("公司简称不能为空");
                                    }

                                    if (params.getOneintroduct() != null || "".equals(params.getOneintroduct())) {
                                        projectSendLogs.setCompanyOneSentenceIntroduct(params.getOneintroduct());
                                    } else {
                                        result.setStatus(50001);
                                        result.setMessage("一句话介绍不能为空");
                                    }

                                    if (params.getHighlights() != null || "".equals(params.getHighlights())) {
                                        projectSendLogs.setCompanyInvestmentHighlights(params.getHighlights());
                                    } else {
                                        result.setStatus(50001);
                                        result.setMessage("投资亮点不能为空");
                                    }
                                    projectSendLogs.setCompanyOfficialWebsite(params.getWebsite());


                                    projectSendLogsMapper.insert(projectSendLogs);
                                    int projectId = projectSendLogs.getId();
                                    ProjectSendTeamMember projectSendTeamMember =new ProjectSendTeamMember();
                                    projectSendTeamMember.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                                    projectSendTeamMember.setUserId(userId);
                                    projectSendTeamMember.setYn(0);
                                    List<ProjectSendTeamMember> list2 =projectSendTeamMemberMapper.select(projectSendTeamMember);
                                    if(list2.size() > 0){
                                        for (ProjectSendTeamMember pstme:list2){
                                            ProjectSendTeamMember projectTeamMemberEducation =new ProjectSendTeamMember();
                                            projectTeamMemberEducation.setYn(0);
                                            projectTeamMemberEducation.setUserId(userId);
                                            projectTeamMemberEducation.setMemberName(pstme.getMemberName());
                                            projectTeamMemberEducation.setProjectSendLogsId(-1);
                                            projectTeamMemberEducation.setCreateTime(new Date());
                                            projectTeamMemberEducation.setMemberDuties(pstme.getMemberDuties());
                                            projectTeamMemberEducation.setMemberDesc(pstme.getMemberDesc());
                                            projectTeamMemberEducation.setShareRatio(pstme.getShareRatio());
                                            projectSendTeamMemberMapper.insertSelective(projectTeamMemberEducation);

                                            ProjectSendTeamMemberEducation projectSendTeamMemberEducationForSearch1 = new ProjectSendTeamMemberEducation ();
                                            projectSendTeamMemberEducationForSearch1.setProjectSendTeamMemberId(pstme.getId());


                                            ProjectSendTeamMemberWork projectSendTeamMemberWorkForSearch1 = new ProjectSendTeamMemberWork();
                                            projectSendTeamMemberWorkForSearch1.setProjectSendTeamMemberId(pstme.getId());

                                            List<ProjectSendTeamMemberEducation> projectSendTeamMemberEducationList1 = projectSendTeamMemberEducationMapper.select(projectSendTeamMemberEducationForSearch1);
                                            List<ProjectSendTeamMemberWork> projectSendTeamMemberWorkList1 = projectSendTeamMemberWorkMapper.select(projectSendTeamMemberWorkForSearch1);
                                            //创建教育经历
                                            if (projectSendTeamMemberEducationList1.size() > 0){
                                                for (ProjectSendTeamMemberEducation pe:projectSendTeamMemberEducationList1){
                                                    ProjectSendTeamMemberEducation projectTeamMemberEducation1 =new ProjectSendTeamMemberEducation();
                                                    projectTeamMemberEducation1.setProjectSendTeamMemberId(projectTeamMemberEducation.getId());
                                                    projectTeamMemberEducation1.setEducationExperience(pe.getEducationExperience());

                                                    projectSendTeamMemberEducationMapper.insertSelective(projectTeamMemberEducation1);
                                                }
                                            }
                                            //创建工作经历
                                            if (projectSendTeamMemberWorkList1.size() > 0){
                                                for (ProjectSendTeamMemberWork pstmw:projectSendTeamMemberWorkList1){
                                                    ProjectSendTeamMemberWork projectTeamMemberWork = new ProjectSendTeamMemberWork();
                                                    projectTeamMemberWork.setProjectSendTeamMemberId(projectTeamMemberEducation.getId());
                                                    projectTeamMemberWork.setWorkExperience(pstmw.getWorkExperience());

                                                    projectSendTeamMemberWorkMapper.insert(projectTeamMemberWork);
                                                }
                                            }

                                        }
                                    }
                                    projectSendTeamMemberMapper.updateTeame(projectId,userId);
                                    ProjectFinancingHistory projectFinancingHistory =new ProjectFinancingHistory();
                                    projectFinancingHistory.setUserId(userId);
                                    projectFinancingHistory.setProjectSendLogId("-1");
                                    projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory);
                                    if(projectFinancingHistory != null) {
                                        projectFinancingHistoryMapper.updateHistory(projectId, userId);
                                    }else{
                                        ProjectFinancingHistory projectFinancingHistory2 =new ProjectFinancingHistory();
                                        projectFinancingHistory2.setUserId(userId);
                                        projectFinancingHistory2.setProjectSendLogId(params.getXmid());
                                        projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory2);
                                        if( projectFinancingHistory !=null) {
                                            ProjectFinancingHistory projectFinancingHistory1 = new ProjectFinancingHistory();
                                            projectFinancingHistory1.setProjectSendLogId(String.valueOf(projectId));
                                            projectFinancingHistory1.setUserId(userId);
                                            projectFinancingHistory1.setHistory(projectFinancingHistory.getHistory());
                                            projectFinancingHistoryMapper.insert(projectFinancingHistory1);
                                        }
                                    }
                                    //相似竞品
                                    ProjectSendLogCompeting projectSendLogCompeting = new ProjectSendLogCompeting();
                                    List<ProjectSendLogCompeting> dataLogWorklist = new Page<ProjectSendLogCompeting>();
                                    String competing = params.getCompeting();
                                    projectSendLogCompeting.setProjectSendLogsId(projectId);
                                    List<ProjectSendLogCompeting> one = projectSendLogCompetingMapper.select(projectSendLogCompeting);
                                    if(competing ==null || "".equals(competing)){
                                        projectSendLogCompetingMapper.delete(projectSendLogCompeting);
                                    }else{
                                        if (one.size()>0) {
                                            ProjectSendLogCompeting dataLogEducation2 = new ProjectSendLogCompeting();
                                            dataLogEducation2.setProjectSendLogsId(projectId);
                                            projectSendLogCompetingMapper.delete(dataLogEducation2);
                                            String[] fieldArry = competing.split(",");
                                            for (int b = 0; b < fieldArry.length; b++) {
                                                ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                                projectSendLogCompeting1.setCompetingProductName(fieldArry[b]);
                                                projectSendLogCompeting1.setProjectSendLogsId(projectId);
                                                dataLogWorklist.add(projectSendLogCompeting1);
                                            }
                                            projectSendLogCompetingMapper.insertList(dataLogWorklist);
                                        } else {
                                            String[] fieldArry = competing.split(",");
                                            for (int b = 0; b < fieldArry.length; b++) {
                                                ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                                projectSendLogCompeting1.setCompetingProductName(fieldArry[b]);
                                                projectSendLogCompeting1.setProjectSendLogsId(projectId);
                                                dataLogWorklist.add(projectSendLogCompeting1);
                                            }
                                            projectSendLogCompetingMapper.insertList(dataLogWorklist);
                                        }}
                                    //更新融资申请表
                                    ProjectFinancingApproval oldProjectFinancingApproval = new ProjectFinancingApproval();
                                    oldProjectFinancingApproval.setProjectSendLogId(projectId);
                                    oldProjectFinancingApproval.setUserId(userId);
                                    oldProjectFinancingApproval = projectFinancingApprovalMapper.selectOne(oldProjectFinancingApproval);
                                    if (oldProjectFinancingApproval != null) {
                                        if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                            oldProjectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                                        }
                                        oldProjectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                                        oldProjectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                                        oldProjectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                                        if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                            oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                        }
//                                        oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                        projectFinancingApprovalMapper.updateByPrimaryKey(oldProjectFinancingApproval);
                                    } else {
                                        ProjectFinancingApproval projectFinancingApproval = new ProjectFinancingApproval();
                                        projectFinancingApproval.setProjectSendLogId(projectId);
                                        projectFinancingApproval.setUserId(userId);
                                        if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                            projectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                                        }
                                        projectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                                        projectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                                        projectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                                        if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                            projectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                        }
//                                        projectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                        projectFinancingApprovalMapper.insert(projectFinancingApproval);
                                        //更新投递项目-机构关系表
                                        //插入
                                        String[] investmentIds = params.getTsid().split(",");
                                        for (String investmentId : investmentIds) {
                                            ProjectFinancingInvestmentInstitutionRelationship relationship = new ProjectFinancingInvestmentInstitutionRelationship();
                                            relationship.setProjectSendLogId(projectId);
                                            relationship.setInvestmentInstitutionId(Integer.parseInt(investmentId));
                                            relationship.setAssociatedTime(new Date());
                                            projectFinancingInvestmentInstitutionRelationshipMapper.insert(relationship);
                                        }
                                    }

                                    //更新创始人记录
                                    int foundersId = 0;
                                    Founders founders = new Founders();
                                    founders.setUserId(userId);
                                    founders = foundersMapper.selectOne(founders);
                                    if (founders != null) {
                                        foundersId = founders.getId();
                                    } else {
                                        //插入
                                        Founders newFounders = new Founders();
                                        newFounders.setUserId(userId);
                                        foundersMapper.insert(newFounders);
                                        foundersId = newFounders.getId();
                                    }

                                    //更新教育经历
                                    if (params.getTuisongxiangmubiao7educationa() != null && !"".equals(params.getTuisongxiangmubiao7educationa())) {
                                        String[] educations = params.getTuisongxiangmubiao7educationa().split(",");
                                        //删除之前的记录
                                        FoundersEducation old = new FoundersEducation();
                                        old.setFounderId(foundersId);
                                        foundersEducationMapper.delete(old);
                                        for (String education : educations) {
                                            FoundersEducation foundersEducation = new FoundersEducation();
                                            foundersEducation.setFounderId(foundersId);
                                            foundersEducation.setEducationExperience(education);
                                            foundersEducationMapper.insert(foundersEducation);
                                        }
                                    }else{
                                        FoundersEducation foundersEducation = new FoundersEducation();
                                        foundersEducation.setFounderId(foundersId);
                                        foundersEducationMapper.delete( foundersEducation);
                                    }

                                    //更新工作经历
                                    if (params.getTuisongxiangmubiao7workexperi() != null && !"".equals(params.getTuisongxiangmubiao7workexperi())) {
                                        String[] works = params.getTuisongxiangmubiao7workexperi().split(",");
                                        //删除之前的记录
                                        FoundersWork old = new FoundersWork();
                                        old.setFounderId(foundersId);
                                        foundersWorkMapper.delete(old);
                                        for (String work : works) {
                                            FoundersWork foundersWork = new FoundersWork();
                                            foundersWork.setFounderId(foundersId);
                                            foundersWork.setWorkExperience(work);
                                            foundersWorkMapper.insert(foundersWork);
                                        }
                                    }else{
                                        FoundersWork foundersWork = new FoundersWork();
                                        foundersWork.setFounderId(foundersId);
                                        foundersWorkMapper.delete(foundersWork);
                                    }
                                } else {
                                    result.setStatus(5002);
                                    result.setMessage("不能提交重复的项目");
                                }
                            }
                        }else{
                            ProjectSendLogs p2 = new ProjectSendLogs();
                            p2.setCompanyShortName(params.getCompanyshortname());
                            p2 = projectSendLogsMapper.selectOne(p2);
                            if (p2 == null) {
                                Users users = new Users();
                                users.setId(userId);
                                users = usersMapper.selectByPrimaryKey(users);
                                users.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                                users.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                                users.setCompanyDuties(params.getTuisongxiangmubiao7assumeoffi());
                                users.setActualName(params.getTuisongxiangmubiao7realname());
                                users.setCity(params.getTuisongxiangmubiao7city());
                                users.setDesc(params.getTuisongxiangmubiao7profile());
                                if (params.getTuisongxiangmubiao7mailbox() != null && !"".equals(params.getTuisongxiangmubiao7mailbox())) {
                                    users.setEmail(params.getTuisongxiangmubiao7mailbox());
                                }
                                if (params.getTuisongxiangmubiao7wechatnumb() != null && !"".equals(params.getTuisongxiangmubiao7wechatnumb())) {
                                    users.setWechat(params.getTuisongxiangmubiao7wechatnumb());
                                }
                                usersMapper.updateByPrimaryKey(users);
                                //更新项目投递记录
                                ProjectSendLogs projectSendLogs = new ProjectSendLogs();
                                projectSendLogs.setUserid(userId);
                                projectSendLogs.setCity(params.getTuisongxiangmubiao7city());
                                projectSendLogs.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                                projectSendLogs.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                                projectSendLogs.setField(params.getTuisongxiangmubiao7industryfi());
                                if (params.getTuisongxiangmubiao7projecttag() != null && !"".equals(params.getTuisongxiangmubiao7projecttag())) {
                                    projectSendLogs.setProjectTags(params.getTuisongxiangmubiao7projecttag());
                                }else{
                                    projectSendLogs.setProjectTags("");
                                }
                                projectSendLogs.setCreatTime(new Date());
                                //logo图
                                projectSendLogs.setCompanyLogo(params.getLogo());
                                if (params.getCompanyshortname() != null || "".equals(params.getCompanyshortname())) {
                                    projectSendLogs.setCompanyShortName(params.getCompanyshortname());
                                } else {
                                    result.setStatus(50001);
                                    result.setMessage("公司简称不能为空");
                                }

                                if (params.getOneintroduct() != null || "".equals(params.getOneintroduct())) {
                                    projectSendLogs.setCompanyOneSentenceIntroduct(params.getOneintroduct());
                                } else {
                                    result.setStatus(50001);
                                    result.setMessage("一句话介绍不能为空");
                                }

                                if (params.getHighlights() != null || "".equals(params.getHighlights())) {
                                    projectSendLogs.setCompanyInvestmentHighlights(params.getHighlights());
                                } else {
                                    result.setStatus(50001);
                                    result.setMessage("投资亮点不能为空");
                                }
                                projectSendLogs.setCompanyOfficialWebsite(params.getWebsite());


                                projectSendLogsMapper.insert(projectSendLogs);
                                int projectId = projectSendLogs.getId();
                                ProjectSendTeamMember projectSendTeamMember =new ProjectSendTeamMember();
                                projectSendTeamMember.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                                projectSendTeamMember.setUserId(userId);
                                projectSendTeamMember.setYn(0);
                                List<ProjectSendTeamMember> list2 =projectSendTeamMemberMapper.select(projectSendTeamMember);
                                if(list2.size() > 0){
                                    for (ProjectSendTeamMember pstme:list2){
                                        ProjectSendTeamMember projectTeamMemberEducation =new ProjectSendTeamMember();
                                        projectTeamMemberEducation.setYn(0);
                                        projectTeamMemberEducation.setUserId(userId);
                                        projectTeamMemberEducation.setMemberName(pstme.getMemberName());
                                        projectTeamMemberEducation.setProjectSendLogsId(-1);
                                        projectTeamMemberEducation.setCreateTime(new Date());
                                        projectTeamMemberEducation.setMemberDuties(pstme.getMemberDuties());
                                        projectTeamMemberEducation.setMemberDesc(pstme.getMemberDesc());
                                        projectTeamMemberEducation.setShareRatio(pstme.getShareRatio());
                                        projectSendTeamMemberMapper.insertSelective(projectTeamMemberEducation);

                                        ProjectSendTeamMemberEducation projectSendTeamMemberEducationForSearch1 = new ProjectSendTeamMemberEducation ();
                                        projectSendTeamMemberEducationForSearch1.setProjectSendTeamMemberId(pstme.getId());


                                        ProjectSendTeamMemberWork projectSendTeamMemberWorkForSearch1 = new ProjectSendTeamMemberWork();
                                        projectSendTeamMemberWorkForSearch1.setProjectSendTeamMemberId(pstme.getId());

                                        List<ProjectSendTeamMemberEducation> projectSendTeamMemberEducationList1 = projectSendTeamMemberEducationMapper.select(projectSendTeamMemberEducationForSearch1);
                                        List<ProjectSendTeamMemberWork> projectSendTeamMemberWorkList1 = projectSendTeamMemberWorkMapper.select(projectSendTeamMemberWorkForSearch1);
                                        //创建教育经历
                                        if (projectSendTeamMemberEducationList1.size() > 0){
                                            for (ProjectSendTeamMemberEducation pe:projectSendTeamMemberEducationList1){
                                                ProjectSendTeamMemberEducation projectTeamMemberEducation1 =new ProjectSendTeamMemberEducation();
                                                projectTeamMemberEducation1.setProjectSendTeamMemberId(projectTeamMemberEducation.getId());
                                                projectTeamMemberEducation1.setEducationExperience(pe.getEducationExperience());

                                                projectSendTeamMemberEducationMapper.insertSelective(projectTeamMemberEducation1);
                                            }
                                        }
                                        //创建工作经历
                                        if (projectSendTeamMemberWorkList1.size() > 0){
                                            for (ProjectSendTeamMemberWork pstmw:projectSendTeamMemberWorkList1){
                                                ProjectSendTeamMemberWork projectTeamMemberWork = new ProjectSendTeamMemberWork();
                                                projectTeamMemberWork.setProjectSendTeamMemberId(projectTeamMemberEducation.getId());
                                                projectTeamMemberWork.setWorkExperience(pstmw.getWorkExperience());

                                                projectSendTeamMemberWorkMapper.insert(projectTeamMemberWork);
                                            }
                                        }

                                    }
                                }
                                projectSendTeamMemberMapper.updateTeame(projectId,userId);
                                //融资历史
                                ProjectFinancingHistory projectFinancingHistory =new ProjectFinancingHistory();
                                projectFinancingHistory.setUserId(userId);
                                projectFinancingHistory.setProjectSendLogId("-1");
                                projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory);
                                if(projectFinancingHistory != null) {
                                    projectFinancingHistoryMapper.updateHistory(projectId, userId);
                                }else{
                                    ProjectFinancingHistory projectFinancingHistory2 =new ProjectFinancingHistory();
                                    projectFinancingHistory2.setUserId(userId);
                                    projectFinancingHistory2.setProjectSendLogId(params.getXmid());
                                    projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory2);
                                    if( projectFinancingHistory !=null) {
                                        ProjectFinancingHistory projectFinancingHistory1 = new ProjectFinancingHistory();
                                        projectFinancingHistory1.setProjectSendLogId(String.valueOf(projectId));
                                        projectFinancingHistory1.setUserId(userId);
                                        projectFinancingHistory1.setHistory(projectFinancingHistory.getHistory());
                                        projectFinancingHistoryMapper.insert(projectFinancingHistory1);
                                    }
                                }
                                //相似竞品
                                ProjectSendLogCompeting projectSendLogCompeting = new ProjectSendLogCompeting();
                                List<ProjectSendLogCompeting> dataLogWorklist = new Page<ProjectSendLogCompeting>();
                                String competing = params.getCompeting();
                                projectSendLogCompeting.setProjectSendLogsId(projectId);
                                List<ProjectSendLogCompeting> one = projectSendLogCompetingMapper.select(projectSendLogCompeting);
                                if(competing ==null || "".equals(competing)){
                                    projectSendLogCompetingMapper.delete(projectSendLogCompeting);
                                }else{
                                    if (one.size()>0) {
                                        ProjectSendLogCompeting dataLogEducation2 = new ProjectSendLogCompeting();
                                        dataLogEducation2.setProjectSendLogsId(projectId);
                                        projectSendLogCompetingMapper.delete(dataLogEducation2);
                                        String[] fieldArry = competing.split(",");
                                        for (int b = 0; b < fieldArry.length; b++) {
                                            ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                            projectSendLogCompeting1.setCompetingProductName(fieldArry[b]);
                                            projectSendLogCompeting1.setProjectSendLogsId(projectId);
                                            dataLogWorklist.add(projectSendLogCompeting1);
                                        }
                                        projectSendLogCompetingMapper.insertList(dataLogWorklist);
                                    } else {
                                        String[] fieldArry = competing.split(",");
                                        for (int b = 0; b < fieldArry.length; b++) {
                                            ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                            projectSendLogCompeting1.setCompetingProductName(fieldArry[b]);
                                            projectSendLogCompeting1.setProjectSendLogsId(projectId);
                                            dataLogWorklist.add(projectSendLogCompeting1);
                                        }
                                        projectSendLogCompetingMapper.insertList(dataLogWorklist);
                                    }}
                                //更新融资申请表
                                ProjectFinancingApproval oldProjectFinancingApproval = new ProjectFinancingApproval();
                                oldProjectFinancingApproval.setProjectSendLogId(projectId);
                                oldProjectFinancingApproval.setUserId(userId);
                                oldProjectFinancingApproval = projectFinancingApprovalMapper.selectOne(oldProjectFinancingApproval);
                                if (oldProjectFinancingApproval != null) {
                                    if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                        oldProjectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                                    }
                                    oldProjectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                                    oldProjectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                                    oldProjectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                                    if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                        oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                    }
//                                    oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                    projectFinancingApprovalMapper.updateByPrimaryKey(oldProjectFinancingApproval);
                                } else {
                                    ProjectFinancingApproval projectFinancingApproval = new ProjectFinancingApproval();
                                    projectFinancingApproval.setProjectSendLogId(projectId);
                                    projectFinancingApproval.setUserId(userId);
                                    if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                        projectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                                    }
                                    projectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                                    projectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                                    projectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                                    if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                        projectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                    }
//                                    projectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                    projectFinancingApprovalMapper.insert(projectFinancingApproval);
                                    //更新投递项目-机构关系表
                                    //插入
                                    String[] investmentIds = params.getTsid().split(",");
                                    for (String investmentId : investmentIds) {
                                        ProjectFinancingInvestmentInstitutionRelationship relationship = new ProjectFinancingInvestmentInstitutionRelationship();
                                        relationship.setProjectSendLogId(projectId);
                                        relationship.setInvestmentInstitutionId(Integer.parseInt(investmentId));
                                        relationship.setAssociatedTime(new Date());
                                        projectFinancingInvestmentInstitutionRelationshipMapper.insert(relationship);
                                    }
                                }

                                //更新创始人记录
                                int foundersId = 0;
                                Founders founders = new Founders();
                                founders.setUserId(userId);
                                founders = foundersMapper.selectOne(founders);
                                if (founders != null) {
                                    foundersId = founders.getId();
                                } else {
                                    //插入
                                    Founders newFounders = new Founders();
                                    newFounders.setUserId(userId);
                                    foundersMapper.insert(newFounders);
                                    foundersId = newFounders.getId();
                                }

                                //更新教育经历
                                if (params.getTuisongxiangmubiao7educationa() != null && !"".equals(params.getTuisongxiangmubiao7educationa())) {
                                    String[] educations = params.getTuisongxiangmubiao7educationa().split(",");
                                    //删除之前的记录
                                    FoundersEducation old = new FoundersEducation();
                                    old.setFounderId(foundersId);
                                    foundersEducationMapper.delete(old);
                                    for (String education : educations) {
                                        FoundersEducation foundersEducation = new FoundersEducation();
                                        foundersEducation.setFounderId(foundersId);
                                        foundersEducation.setEducationExperience(education);
                                        foundersEducationMapper.insert(foundersEducation);
                                    }
                                }else{
                                    FoundersEducation foundersEducation = new FoundersEducation();
                                    foundersEducation.setFounderId(foundersId);
                                    foundersEducationMapper.delete( foundersEducation);
                                }

                                //更新工作经历
                                if (params.getTuisongxiangmubiao7workexperi() != null && !"".equals(params.getTuisongxiangmubiao7workexperi())) {
                                    String[] works = params.getTuisongxiangmubiao7workexperi().split(",");
                                    //删除之前的记录
                                    FoundersWork old = new FoundersWork();
                                    old.setFounderId(foundersId);
                                    foundersWorkMapper.delete(old);
                                    for (String work : works) {
                                        FoundersWork foundersWork = new FoundersWork();
                                        foundersWork.setFounderId(foundersId);
                                        foundersWork.setWorkExperience(work);
                                        foundersWorkMapper.insert(foundersWork);
                                    }
                                }else{
                                    FoundersWork foundersWork = new FoundersWork();
                                    foundersWork.setFounderId(foundersId);
                                    foundersWorkMapper.delete(foundersWork);
                                }
                            } else {
                                result.setStatus(5002);
                                result.setMessage("不能提交重复的项目");
                            }
                        }

                    }else{
                        ProjectSendLogs p2 = new ProjectSendLogs();
                        p2.setCompanyShortName(params.getCompanyshortname());
                        p2 = projectSendLogsMapper.selectOne(p2);
                        if (p2 == null) {
                            Users users = new Users();
                            users.setId(userId);
                            users = usersMapper.selectByPrimaryKey(users);
                            users.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                            users.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                            users.setCompanyDuties(params.getTuisongxiangmubiao7assumeoffi());
                            users.setActualName(params.getTuisongxiangmubiao7realname());
                            users.setCity(params.getTuisongxiangmubiao7city());
                            users.setDesc(params.getTuisongxiangmubiao7profile());
                            if (params.getTuisongxiangmubiao7mailbox() != null && !"".equals(params.getTuisongxiangmubiao7mailbox())) {
                                users.setEmail(params.getTuisongxiangmubiao7mailbox());
                            }
                            if (params.getTuisongxiangmubiao7wechatnumb() != null && !"".equals(params.getTuisongxiangmubiao7wechatnumb())) {
                                users.setWechat(params.getTuisongxiangmubiao7wechatnumb());
                            }
                           /* if ("投资人".equals(params.getTuisongxiangmubiao7identityty())) {
                                users.setIdentityType(0);
                            }
                            if ("创业者".equals(params.getTuisongxiangmubiao7identityty())) {
                                users.setIdentityType(1);
                            }
                            if ("产业公司".equals(params.getTuisongxiangmubiao7identityty())) {
                                users.setIdentityType(2);
                            }
                            if ("媒体".equals(params.getTuisongxiangmubiao7identityty())) {
                                users.setIdentityType(3);
                            }
                            if ("服务机构".equals(params.getTuisongxiangmubiao7identityty())) {
                                users.setIdentityType(4);
                            }
                            if ("政府机构".equals(params.getTuisongxiangmubiao7identityty())) {
                                users.setIdentityType(5);
                            }*/
                            usersMapper.updateByPrimaryKey(users);
                            //更新项目投递记录
                            ProjectSendLogs projectSendLogs = new ProjectSendLogs();
                            projectSendLogs.setUserid(userId);
                            projectSendLogs.setCity(params.getTuisongxiangmubiao7city());
                            projectSendLogs.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                            projectSendLogs.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                            projectSendLogs.setField(params.getTuisongxiangmubiao7industryfi());
                            if (params.getTuisongxiangmubiao7projecttag() != null && !"".equals(params.getTuisongxiangmubiao7projecttag())) {
                                projectSendLogs.setProjectTags(params.getTuisongxiangmubiao7projecttag());
                            }else{
                                projectSendLogs.setProjectTags("");
                            }
                            projectSendLogs.setCreatTime(new Date());
                            //logo图
                            projectSendLogs.setCompanyLogo(params.getLogo());
                            if (params.getCompanyshortname() != null || "".equals(params.getCompanyshortname())) {
                                projectSendLogs.setCompanyShortName(params.getCompanyshortname());
                            } else {
                                result.setStatus(50001);
                                result.setMessage("公司简称不能为空");
                            }

                            if (params.getOneintroduct() != null || "".equals(params.getOneintroduct())) {
                                projectSendLogs.setCompanyOneSentenceIntroduct(params.getOneintroduct());
                            } else {
                                result.setStatus(50001);
                                result.setMessage("一句话介绍不能为空");
                            }

                            if (params.getHighlights() != null || "".equals(params.getHighlights())) {
                                projectSendLogs.setCompanyInvestmentHighlights(params.getHighlights());
                            } else {
                                result.setStatus(50001);
                                result.setMessage("投资亮点不能为空");
                            }
                            projectSendLogs.setCompanyOfficialWebsite(params.getWebsite());


                            projectSendLogsMapper.insert(projectSendLogs);
                            int projectId = projectSendLogs.getId();
                            ProjectSendTeamMember projectSendTeamMember =new ProjectSendTeamMember();
                            projectSendTeamMember.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                            projectSendTeamMember.setUserId(userId);
                            projectSendTeamMember.setYn(0);
                            List<ProjectSendTeamMember> list2 =projectSendTeamMemberMapper.select(projectSendTeamMember);
                            if(list2.size() > 0){
                                for (ProjectSendTeamMember pstme:list2){
                                    ProjectSendTeamMember projectTeamMemberEducation =new ProjectSendTeamMember();
                                    projectTeamMemberEducation.setYn(0);
                                    projectTeamMemberEducation.setUserId(userId);
                                    projectTeamMemberEducation.setMemberName(pstme.getMemberName());
                                    projectTeamMemberEducation.setProjectSendLogsId(-1);
                                    projectTeamMemberEducation.setCreateTime(new Date());
                                    projectTeamMemberEducation.setMemberDuties(pstme.getMemberDuties());
                                    projectTeamMemberEducation.setMemberDesc(pstme.getMemberDesc());
                                    projectTeamMemberEducation.setShareRatio(pstme.getShareRatio());
                                    projectSendTeamMemberMapper.insertSelective(projectTeamMemberEducation);

                                    ProjectSendTeamMemberEducation projectSendTeamMemberEducationForSearch1 = new ProjectSendTeamMemberEducation ();
                                    projectSendTeamMemberEducationForSearch1.setProjectSendTeamMemberId(pstme.getId());


                                    ProjectSendTeamMemberWork projectSendTeamMemberWorkForSearch1 = new ProjectSendTeamMemberWork();
                                    projectSendTeamMemberWorkForSearch1.setProjectSendTeamMemberId(pstme.getId());

                                    List<ProjectSendTeamMemberEducation> projectSendTeamMemberEducationList1 = projectSendTeamMemberEducationMapper.select(projectSendTeamMemberEducationForSearch1);
                                    List<ProjectSendTeamMemberWork> projectSendTeamMemberWorkList1 = projectSendTeamMemberWorkMapper.select(projectSendTeamMemberWorkForSearch1);
                                    //创建教育经历
                                    if (projectSendTeamMemberEducationList1.size() > 0){
                                        for (ProjectSendTeamMemberEducation pe:projectSendTeamMemberEducationList1){
                                            ProjectSendTeamMemberEducation projectTeamMemberEducation1 =new ProjectSendTeamMemberEducation();
                                            projectTeamMemberEducation1.setProjectSendTeamMemberId(projectTeamMemberEducation.getId());
                                            projectTeamMemberEducation1.setEducationExperience(pe.getEducationExperience());

                                            projectSendTeamMemberEducationMapper.insertSelective(projectTeamMemberEducation1);
                                        }
                                    }
                                    //创建工作经历
                                    if (projectSendTeamMemberWorkList1.size() > 0){
                                        for (ProjectSendTeamMemberWork pstmw:projectSendTeamMemberWorkList1){
                                            ProjectSendTeamMemberWork projectTeamMemberWork = new ProjectSendTeamMemberWork();
                                            projectTeamMemberWork.setProjectSendTeamMemberId(projectTeamMemberEducation.getId());
                                            projectTeamMemberWork.setWorkExperience(pstmw.getWorkExperience());

                                            projectSendTeamMemberWorkMapper.insert(projectTeamMemberWork);
                                        }
                                    }

                                }
                            }
                            projectSendTeamMemberMapper.updateTeame(projectId,userId);
                            //更新历史融资历史
                            ProjectFinancingHistory projectFinancingHistory =new ProjectFinancingHistory();
                            projectFinancingHistory.setUserId(userId);
                            projectFinancingHistory.setProjectSendLogId("-1");
                            projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory);
                            if(projectFinancingHistory != null) {
                                projectFinancingHistoryMapper.updateHistory(projectId, userId);
                            }else{
                                ProjectFinancingHistory projectFinancingHistory2 =new ProjectFinancingHistory();
                                projectFinancingHistory2.setUserId(userId);
                                projectFinancingHistory2.setProjectSendLogId(params.getXmid());
                                projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory2);
                                if( projectFinancingHistory !=null) {
                                    ProjectFinancingHistory projectFinancingHistory1 = new ProjectFinancingHistory();
                                    projectFinancingHistory1.setProjectSendLogId(String.valueOf(projectId));
                                    projectFinancingHistory1.setUserId(userId);
                                    projectFinancingHistory1.setHistory(projectFinancingHistory.getHistory());
                                    projectFinancingHistoryMapper.insert(projectFinancingHistory1);
                                }
                            }
                            //相似竞品
                            ProjectSendLogCompeting projectSendLogCompeting = new ProjectSendLogCompeting();
                            List<ProjectSendLogCompeting> dataLogWorklist = new Page<ProjectSendLogCompeting>();
                            String competing = params.getCompeting();
                            projectSendLogCompeting.setProjectSendLogsId(projectId);
                            List<ProjectSendLogCompeting> one = projectSendLogCompetingMapper.select(projectSendLogCompeting);
                            if(competing ==null || "".equals(competing)){
                                projectSendLogCompetingMapper.delete(projectSendLogCompeting);
                            }else{
                                if (one.size()>0) {
                                    ProjectSendLogCompeting dataLogEducation2 = new ProjectSendLogCompeting();
                                    dataLogEducation2.setProjectSendLogsId(projectId);
                                    projectSendLogCompetingMapper.delete(dataLogEducation2);
                                    String[] fieldArry = competing.split(",");
                                    for (int b = 0; b < fieldArry.length; b++) {
                                        ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                        projectSendLogCompeting1.setCompetingProductName(fieldArry[b]);
                                        projectSendLogCompeting1.setProjectSendLogsId(projectId);
                                        dataLogWorklist.add(projectSendLogCompeting1);
                                    }
                                    projectSendLogCompetingMapper.insertList(dataLogWorklist);
                                } else {
                                    String[] fieldArry = competing.split(",");
                                    for (int b = 0; b < fieldArry.length; b++) {
                                        ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                        projectSendLogCompeting1.setCompetingProductName(fieldArry[b]);
                                        projectSendLogCompeting1.setProjectSendLogsId(projectId);
                                        dataLogWorklist.add(projectSendLogCompeting1);
                                    }
                                    projectSendLogCompetingMapper.insertList(dataLogWorklist);
                                }}
                            //更新融资申请表
                            ProjectFinancingApproval oldProjectFinancingApproval = new ProjectFinancingApproval();
                            oldProjectFinancingApproval.setProjectSendLogId(projectId);
                            oldProjectFinancingApproval.setUserId(userId);
                            oldProjectFinancingApproval = projectFinancingApprovalMapper.selectOne(oldProjectFinancingApproval);
                            if (oldProjectFinancingApproval != null) {
                                if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                    oldProjectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                                }
                                oldProjectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                                oldProjectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                                oldProjectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                                if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                    oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                }
//                                oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                projectFinancingApprovalMapper.updateByPrimaryKey(oldProjectFinancingApproval);
                            } else {
                                ProjectFinancingApproval projectFinancingApproval = new ProjectFinancingApproval();
                                projectFinancingApproval.setProjectSendLogId(projectId);
                                projectFinancingApproval.setUserId(userId);
                                if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                    projectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                                }
                                projectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                                projectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                                projectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                                if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                    projectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                }
//                                projectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                                projectFinancingApprovalMapper.insert(projectFinancingApproval);
                                //更新投递项目-机构关系表
                                //插入
                                String[] investmentIds = params.getTsid().split(",");
                                for (String investmentId : investmentIds) {
                                    ProjectFinancingInvestmentInstitutionRelationship relationship = new ProjectFinancingInvestmentInstitutionRelationship();
                                    relationship.setProjectSendLogId(projectId);
                                    relationship.setInvestmentInstitutionId(Integer.parseInt(investmentId));
                                    relationship.setAssociatedTime(new Date());
                                    projectFinancingInvestmentInstitutionRelationshipMapper.insert(relationship);
                                }
                            }

                            //更新创始人记录
                            int foundersId = 0;
                            Founders founders = new Founders();
                            founders.setUserId(userId);
                            founders = foundersMapper.selectOne(founders);
                            if (founders != null) {
                                foundersId = founders.getId();
                            } else {
                                //插入
                                Founders newFounders = new Founders();
                                newFounders.setUserId(userId);
                                foundersMapper.insert(newFounders);
                                foundersId = newFounders.getId();
                            }

                            //更新教育经历
                            if (params.getTuisongxiangmubiao7educationa() != null && !"".equals(params.getTuisongxiangmubiao7educationa())) {
                                String[] educations = params.getTuisongxiangmubiao7educationa().split(",");
                                //删除之前的记录
                                FoundersEducation old = new FoundersEducation();
                                old.setFounderId(foundersId);
                                foundersEducationMapper.delete(old);
                                for (String education : educations) {
                                    FoundersEducation foundersEducation = new FoundersEducation();
                                    foundersEducation.setFounderId(foundersId);
                                    foundersEducation.setEducationExperience(education);
                                    foundersEducationMapper.insert(foundersEducation);
                                }
                            }else{
                                FoundersEducation foundersEducation = new FoundersEducation();
                                foundersEducation.setFounderId(foundersId);
                                foundersEducationMapper.delete( foundersEducation);
                            }

                            //更新工作经历
                            if (params.getTuisongxiangmubiao7workexperi() != null && !"".equals(params.getTuisongxiangmubiao7workexperi())) {
                                String[] works = params.getTuisongxiangmubiao7workexperi().split(",");
                                //删除之前的记录
                                FoundersWork old = new FoundersWork();
                                old.setFounderId(foundersId);
                                foundersWorkMapper.delete(old);
                                for (String work : works) {
                                    FoundersWork foundersWork = new FoundersWork();
                                    foundersWork.setFounderId(foundersId);
                                    foundersWork.setWorkExperience(work);
                                    foundersWorkMapper.insert(foundersWork);
                                }
                            }else{
                                FoundersWork foundersWork = new FoundersWork();
                                foundersWork.setFounderId(foundersId);
                                foundersWorkMapper.delete(foundersWork);
                            }
                        } else {
                            result.setStatus(5002);
                            result.setMessage("不能提交重复的项目");
                        }
                    }
                    //条件1
                }else{
                	ProjectSendLogs p2 = new ProjectSendLogs();
                    p2.setCompanyShortName(params.getCompanyshortname());
                    p2 = projectSendLogsMapper.selectOne(p2);
                    if (p2 == null) {
                        Users users = new Users();
                        users.setId(userId);
                        users = usersMapper.selectByPrimaryKey(users);
                        users.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                        users.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                        users.setCompanyDuties(params.getTuisongxiangmubiao7assumeoffi());
                        users.setActualName(params.getTuisongxiangmubiao7realname());
                        users.setCity(params.getTuisongxiangmubiao7city());
                        users.setDesc(params.getTuisongxiangmubiao7profile());
                        if (params.getTuisongxiangmubiao7mailbox() != null && !"".equals(params.getTuisongxiangmubiao7mailbox())) {
                            users.setEmail(params.getTuisongxiangmubiao7mailbox());
                        }
                        if (params.getTuisongxiangmubiao7wechatnumb() != null && !"".equals(params.getTuisongxiangmubiao7wechatnumb())) {
                            users.setWechat(params.getTuisongxiangmubiao7wechatnumb());
                        }
                        usersMapper.updateByPrimaryKey(users);
                        //更新项目投递记录
                        ProjectSendLogs projectSendLogs = new ProjectSendLogs();
                        projectSendLogs.setUserid(userId);
                        projectSendLogs.setCity(params.getTuisongxiangmubiao7city());
                        projectSendLogs.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                        projectSendLogs.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                        projectSendLogs.setField(params.getTuisongxiangmubiao7industryfi());
                        if (params.getTuisongxiangmubiao7projecttag() != null && !"".equals(params.getTuisongxiangmubiao7projecttag())) {
                            projectSendLogs.setProjectTags(params.getTuisongxiangmubiao7projecttag());
                        }else{
                            projectSendLogs.setProjectTags("");
                        }
                        projectSendLogs.setCreatTime(new Date());
                        //logo图
                        projectSendLogs.setCompanyLogo(params.getLogo());
                        if (params.getCompanyshortname() != null || "".equals(params.getCompanyshortname())) {
                            projectSendLogs.setCompanyShortName(params.getCompanyshortname());
                        } else {
                            result.setStatus(50001);
                            result.setMessage("公司简称不能为空");
                        }

                        if (params.getOneintroduct() != null || "".equals(params.getOneintroduct())) {
                            projectSendLogs.setCompanyOneSentenceIntroduct(params.getOneintroduct());
                        } else {
                            result.setStatus(50001);
                            result.setMessage("一句话介绍不能为空");
                        }

                        if (params.getHighlights() != null || "".equals(params.getHighlights())) {
                            projectSendLogs.setCompanyInvestmentHighlights(params.getHighlights());
                        } else {
                            result.setStatus(50001);
                            result.setMessage("投资亮点不能为空");
                        }
                        projectSendLogs.setCompanyOfficialWebsite(params.getWebsite());


                        projectSendLogsMapper.insert(projectSendLogs);
                        int projectId = projectSendLogs.getId();
                        ProjectSendTeamMember projectSendTeamMember =new ProjectSendTeamMember();
                        projectSendTeamMember.setProjectSendLogsId(Integer.parseInt(params.getXmid()));
                        projectSendTeamMember.setUserId(userId);
                        projectSendTeamMember.setYn(0);
                        List<ProjectSendTeamMember> list2 =projectSendTeamMemberMapper.select(projectSendTeamMember);
                        if(list2.size() > 0){
                            for (ProjectSendTeamMember pstme:list2){
                                ProjectSendTeamMember projectTeamMemberEducation =new ProjectSendTeamMember();
                                projectTeamMemberEducation.setYn(0);
                                projectTeamMemberEducation.setUserId(userId);
                                projectTeamMemberEducation.setMemberName(pstme.getMemberName());
                                projectTeamMemberEducation.setProjectSendLogsId(-1);
                                projectTeamMemberEducation.setCreateTime(new Date());
                                projectTeamMemberEducation.setMemberDuties(pstme.getMemberDuties());
                                projectTeamMemberEducation.setMemberDesc(pstme.getMemberDesc());
                                projectTeamMemberEducation.setShareRatio(pstme.getShareRatio());
                                projectSendTeamMemberMapper.insertSelective(projectTeamMemberEducation);

                                ProjectSendTeamMemberEducation projectSendTeamMemberEducationForSearch1 = new ProjectSendTeamMemberEducation ();
                                projectSendTeamMemberEducationForSearch1.setProjectSendTeamMemberId(pstme.getId());


                                ProjectSendTeamMemberWork projectSendTeamMemberWorkForSearch1 = new ProjectSendTeamMemberWork();
                                projectSendTeamMemberWorkForSearch1.setProjectSendTeamMemberId(pstme.getId());

                                List<ProjectSendTeamMemberEducation> projectSendTeamMemberEducationList1 = projectSendTeamMemberEducationMapper.select(projectSendTeamMemberEducationForSearch1);
                                List<ProjectSendTeamMemberWork> projectSendTeamMemberWorkList1 = projectSendTeamMemberWorkMapper.select(projectSendTeamMemberWorkForSearch1);
                                //创建教育经历
                                if (projectSendTeamMemberEducationList1.size() > 0){
                                    for (ProjectSendTeamMemberEducation pe:projectSendTeamMemberEducationList1){
                                        ProjectSendTeamMemberEducation projectTeamMemberEducation1 =new ProjectSendTeamMemberEducation();
                                        projectTeamMemberEducation1.setProjectSendTeamMemberId(projectTeamMemberEducation.getId());
                                        projectTeamMemberEducation1.setEducationExperience(pe.getEducationExperience());

                                        projectSendTeamMemberEducationMapper.insertSelective(projectTeamMemberEducation1);
                                    }
                                }
                                //创建工作经历
                                if (projectSendTeamMemberWorkList1.size() > 0){
                                    for (ProjectSendTeamMemberWork pstmw:projectSendTeamMemberWorkList1){
                                        ProjectSendTeamMemberWork projectTeamMemberWork = new ProjectSendTeamMemberWork();
                                        projectTeamMemberWork.setProjectSendTeamMemberId(projectTeamMemberEducation.getId());
                                        projectTeamMemberWork.setWorkExperience(pstmw.getWorkExperience());

                                        projectSendTeamMemberWorkMapper.insert(projectTeamMemberWork);
                                    }
                                }

                            }
                        }
                        projectSendTeamMemberMapper.updateTeame(projectId,userId);
                        //更新历史融资历史
                        ProjectFinancingHistory projectFinancingHistory =new ProjectFinancingHistory();
                        projectFinancingHistory.setUserId(userId);
                        projectFinancingHistory.setProjectSendLogId("-1");
                        projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory);
                        if(projectFinancingHistory != null) {
                            projectFinancingHistoryMapper.updateHistory(projectId, userId);
                        }else{
                            ProjectFinancingHistory projectFinancingHistory2 =new ProjectFinancingHistory();
                            projectFinancingHistory2.setUserId(userId);
                            projectFinancingHistory2.setProjectSendLogId(params.getXmid());
                            projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory2);
                            if( projectFinancingHistory !=null) {
                                ProjectFinancingHistory projectFinancingHistory1 = new ProjectFinancingHistory();
                                projectFinancingHistory1.setProjectSendLogId(String.valueOf(projectId));
                                projectFinancingHistory1.setUserId(userId);
                                projectFinancingHistory1.setHistory(projectFinancingHistory.getHistory());
                                projectFinancingHistoryMapper.insert(projectFinancingHistory1);
                            }
                        }
                        //相似竞品
                        //相似竞品
                        ProjectSendLogCompeting projectSendLogCompeting = new ProjectSendLogCompeting();
                        List<ProjectSendLogCompeting> dataLogWorklist = new Page<ProjectSendLogCompeting>();
                        String competing = params.getCompeting();
                        projectSendLogCompeting.setProjectSendLogsId(projectId);
                        List<ProjectSendLogCompeting> one = projectSendLogCompetingMapper.select(projectSendLogCompeting);
                        if(competing ==null || "".equals(competing)){
                            projectSendLogCompetingMapper.delete(projectSendLogCompeting);
                        }else{
                            if (one.size()>0) {
                                ProjectSendLogCompeting dataLogEducation2 = new ProjectSendLogCompeting();
                                dataLogEducation2.setProjectSendLogsId(projectId);
                                projectSendLogCompetingMapper.delete(dataLogEducation2);
                                String[] fieldArry = competing.split(",");
                                for (int b = 0; b < fieldArry.length; b++) {
                                    ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                    projectSendLogCompeting1.setCompetingProductName(fieldArry[b]);
                                    projectSendLogCompeting1.setProjectSendLogsId(projectId);
                                    dataLogWorklist.add(projectSendLogCompeting1);
                                }
                                projectSendLogCompetingMapper.insertList(dataLogWorklist);
                            } else {
                                String[] fieldArry = competing.split(",");
                                for (int b = 0; b < fieldArry.length; b++) {
                                    ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                    projectSendLogCompeting1.setCompetingProductName(fieldArry[b]);
                                    projectSendLogCompeting1.setProjectSendLogsId(projectId);
                                    dataLogWorklist.add(projectSendLogCompeting1);
                                }
                                projectSendLogCompetingMapper.insertList(dataLogWorklist);
                            }}
                        //更新融资申请表
                        ProjectFinancingApproval oldProjectFinancingApproval = new ProjectFinancingApproval();
                        oldProjectFinancingApproval.setProjectSendLogId(projectId);
                        oldProjectFinancingApproval.setUserId(userId);
                        oldProjectFinancingApproval = projectFinancingApprovalMapper.selectOne(oldProjectFinancingApproval);
                        if (oldProjectFinancingApproval != null) {
                            if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                oldProjectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                            }
                            oldProjectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                            oldProjectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                            oldProjectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                            if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                            }
//                            oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                            projectFinancingApprovalMapper.updateByPrimaryKey(oldProjectFinancingApproval);
                        } else {
                            ProjectFinancingApproval projectFinancingApproval = new ProjectFinancingApproval();
                            projectFinancingApproval.setProjectSendLogId(projectId);
                            projectFinancingApproval.setUserId(userId);
                            if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                projectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                            }
                            projectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                            projectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                            projectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                            if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                projectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                            }
//                            projectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                            projectFinancingApprovalMapper.insert(projectFinancingApproval);
                            //更新投递项目-机构关系表
                            //插入
                            String[] investmentIds = params.getTsid().split(",");
                            for (String investmentId : investmentIds) {
                                ProjectFinancingInvestmentInstitutionRelationship relationship = new ProjectFinancingInvestmentInstitutionRelationship();
                                relationship.setProjectSendLogId(projectId);
                                relationship.setInvestmentInstitutionId(Integer.parseInt(investmentId));
                                relationship.setAssociatedTime(new Date());
                                projectFinancingInvestmentInstitutionRelationshipMapper.insert(relationship);
                            }
                        }

                        //更新创始人记录
                        int foundersId = 0;
                        Founders founders = new Founders();
                        founders.setUserId(userId);
                        founders = foundersMapper.selectOne(founders);
                        if (founders != null) {
                            foundersId = founders.getId();
                        } else {
                            //插入
                            Founders newFounders = new Founders();
                            newFounders.setUserId(userId);
                            foundersMapper.insert(newFounders);
                            foundersId = newFounders.getId();
                        }

                        //更新教育经历
                        if (params.getTuisongxiangmubiao7educationa() != null && !"".equals(params.getTuisongxiangmubiao7educationa())) {
                            String[] educations = params.getTuisongxiangmubiao7educationa().split(",");
                            //删除之前的记录
                            FoundersEducation old = new FoundersEducation();
                            old.setFounderId(foundersId);
                            foundersEducationMapper.delete(old);
                            for (String education : educations) {
                                FoundersEducation foundersEducation = new FoundersEducation();
                                foundersEducation.setFounderId(foundersId);
                                foundersEducation.setEducationExperience(education);
                                foundersEducationMapper.insert(foundersEducation);
                            }
                        }else{
                            FoundersEducation foundersEducation = new FoundersEducation();
                            foundersEducation.setFounderId(foundersId);
                            foundersEducationMapper.delete( foundersEducation);
                        }

                        //更新工作经历
                        if (params.getTuisongxiangmubiao7workexperi() != null && !"".equals(params.getTuisongxiangmubiao7workexperi())) {
                            String[] works = params.getTuisongxiangmubiao7workexperi().split(",");
                            //删除之前的记录
                            FoundersWork old = new FoundersWork();
                            old.setFounderId(foundersId);
                            foundersWorkMapper.delete(old);
                            for (String work : works) {
                                FoundersWork foundersWork = new FoundersWork();
                                foundersWork.setFounderId(foundersId);
                                foundersWork.setWorkExperience(work);
                                foundersWorkMapper.insert(foundersWork);
                            }
                        }else{
                            FoundersWork foundersWork = new FoundersWork();
                            foundersWork.setFounderId(foundersId);
                            foundersWorkMapper.delete(foundersWork);
                        }
                    } else {
                        result.setStatus(5002);
                        result.setMessage("不能提交重复的项目");
                    }
                }
                } else {
                    ProjectSendLogs p2 = new ProjectSendLogs();
                    p2.setCompanyShortName(params.getCompanyshortname());
                    p2 = projectSendLogsMapper.selectOne(p2);
                    if (p2 == null) {
                        Users users = new Users();
                        users.setId(userId);
                        users = usersMapper.selectByPrimaryKey(users);
                        users.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                        users.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                        users.setCompanyDuties(params.getTuisongxiangmubiao7assumeoffi());
                        users.setActualName(params.getTuisongxiangmubiao7realname());
                        users.setCity(params.getTuisongxiangmubiao7city());
                        users.setDesc(params.getTuisongxiangmubiao7profile());
                        if (params.getTuisongxiangmubiao7mailbox() != null && !"".equals(params.getTuisongxiangmubiao7mailbox())) {
                            users.setEmail(params.getTuisongxiangmubiao7mailbox());
                        }
                        if (params.getTuisongxiangmubiao7wechatnumb() != null && !"".equals(params.getTuisongxiangmubiao7wechatnumb())) {
                            users.setWechat(params.getTuisongxiangmubiao7wechatnumb());
                        }
                        usersMapper.updateByPrimaryKey(users);
                        //更新项目投递记录
                        ProjectSendLogs projectSendLogs = new ProjectSendLogs();
                        projectSendLogs.setUserid(userId);
                        projectSendLogs.setCity(params.getTuisongxiangmubiao7city());
                        projectSendLogs.setCompanyName(params.getTuisongxiangmubiao7companywhe());
                        projectSendLogs.setCompanyDesc(params.getTuisongxiangmubiao7companypro());
                        projectSendLogs.setField(params.getTuisongxiangmubiao7industryfi());
                        if (params.getTuisongxiangmubiao7projecttag() != null && !"".equals(params.getTuisongxiangmubiao7projecttag())) {
                            projectSendLogs.setProjectTags(params.getTuisongxiangmubiao7projecttag());
                        }else{
                            projectSendLogs.setProjectTags("");
                        }
                        projectSendLogs.setCreatTime(new Date());
                        //logo图
                        projectSendLogs.setCompanyLogo(params.getLogo());
                        if (params.getCompanyshortname() != null || "".equals(params.getCompanyshortname())) {
                            projectSendLogs.setCompanyShortName(params.getCompanyshortname());
                        } else {
                            result.setStatus(50001);
                            result.setMessage("公司简称不能为空");
                        }

                        if (params.getOneintroduct() != null || "".equals(params.getOneintroduct())) {
                            projectSendLogs.setCompanyOneSentenceIntroduct(params.getOneintroduct());
                        } else {
                            result.setStatus(50001);
                            result.setMessage("一句话介绍不能为空");
                        }

                        if (params.getHighlights() != null || "".equals(params.getHighlights())) {
                            projectSendLogs.setCompanyInvestmentHighlights(params.getHighlights());
                        } else {
                            result.setStatus(50001);
                            result.setMessage("投资亮点不能为空");
                        }
                        projectSendLogs.setCompanyOfficialWebsite(params.getWebsite());


                        projectSendLogsMapper.insert(projectSendLogs);
                        int projectId = projectSendLogs.getId();

                        projectSendTeamMemberMapper.updateTeame(projectId,userId);
                        //更新的融资历史
                        ProjectFinancingHistory projectFinancingHistory =new ProjectFinancingHistory();
                        projectFinancingHistory.setUserId(userId);
                        projectFinancingHistory.setProjectSendLogId("-1");
                        projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory);
                        if(projectFinancingHistory != null) {
                            projectFinancingHistoryMapper.updateHistory(projectId, userId);
                        }else{
                            ProjectFinancingHistory projectFinancingHistory2 =new ProjectFinancingHistory();
                            projectFinancingHistory2.setUserId(userId);
                            projectFinancingHistory2.setProjectSendLogId(params.getXmid());
                            projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory2);
                            if( projectFinancingHistory !=null) {
                                ProjectFinancingHistory projectFinancingHistory1 = new ProjectFinancingHistory();
                                projectFinancingHistory1.setProjectSendLogId(String.valueOf(projectId));
                                projectFinancingHistory1.setUserId(userId);
                                projectFinancingHistory1.setHistory(projectFinancingHistory.getHistory());
                                projectFinancingHistoryMapper.insert(projectFinancingHistory1);
                            }
                        }
                        //相似竞品
                        ProjectSendLogCompeting projectSendLogCompeting = new ProjectSendLogCompeting();
                        List<ProjectSendLogCompeting> dataLogWorklist = new Page<ProjectSendLogCompeting>();
                        String competing = params.getCompeting();
                        projectSendLogCompeting.setProjectSendLogsId(projectId);
                        List<ProjectSendLogCompeting> one = projectSendLogCompetingMapper.select(projectSendLogCompeting);
                        if(competing ==null || "".equals(competing)){
                            projectSendLogCompetingMapper.delete(projectSendLogCompeting);
                        }else{
                            if (one.size()>0) {
                                ProjectSendLogCompeting dataLogEducation2 = new ProjectSendLogCompeting();
                                dataLogEducation2.setProjectSendLogsId(projectId);
                                projectSendLogCompetingMapper.delete(dataLogEducation2);
                                String[] fieldArry = competing.split(",");
                                for (int b = 0; b < fieldArry.length; b++) {
                                    ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                    projectSendLogCompeting1.setCompetingProductName(fieldArry[b]);
                                    projectSendLogCompeting1.setProjectSendLogsId(projectId);
                                    dataLogWorklist.add(projectSendLogCompeting1);
                                }
                                projectSendLogCompetingMapper.insertList(dataLogWorklist);
                            } else {
                                String[] fieldArry = competing.split(",");
                                for (int b = 0; b < fieldArry.length; b++) {
                                    ProjectSendLogCompeting projectSendLogCompeting1 = new ProjectSendLogCompeting();
                                    projectSendLogCompeting1.setCompetingProductName(fieldArry[b]);
                                    projectSendLogCompeting1.setProjectSendLogsId(projectId);
                                    dataLogWorklist.add(projectSendLogCompeting1);
                                }
                                projectSendLogCompetingMapper.insertList(dataLogWorklist);
                            }}
                        //更新融资申请表
                        ProjectFinancingApproval oldProjectFinancingApproval = new ProjectFinancingApproval();
                        oldProjectFinancingApproval.setProjectSendLogId(projectId);
                        oldProjectFinancingApproval.setUserId(userId);
                        oldProjectFinancingApproval = projectFinancingApprovalMapper.selectOne(oldProjectFinancingApproval);
                        if (oldProjectFinancingApproval != null) {
                            if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                oldProjectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                            }
                            oldProjectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                            oldProjectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                            oldProjectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                            if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                            }
//                            oldProjectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                            projectFinancingApprovalMapper.updateByPrimaryKey(oldProjectFinancingApproval);
                        } else {
                            ProjectFinancingApproval projectFinancingApproval = new ProjectFinancingApproval();
                            projectFinancingApproval.setProjectSendLogId(projectId);
                            projectFinancingApproval.setUserId(userId);
                            if (params.getTuisongxiangmubiao7currentdem() != null && !"".equals(params.getTuisongxiangmubiao7currentdem())) {
                                projectFinancingApproval.setCurrentDemand(params.getTuisongxiangmubiao7currentdem());
                            }
                            projectFinancingApproval.setFinancingRounds(params.getTuisongxiangmubiao7roundoffin());
                            projectFinancingApproval.setFinancingAmount(new BigDecimal(params.getTuisongxiangmubiao7financinga()));
                            projectFinancingApproval.setFinancingCurrency(Integer.parseInt(params.getTuisongxiangmubiao7financingu()));
                            if(null != params.getTuisongxiangmubiao7sellingsha() && StringUtils.isNotEmpty(params.getTuisongxiangmubiao7sellingsha())){
                                projectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                            }
//                            projectFinancingApproval.setTransferShares(new BigDecimal(params.getTuisongxiangmubiao7sellingsha()));
                            projectFinancingApprovalMapper.insert(projectFinancingApproval);
                            //更新投递项目-机构关系表
                            //插入
                            String[] investmentIds = params.getTsid().split(",");
                            for (String investmentId : investmentIds) {
                                ProjectFinancingInvestmentInstitutionRelationship relationship = new ProjectFinancingInvestmentInstitutionRelationship();
                                relationship.setProjectSendLogId(projectId);
                                relationship.setInvestmentInstitutionId(Integer.parseInt(investmentId));
                                relationship.setAssociatedTime(new Date());
                                projectFinancingInvestmentInstitutionRelationshipMapper.insert(relationship);
                            }
                        }

                        //更新创始人记录
                        int foundersId = 0;
                        Founders founders = new Founders();
                        founders.setUserId(userId);
                        founders = foundersMapper.selectOne(founders);
                        if (founders != null) {
                            foundersId = founders.getId();
                        } else {
                            //插入
                            Founders newFounders = new Founders();
                            newFounders.setUserId(userId);
                            foundersMapper.insert(newFounders);
                            foundersId = newFounders.getId();
                        }

                        //更新教育经历
                        if (params.getTuisongxiangmubiao7educationa() != null && !"".equals(params.getTuisongxiangmubiao7educationa())) {
                            String[] educations = params.getTuisongxiangmubiao7educationa().split(",");
                            //删除之前的记录
                            FoundersEducation old = new FoundersEducation();
                            old.setFounderId(foundersId);
                            foundersEducationMapper.delete(old);
                            for (String education : educations) {
                                FoundersEducation foundersEducation = new FoundersEducation();
                                foundersEducation.setFounderId(foundersId);
                                foundersEducation.setEducationExperience(education);
                                foundersEducationMapper.insert(foundersEducation);
                            }
                        }else{
                            FoundersEducation foundersEducation = new FoundersEducation();
                            foundersEducation.setFounderId(foundersId);
                            foundersEducationMapper.delete( foundersEducation);
                        }

                        //更新工作经历
                        if (params.getTuisongxiangmubiao7workexperi() != null && !"".equals(params.getTuisongxiangmubiao7workexperi())) {
                            String[] works = params.getTuisongxiangmubiao7workexperi().split(",");
                            //删除之前的记录
                            FoundersWork old = new FoundersWork();
                            old.setFounderId(foundersId);
                            foundersWorkMapper.delete(old);
                            for (String work : works) {
                                FoundersWork foundersWork = new FoundersWork();
                                foundersWork.setFounderId(foundersId);
                                foundersWork.setWorkExperience(work);
                                foundersWorkMapper.insert(foundersWork);
                            }
                        }else{
                            FoundersWork foundersWork = new FoundersWork();
                            foundersWork.setFounderId(foundersId);
                            foundersWorkMapper.delete(foundersWork);
                        }
                    } else {
                        result.setStatus(5002);
                        result.setMessage("不能提交重复的项目");
                    }
                }

            return result;

    }
    

    /**
     * 项目投递回显
     * @param userId 用户ID
     * @param tsid 投递项目ID
     * @return
     */
    @Override
    public CommonDto<Map<String, Object>> rtuisonghuixian(int userId, String tsid) {
        CommonDto<Map<String, Object>> result = new CommonDto<>();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> datas = new HashMap<String, Object>();

        CommonDto<Map<String, List<LabelList>>> hotsdatas = evaluateService.queryHotData();

        Example example = new Example(ProjectSendLogs.class);
        example.and().andEqualTo("userid", userId).andIsNotNull("companyShortName");;
        example.setOrderByClause("creat_time desc");
        List<ProjectSendLogs> sendLogsList = projectSendLogsMapper.selectByExample(example);
        ProjectSendLogs sendLogs = new ProjectSendLogs();
        int projectId = 0;
        if(sendLogsList.size() > 0) {
            sendLogs = sendLogsList.get(0);
            projectId = sendLogs.getId();
            datas.put("tsid",projectId);
        }else{
            datas.put("tsid",null);
        }


        Users users = new Users();
        users.setId(userId);
        users = usersMapper.selectByPrimaryKey(users);

        ProjectFinancingApproval approval = new ProjectFinancingApproval();
        approval.setUserId(userId);
        approval.setProjectSendLogId(projectId);
        approval = projectFinancingApprovalMapper.selectOne(approval);

       //相似竞品回显
        ProjectSendLogCompeting  projectSendLogCompeting =new ProjectSendLogCompeting();
        projectSendLogCompeting.setProjectSendLogsId(projectId);
        List<ProjectSendLogCompeting> work1 = projectSendLogCompetingMapper.select(projectSendLogCompeting);
        String [] workArray1 ={};
        if(work1.size()>0){
            for(ProjectSendLogCompeting pp :work1){
                if(pp.getCompetingProductName() == null || "".equals(pp.getCompetingProductName())){
                    datas.put("competing", workArray1);
                }else{
                    String [] workArray =null;
                    List<String> a=new LinkedList<String>();
                    for(ProjectSendLogCompeting d:work1){
                        a.add(d.getCompetingProductName());
                        workArray= new String[a.size()];
                        workArray=a.toArray(workArray);
                        datas.put("competing",workArray);
                    }
                }
            }

        }else{
            datas.put("competing", workArray1);
        }
        //团队成员
       /* ProjectSendTeamMember projectSendTeamMember =new ProjectSendTeamMember();
        projectSendTeamMember.setProjectSendLogsId(projectId);
        projectSendTeamMember.setYn(1);
        List<ProjectSendTeamMember> list1 = projectSendTeamMemberMapper.select(projectSendTeamMember);*/
        //List<ProjectSendTeamMember> list1 =projectSendTeamMemberMapper.findTeam(projectId,userId);
       // datas.put("teamMember",list1);


        Founders founders = new Founders();
        founders.setUserId(userId);
        founders = foundersMapper.selectOne(founders);
        List<FoundersEducation> educations = new ArrayList<>();
        List<FoundersWork> works = new ArrayList<>();
        if(founders != null){
            int foundersId = founders.getId();
            FoundersEducation foundersEducation = new FoundersEducation();
            foundersEducation.setFounderId(foundersId);
            educations = foundersEducationMapper.select(foundersEducation);

            FoundersWork foundersWork = new FoundersWork();
            foundersWork.setFounderId(foundersId);
            works = foundersWorkMapper.select(foundersWork);
        }

        //所在城市(users)
        List<LabelList> cities = hotsdatas.getData().get("cityKey");
        if(users != null && users.getCity() != null && !"".equals(users.getCity())){
            String cityStr = users.getCity();
            for(LabelList labellist : cities){
                if(cityStr.equals(labellist.getName())){
                    labellist.setChecked(true);
                }
            }
            datas.put("tuisongxiangmubiao7city", cityStr);
        }else{
            datas.put("tuisongxiangmubiao7city", "");
        }
        datas.put("chengshiming", cities);

        //行业领域(send_logs)
        List<LabelList> industrys = hotsdatas.getData().get("industryKey");
        String industryStr = sendLogs.getField();
        if(industryStr != null && !"".equals(industryStr)){
            String[] industryArray = industryStr.split(",");
            for(LabelList labelList : industrys){
                for(String string : industryArray){
                    if(string.equals(labelList.getName())){
                        labelList.setChecked(true);
                    }
                }
            }
        }
        datas.put("tuisongxiangmubiao7industryfi", industrys);
        //
        datas.put("logo",sendLogs.getCompanyLogo());
        datas.put("shortName",sendLogs.getCompanyShortName());
        datas.put("oenintroduce",sendLogs.getCompanyOneSentenceIntroduct());
        datas.put("highlights",sendLogs.getCompanyInvestmentHighlights());
        datas.put("website", sendLogs.getCompanyOfficialWebsite());

        //融资轮次(approve)
        String roundNameStr = "";
        if(approval != null && !"".equals(approval.getFinancingRounds()) && approval.getFinancingRounds() != null){
            roundNameStr = approval.getFinancingRounds();
        }
        datas.put("tuisongxiangmubiao7roundoffin", roundNameStr);

        //身份类型(users)
       /* List<LabelList> identities = new ArrayList<>();
        LabelList identify1 = new LabelList();
        identify1.setName("投资人");
        identify1.setValue("投资人");
        identify1.setChecked(false);
        LabelList identify2 = new LabelList();
        identify2.setName("创业者");
        identify2.setValue("创业者");
        identify2.setChecked(false);
        LabelList identify3 = new LabelList();
        identify3.setName("产业公司");
        identify3.setValue("产业公司");
        identify3.setChecked(false);
        LabelList identify4 = new LabelList();
        identify4.setName("媒体");
        identify4.setValue("媒体");
        identify4.setChecked(false);
        LabelList identify5 = new LabelList();
        identify5.setName("服务机构");
        identify5.setValue("服务机构");
        identify5.setChecked(false);
        LabelList identify6 = new LabelList();
        identify6.setName("政府机构");
        identify6.setValue("政府机构");
        identify6.setChecked(false);
        identities.add(identify1);
        identities.add(identify2);
        identities.add(identify3);
        identities.add(identify4);
        identities.add(identify5);
        identities.add(identify6);
        if(users != null && users.getIdentityType() != null){
            int identity = users.getIdentityType();
            String identityStr = "";
            switch(identity){
                case 0:
                    identityStr = "投资人";
                    break;
                case 1:
                    identityStr = "创业者";
                    break;
                case 2:
                    identityStr = "产业公司";
                    break;
                case 3:
                    identityStr = "媒体";
                    break;
                case 4:
                    identityStr = "服务机构";
                    break;
                case 5:
                    identityStr = "政府机构";
                    break;
            }
            for(LabelList list : identities){
                if(identityStr.equals(list.getName())){
                    list.setChecked(true);
                }
            }
            datas.put("tuisongxiangmubiao7identityty", identityStr);
        }else{
            datas.put("tuisongxiangmubiao7identityty", "");
        }
        datas.put("bigBoss", identities);
*/
        if(users != null){
            //公司名称
            datas.put("tuisongxiangmubiao7companywhe", users.getCompanyName());
            //公司简介
            datas.put("tuisongxiangmubiao7companypro", users.getCompanyDesc());
            //真是姓名
            datas.put("tuisongxiangmubiao7realname", users.getActualName());
            //担任职务
            datas.put("tuisongxiangmubiao7assumeoffi", users.getCompanyDuties());
            //个人简介
            datas.put("tuisongxiangmubiao7profile", users.getDesc());
            //邮箱
            datas.put("tuisongxiangmubiao7mailbox", users.getEmail());
            //微信号
            datas.put("tuisongxiangmubiao7wechatnumb", users.getWechat());
        }else{
            //公司名称
            datas.put("tuisongxiangmubiao7companywhe", "");
            //公司简介
            datas.put("tuisongxiangmubiao7companypro", "");
            //真是姓名
            datas.put("tuisongxiangmubiao7realname", "");
            //担任职务
            datas.put("tuisongxiangmubiao7assumeoffi", "");
            //个人简介
            datas.put("tuisongxiangmubiao7profile", "");
            //邮箱
            datas.put("tuisongxiangmubiao7mailbox", "");
            //微信号
            datas.put("tuisongxiangmubiao7wechatnumb", "");
        }

        if(approval != null){
            //融资金额
            datas.put("tuisongxiangmubiao7financinga", approval.getFinancingAmount());
            //融资金额单位
            datas.put("tuisongxiangmubiao7financingu", approval.getFinancingCurrency());
            //出让股份
            datas.put("tuisongxiangmubiao7sellingsha", approval.getTransferShares());
            //当前需求
            datas.put("tuisongxiangmubiao7currentdem", approval.getCurrentDemand());
        }else{
            //融资金额
            datas.put("tuisongxiangmubiao7financinga", "");
            //融资金额单位
            datas.put("tuisongxiangmubiao7financingu", "");
            //出让股份
            datas.put("tuisongxiangmubiao7sellingsha", "");
            //当前需求
            datas.put("tuisongxiangmubiao7currentdem", "");
        }

        //项目标签
        datas.put("tuisongxiangmubiao7projecttag", sendLogs.getProjectTags());

        //教育经历
        List<String> educationStr = new ArrayList<>();
        if(educations.size() > 0){
            for(FoundersEducation education : educations){
                educationStr.add(education.getEducationExperience());
            }
        }
        datas.put("tuisongxiangmubiao7educationa", educationStr);

        //工作经历
        List<String> workStr = new ArrayList<>();
        if(works.size() > 0){
            for(FoundersWork work : works){
                workStr.add(work.getWorkExperience());
            }
        }
        datas.put("tuisongxiangmubiao7workexperi", workStr);

        //获取最新项目ID
        /*ProjectSendLogs projectSendLogs = new ProjectSendLogs();
        projectSendLogs.setUserid(userId);
        projectSendLogs.setCreatTime(new Date());
        projectSendLogsMapper.insert(projectSendLogs);

        int newId = projectSendLogs.getId();*/

        data.put("jieguo", datas);
        data.put("success", true);
        result.setStatus(200);
        result.setMessage("回显数据获取成功");
        result.setData(data);
        return result;
    }

    /**
     * 融资历史记录
     * @param tsid 项目ID
     * @param rongzilishi 融资历史信息
     * @return
     */
    @Override
    public CommonDto<String> ctuisongthird(String tsid, String rongzilishi,int userId) {
        CommonDto<String> result = new CommonDto<>();
        //保存融资历史记录
        tsid="-1";
        ProjectFinancingHistory projectFinancingHistory = new ProjectFinancingHistory();
        projectFinancingHistory.setProjectSendLogId(tsid);
        projectFinancingHistory.setUserId(userId);
        projectFinancingHistory = projectFinancingHistoryMapper.selectOne(projectFinancingHistory);
        if(projectFinancingHistory != null){
            projectFinancingHistory.setHistory(rongzilishi);
            projectFinancingHistory.setUserId(userId);
            projectFinancingHistoryMapper.updateByPrimaryKey(projectFinancingHistory);
        }else{
            ProjectFinancingHistory newProjectFinancingHistory = new ProjectFinancingHistory();
            newProjectFinancingHistory.setProjectSendLogId(tsid);
            newProjectFinancingHistory.setUserId(userId);
            newProjectFinancingHistory.setHistory(rongzilishi);
            projectFinancingHistoryMapper.insert(newProjectFinancingHistory);
        }

        result.setStatus(200);
        result.setMessage("融资历史保存成功");
        return result;
    }

    /**
     * 融资历史回显
     * @param tsid 投递项目ID
     * @return
     */
    @Override
    public CommonDto<ProjectFinancingHistory> rtuisongthird(String tsid, int userId) {
        CommonDto<ProjectFinancingHistory>  result = new CommonDto<>();
        Map<String, Object> data = new HashMap<>();
        String history = "";
        //查出当前用户投递信息
        /*Example example = new Example(ProjectSendLogs.class);
        example.and().andEqualTo("userid", userId);
        example.setOrderByClause("creat_time desc");
        List<ProjectSendLogs> sendLogsList = projectSendLogsMapper.selectByExample(example);
        if(sendLogsList.size() > 0){
            for(ProjectSendLogs sendLogs : sendLogsList){
                ProjectFinancingHistory projectFinancingHistory = new ProjectFinancingHistory();
                projectFinancingHistory.setProjectSendLogId(sendLogs.getId() + "");
                projectFinancingHistory = projectFinancingHistoryMapper.selectOne(projectFinancingHistory);
                if(projectFinancingHistory != null){
                    history = projectFinancingHistory.getHistory();
                    break;
                }
            }
        }*/
        ProjectFinancingHistory projectFinancingHistory =new ProjectFinancingHistory();
        projectFinancingHistory.setUserId(userId);
        projectFinancingHistory.setProjectSendLogId("-1");
        projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory);
        if(projectFinancingHistory == null){
            ProjectFinancingHistory projectFinancingHistory1 =new ProjectFinancingHistory();
            projectFinancingHistory1.setUserId(userId);
            projectFinancingHistory1.setProjectSendLogId(tsid);
            projectFinancingHistory=projectFinancingHistoryMapper.selectOne(projectFinancingHistory1);
        }
        //List<ProjectFinancingHistory>list1 =projectFinancingHistoryMapper.searchTeam1(projectId,userId);
        result.setData(projectFinancingHistory);
        result.setStatus(200);
        result.setMessage("融资历史回显数据获取成功");
        return result;
    }

    /**
     * 保存团队成员
     * @param body
     * @return
     */
    
    @Override
    public CommonDto<String> saveTeam(TeamDto body) {
        CommonDto<String> result = new CommonDto<>();
        String token = body.getToken();
        int userId = commonUserService.getLocalUserId(token);
        String education = body.getEducation();
        String jianjie = body.getJianjie();
        String shortname = body.getShortname();
        String stock_right = body.getStock_right();
        String tsid = body.getTsid();
        String work = body.getWork();
        String zhiwu = body.getZhiwu();
        if(education ==null || "".equals(education)){
        	 result.setStatus(50021);
             result.setMessage("教育背景不能为空");
        	
        }
        
        if(jianjie ==null || "".equals(jianjie)){
       	 result.setStatus(50021);
            result.setMessage("简介不能为空");
       	
       }
        if(shortname ==null || "".equals(shortname)){
       	 result.setStatus(50021);
            result.setMessage("姓名不能为空");
       	
       }
        if(stock_right ==null || "".equals(stock_right)){
          	 result.setStatus(50021);
               result.setMessage("轮次不能为空");
          	
          }
        if(tsid ==null || "".equals(tsid)){
          	 result.setStatus(50021);
               result.setMessage("记录不能为空");
          	
          }
        if(work ==null || "".equals(work)){
          	 result.setStatus(50021);
               result.setMessage("工作不能为空");
          	
          }
        if(zhiwu ==null || "".equals(zhiwu)){
          	 result.setStatus(50021);
               result.setMessage("职务不能为空");
          	
          }
         ProjectSendTeamMember projectSendTeamMember =new ProjectSendTeamMember();
         projectSendTeamMember.setCreateTime(new Date());
         projectSendTeamMember.setMemberDesc(jianjie);
         projectSendTeamMember.setMemberDuties(zhiwu);
         projectSendTeamMember.setProjectSendLogsId(-1);
         BigDecimal stockright = new BigDecimal(stock_right);
         projectSendTeamMember.setShareRatio(stockright);
         projectSendTeamMember.setMemberName(shortname);
         projectSendTeamMember.setYn(0);
         projectSendTeamMember.setUserId(userId);
         projectSendTeamMemberMapper.insert(projectSendTeamMember);
         //教育背景
         String[] educationArry =education.split(",");
         List<ProjectSendTeamMemberEducation> dataLogEducationlist =new Page< ProjectSendTeamMemberEducation>();
         for (int a=0; a < educationArry.length;a++){
        	  ProjectSendTeamMemberEducation dataLogEducation =new ProjectSendTeamMemberEducation();
             dataLogEducation.setEducationExperience(educationArry[a]);
             dataLogEducation.setProjectSendTeamMemberId(projectSendTeamMember.getId());
             dataLogEducationlist.add(dataLogEducation);
         }
         projectSendTeamMemberEducationMapper.insertList(dataLogEducationlist);
         //工作背景
        String[] workArry =work.split(",");
        List<ProjectSendTeamMemberWork> dataLogworklist =new Page<ProjectSendTeamMemberWork>();
        for (int a=0; a < workArry.length;a++){
            ProjectSendTeamMemberWork dataLogEducation =new ProjectSendTeamMemberWork();
            dataLogEducation.setWorkExperience(workArry[a]);
            dataLogEducation.setProjectSendTeamMemberId(projectSendTeamMember.getId());
            dataLogworklist.add(dataLogEducation);
        }
        projectSendTeamMemberWorkMapper.insertList(dataLogworklist);
         result.setStatus(200);
         result.setMessage("保存成功");
         return result;
    }


    /**
     * 删除团队成员
     * @param id
     * @return
     */
	@Override
	public CommonDto<String> deleteTeam(Integer id) {
		CommonDto<String> result = new CommonDto<>();
		ProjectSendTeamMember projectSendTeamMember =new ProjectSendTeamMember();
		projectSendTeamMember.setId(id);
		ProjectSendTeamMember body = projectSendTeamMemberMapper.selectByPrimaryKey(projectSendTeamMember);
		if(body.getYn() == 0){
			body.setYn(1);
		}else{
			result.setStatus(501);
	        result.setMessage("已删除，请不要频繁点击");
		}
		projectSendTeamMemberMapper.updateByPrimaryKey((body));
		result.setStatus(200);
        result.setMessage("保存成功");
		return result;
	}

    /**
     * 团队成员回显
     */
	@Override
	public CommonDto<Map<String, Object>> teamRecord(Integer id) {
		CommonDto<Map<String, Object>> result =new CommonDto<Map<String, Object>>();
		 Map<String,Object> map =new HashMap<>();
		 if(id !=null){
			 ProjectSendTeamMember projectSendTeamMember =new ProjectSendTeamMember();
			 projectSendTeamMember.setId(id);
			 projectSendTeamMember = projectSendTeamMemberMapper.selectByPrimaryKey(projectSendTeamMember);
			 map.put("memberName",projectSendTeamMember.getMemberName());
			 map.put("zhiwu",projectSendTeamMember.getMemberDuties());
			 map.put("jianjie",projectSendTeamMember.getMemberDesc());
			 map.put("rocket",projectSendTeamMember.getShareRatio());
			 
			ProjectSendTeamMemberWork dataLogWork =new ProjectSendTeamMemberWork();
	        dataLogWork.setProjectSendTeamMemberId(id);
	        List<ProjectSendTeamMemberWork> work = projectSendTeamMemberWorkMapper.select(dataLogWork);
	        if(work !=null){
	        	List<String> a=new LinkedList<String>();
	            String [] workArray =null;
	            for(ProjectSendTeamMemberWork d:work){
	                a.add(d.getWorkExperience());
	                workArray= new String[a.size()];
	                workArray=a.toArray(workArray);
	        	    map.put("work",workArray);
	            }
	        }else{
	        	result.setStatus(51002);
	            result.setMessage("工作必填的");
	        }
	        
	        //教育背景
	        ProjectSendTeamMemberEducation dataLogeducation =new ProjectSendTeamMemberEducation();
	        dataLogeducation.setProjectSendTeamMemberId(id);
	        List<ProjectSendTeamMemberEducation> logeducation = projectSendTeamMemberEducationMapper.select(dataLogeducation);
	        if(logeducation !=null){
	        	List<String> a=new LinkedList<String>();
	            String [] workArray =null;
	            for(ProjectSendTeamMemberEducation d: logeducation){
	                a.add(d.getEducationExperience());
	                workArray= new String[a.size()];
	                workArray=a.toArray(workArray);
	        	    map.put("education",workArray);
	            }
	        }else{
	        	result.setStatus(51002);
	            result.setMessage("工作必填的");
	        }
	        
	        }else{
	        	result.setStatus(501);
	            result.setMessage("此团队成员不存在");	
	        }
	        result.setData(map);
		return result;
	}

    /**
     * 修改页面
     * @param body
     * @return
     */
	@Override
	public CommonDto<String> saveTeam1(TeamDto1 body) {
		 CommonDto<String> result = new CommonDto<>();
	        String education = body.getEducation();
	        String jianjie = body.getJianjie();
	        String shortname = body.getShortname();
	        String stock_right = body.getStock_right();
	        String tsid = body.getTsid();
	        String work = body.getWork();
	        String zhiwu = body.getZhiwu();
	        String token = body.getToken();
	        int userId = commonUserService.getLocalUserId(token);
	        if(education ==null || "".equals(education)){
	        	 result.setStatus(50021);
	             result.setMessage("教育背景不能为空");
	        	
	        }
	        
	        if(jianjie ==null || "".equals(jianjie)){
	       	 result.setStatus(50021);
	            result.setMessage("简介不能为空");
	       	
	       }
	        if(shortname ==null || "".equals(shortname)){
	       	 result.setStatus(50021);
	            result.setMessage("姓名不能为空");
	       	
	       }
	        if(stock_right ==null || "".equals(stock_right)){
	          	 result.setStatus(50021);
	               result.setMessage("轮次不能为空");
	          	
	          }
	        if(tsid ==null || "".equals(tsid)){
	          	 result.setStatus(50021);
	               result.setMessage("记录不能为空");
	          	
	          }
	        if(work ==null || "".equals(work)){
	          	 result.setStatus(50021);
	               result.setMessage("工作不能为空");
	          	
	          }
	        if(zhiwu ==null || "".equals(zhiwu)){
	          	 result.setStatus(50021);
	               result.setMessage("职务不能为空");
	          	
	          }
	         ProjectSendTeamMember projectSendTeamMember =new ProjectSendTeamMember();
	         projectSendTeamMember.setId(body.getId());
	         projectSendTeamMember.setCreateTime(new Date());
	         projectSendTeamMember.setMemberDesc(jianjie);
	         projectSendTeamMember.setMemberDuties(zhiwu);
             projectSendTeamMember.setProjectSendLogsId(-1);
	         BigDecimal stockright = new BigDecimal(stock_right);
	         projectSendTeamMember.setShareRatio(stockright);
	         projectSendTeamMember.setMemberName(shortname);
	         projectSendTeamMember.setYn(0);
	         projectSendTeamMember.setUserId(userId);
	         projectSendTeamMemberMapper.updateByPrimaryKey(projectSendTeamMember);
	         //教育背景
	         ProjectSendTeamMemberEducation dataLogEducation1 =new ProjectSendTeamMemberEducation();
	         dataLogEducation1.setProjectSendTeamMemberId(body.getId());
	         projectSendTeamMemberEducationMapper.delete(dataLogEducation1);
	         String[] educationArry =education.split(",");
	         List<ProjectSendTeamMemberEducation> dataLogEducationlist =new Page< ProjectSendTeamMemberEducation>();
	         for (int a=0; a < educationArry.length;a++){
	        	  ProjectSendTeamMemberEducation dataLogEducation =new ProjectSendTeamMemberEducation();
	             dataLogEducation.setEducationExperience(educationArry[a]);
	             dataLogEducation.setProjectSendTeamMemberId(projectSendTeamMember.getId());
	             dataLogEducationlist.add(dataLogEducation);
	         }
	         projectSendTeamMemberEducationMapper.insertList(dataLogEducationlist);
	         //工作背景
	         ProjectSendTeamMemberWork dataLogEducation3 =new ProjectSendTeamMemberWork();
	         dataLogEducation3.setProjectSendTeamMemberId(body.getId());
	         projectSendTeamMemberWorkMapper.delete(dataLogEducation3);
	        String[] workArry =work.split(",");
	        List<ProjectSendTeamMemberWork> dataLogworklist =new Page<ProjectSendTeamMemberWork>();
	        for (int a=0; a < workArry.length;a++){
	            ProjectSendTeamMemberWork dataLogEducation =new ProjectSendTeamMemberWork();
	            dataLogEducation.setWorkExperience(workArry[a]);
	            dataLogEducation.setProjectSendTeamMemberId(projectSendTeamMember.getId());
	            dataLogworklist.add(dataLogEducation);
	        }
	         projectSendTeamMemberWorkMapper.insertList(dataLogworklist);
	         result.setStatus(200);
	         result.setMessage("保存成功");
	         return result;
	    }


	@Override
	public CommonDto<List<ProjectSendTeamMember>> serachTeam(String  tsid, Integer userId) {
		CommonDto<List<ProjectSendTeamMember>> result = new CommonDto<List<ProjectSendTeamMember>>();
		int projectId=0;
		if(tsid==null || "".equals(tsid)){
			projectId=0;
			
		}else{
			projectId=Integer.parseInt(tsid);
		}
		List<ProjectSendTeamMember> list1 =projectSendTeamMemberMapper.searchTeam(projectId,userId);
        result.setData(list1);
		return result;
	}
}