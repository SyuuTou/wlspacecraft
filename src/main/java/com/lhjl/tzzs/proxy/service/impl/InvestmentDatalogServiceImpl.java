package com.lhjl.tzzs.proxy.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.lhjl.tzzs.proxy.model.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.LabelList;
import com.lhjl.tzzs.proxy.mapper.DataLogDomainMapper;
import com.lhjl.tzzs.proxy.mapper.DataLogEducationMapper;
import com.lhjl.tzzs.proxy.mapper.DataLogWorkMapper;
import com.lhjl.tzzs.proxy.mapper.FoundersEducationMapper;
import com.lhjl.tzzs.proxy.mapper.FoundersMapper;
import com.lhjl.tzzs.proxy.mapper.FoundersWorkMapper;
import com.lhjl.tzzs.proxy.mapper.InvestmentDataLogMapper;
import com.lhjl.tzzs.proxy.mapper.InvestmentInstitutionsMapper;
import com.lhjl.tzzs.proxy.mapper.InvestorsMapper;
import com.lhjl.tzzs.proxy.mapper.ProjectFinancingLogMapper;
import com.lhjl.tzzs.proxy.mapper.ProjectSegmentationMapper;
import com.lhjl.tzzs.proxy.mapper.ProjectsMapper;
import com.lhjl.tzzs.proxy.mapper.UsersMapper;
import com.lhjl.tzzs.proxy.service.EvaluateService;
import com.lhjl.tzzs.proxy.service.InvestmentDatalogService;

import tk.mybatis.mapper.util.StringUtil;
@Service
public class InvestmentDatalogServiceImpl implements InvestmentDatalogService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(InvestmentDataImpl.class);

    //投资机构表
    @Resource
    private InvestmentInstitutionsMapper investmentInstitutionsMapper;

    //创始人表
    @Resource
    private FoundersMapper foundersMapper;

    //投融资项目表
    @Resource
    private ProjectsMapper projectsMapper;

    //项目融资记录表
    @Resource
    private ProjectFinancingLogMapper projectFinancingLogMapper;

    //投资人教育经历
    @Resource
    private FoundersEducationMapper foundersEducationMapper;

    //投资人工作经历
    @Resource
    private FoundersWorkMapper foundersWorkMapper;

    //项目细分领域表
    @Resource
    private ProjectSegmentationMapper projectSegmentationMapper;

    //投资人表
    @Resource
    private InvestorsMapper investorsMapper;

    //用户表
    @Resource
    private UsersMapper usersMapper;
    //记录表
    @Resource
    private  InvestmentDataLogMapper investmentDataLogMapper;

    @Resource
    private DataLogDomainMapper dataLogDomainMapper;

    @Resource
    private DataLogEducationMapper  dataLogEducationMapper;

    @Resource
    private  DataLogWorkMapper dataLogWorkMapper;

    @Resource
    private EvaluateService evaluateService;

    /**
     * 首次添加
     */

    @Transactional
    @Override
    public CommonDto<String> saveInvestmentData(String investment_institution_name , String project_name, String project_full_name, String summary, String field,  String city, String rounds, String amount,  String currency,  String stock_right,  Date dateDate,  String founder_name, String founder_work, String founder_education,String userId,String zhiwu){
        CommonDto<String> result = new CommonDto<String>();


        //参数验证
        if (StringUtil.isEmpty(investment_institution_name)){
            result.setStatus(50001);
            result.setMessage("请填写投资机构名称");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(project_name)){
            result.setStatus(50001);
            result.setMessage("请填写投资项目简称");
            result.setData(null);

            return result;
        }
//        if (StringUtil.isEmpty(project_full_name)){
//            result.setStatus(50001);
//            result.setMessage("请填写投资项目的工商注册全称");
//            result.setData(null);
//
//            return result;
//        }
//        if (StringUtil.isEmpty(summary)){
//            result.setStatus(50001);
//            result.setMessage("请填写一句话介绍");
//            result.setData(null);
//
//            return result;
//        }else if (summary.length()>50){
//            result.setStatus(50003);
//            result.setMessage("一句话介绍长度不能超过50个字符");
//            result.setData(null);
//
//            return result;
//        }
        if (StringUtil.isEmpty(field)){
            result.setStatus(50001);
            result.setMessage("请填写所属细分领域");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(city)){
            result.setStatus(50001);
            result.setMessage("请填写地域");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(rounds)){
            result.setStatus(50001);
            result.setMessage("请填写投资轮次");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(amount)){
            result.setStatus(50001);
            result.setMessage("请填写投资金额");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(currency)){
            result.setStatus(50001);
            result.setMessage("请填写投资币种");
            result.setData(null);

            return result;
        }
//        if (StringUtil.isEmpty(stock_right)){
//            result.setStatus(50001);
//            result.setMessage("请填写股份占比");
//            result.setData(null);
//
//            return result;
//        }
        if (dateDate  == null){
            result.setStatus(50001);
            result.setMessage("请填写投资时间");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(founder_name)){
            result.setStatus(50001);
            result.setMessage("请填写创始人姓名");
            result.setData(null);

            return result;
        }
//        if (StringUtil.isEmpty(founder_work)){
//            result.setStatus(50001);
//            result.setMessage("请填写创始人工作背景");
//            result.setData(null);
//
//            return result;
//        }
//        if (StringUtil.isEmpty(founder_education)){
//            result.setStatus(50001);
//            result.setMessage("请填写创始人毕业院校");
//            result.setData(null);
//
//            return result;
//        }
        if (StringUtil.isEmpty(userId)){
            result.setStatus(50001);
            result.setMessage("缺少用户id");
        }
        //插入主表信息
        Integer userID= usersMapper.findByUuid(userId);
        InvestmentDataLog i2 =new InvestmentDataLog();
        i2.setUserId(userID);
        i2.setInstitutionalName(investment_institution_name);
        i2.setShortName(project_name);
        i2.setStage(rounds);
        InvestmentDataLog one = investmentDataLogMapper.selectOne(i2);
        
        if(one==null){
        int currency1 =Integer.parseInt(currency);
        InvestmentDataLog i =new InvestmentDataLog();
        i.setYn(currency1);
        i.setCity(city);
        BigDecimal bigDecimalAmount = new BigDecimal(amount);
            if(stock_right ==null || "".equals(stock_right)) {

            }else{
                BigDecimal bigDecimalStockRight = new BigDecimal(stock_right);
                i.setStockRight(bigDecimalStockRight);
            }
        i.setAmont(bigDecimalAmount);
        i.setCompanyName(project_full_name);
        i.setCreateName(founder_name);
        i.setCreateTime(new Date());
        i.setFinanTime(dateDate);
        i.setInstitutionalName(investment_institution_name);
        i.setShortName(project_name);
        i.setStage(rounds);
        i.setUserId(userID);
        i.setWordIntroduction(summary);
        i.setYn(currency1);
        i.setZhiwu(zhiwu);
        //
        i.setAuditYn(0);
        investmentDataLogMapper.insert(i);
        //插入细分领域
        String[] fieldArry = field.split(",");
        List<DataLogDomain> projectSegmentationList =new Page<DataLogDomain>();
        for (int a=0; a < fieldArry.length;a++){
            DataLogDomain projectSegmentation =new DataLogDomain();
            projectSegmentation.setCreateTime(new Date());
            projectSegmentation.setDomainName(fieldArry[a]);
            projectSegmentation.setLogId(i.getId());
            projectSegmentation.setUserId(userID);
            projectSegmentationList.add(projectSegmentation);
        }
        dataLogDomainMapper.insertList(projectSegmentationList);
        //教育背景
         if(founder_education ==null || "".equals(founder_education))  {

         } else {
             String[] educationArry = founder_education.split(",");
             List<DataLogEducation> dataLogEducationlist = new Page<DataLogEducation>();
             for (int a = 0; a < educationArry.length; a++) {
                 DataLogEducation dataLogEducation = new DataLogEducation();
                 dataLogEducation.setEducationName(educationArry[a]);
                 dataLogEducation.setLogId(i.getId());
                 dataLogEducation.setUserId(userID);
                 dataLogEducationlist.add(dataLogEducation);
             }
             dataLogEducationMapper.insertList(dataLogEducationlist);
         }
        //工作背景
            if(founder_work ==null || "".equals(founder_work))  {

            }else {
                String[] workArry = founder_work.split(",");
                List<DataLogWork> dataLogWorklist = new Page<DataLogWork>();
                for (int a = 0; a < workArry.length; a++) {
                    DataLogWork dataLogEducation = new DataLogWork();
                    dataLogEducation.setWorkName(workArry[a]);
                    dataLogEducation.setLogId(i.getId());
                    dataLogEducation.setUserId(userID);
                    dataLogWorklist.add(dataLogEducation);
                }
                dataLogWorkMapper.insertList(dataLogWorklist);
            }
        }else{
        	result.setStatus(204);
            result.setMessage("不能提交重复项目");
        }
        return result;
    }

    public CommonDto<List<Map<String, Object>>> saveInformation(String uuids, Integer pageNum, Integer pageSize) {
        CommonDto<List<Map<String, Object>>> result = new CommonDto<List<Map<String, Object>>>();
        List<Map<String, Object>> list =new ArrayList<Map<String, Object>>();
        Map<String, Object> map =new HashMap<>();
        Integer userId= usersMapper.findByUuid(uuids);
        Integer beginNum = (pageNum-1)*pageSize;
        //最多返回100条记录
        /*if(beginNum > 100){
            result.setStatus(201);
            result.setMessage("查询记录数超出限制（100条）");
            return result;
        }else{
            pageSize = (100 - beginNum)>=pageSize?pageSize:(100-beginNum);
        }*/
        if(userId !=0 && userId !=null){
            list=investmentDataLogMapper.saveInformation(userId, beginNum, pageSize);
            for (Map<String, Object> obj : list){
                obj.put("finan_time",String.valueOf(obj.get("finan_time")).substring(0,10));
                obj.put("create_time",String.valueOf(obj.get("create_time")).substring(0,16));
            }
            InvestmentDataLog investmentDataLog =new InvestmentDataLog();
            investmentDataLog.setUserId(userId);
            int hnum = investmentDataLogMapper.selectCount(investmentDataLog);
            map.put("hnum", hnum);

            list.add(map);
            result.setData(list);
        }else{
            result.setStatus(51001);
            result.setMessage("用户存在异常");
        }
        return result;
    }

    /**
     * 数据回显的
     * @param id
     * @return
     */
    @Override
    public CommonDto<Map<String, Object>> findinformation(Integer id) {
        CommonDto<Map<String, Object>> result =new CommonDto<Map<String, Object>>();
        CommonDto<Map<String,List<LabelList>>> data = evaluateService.queryHotData();
        Map<String,Object> map =new HashMap<>();
        InvestmentDataLog p =new InvestmentDataLog();
        p.setId(id);
        InvestmentDataLog pi = investmentDataLogMapper.selectOne(p);
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(pi.getFinanTime());
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString2 = formatter2.format(pi.getCreateTime());
        //基本信息
        map.put("FinanTime",dateString);
        map.put("createtime",dateString2);
        map.put("Institutional_name",pi.getInstitutionalName());
        map.put("short_name",pi.getShortName());
        map.put("company_name",pi.getCompanyName());
        map.put("word_introduction",pi.getWordIntroduction());
        map.put("amont",pi.getAmont());
        map.put("stock_right",pi.getStockRight());
        map.put("create_name",pi.getCreateName());
        map.put("currency",pi.getYn());
        map.put("zhiwu",pi.getZhiwu());
        map.put("id",pi.getId());
       
        if("".equals(pi.getCity())){
            List<LabelList> cities = data.getData().get("cityKey");
            map.put("city",cities);//城市
            map.put("chengshi",pi.getCity());//城市
        }else{
            List<LabelList> cities = data.getData().get("cityKey");
            String cityStr =pi.getCity();
            for(LabelList labellist : cities){
                if(cityStr.equals(labellist.getName())){
                    labellist.setChecked(true);
                }
                map.put("city",cities);//城市
                map.put("chengshi",pi.getCity());
            }
        }
        List<LabelList> industrys = data.getData().get("industryKey");
        DataLogDomain dataLogDomain =new DataLogDomain();
        dataLogDomain.setLogId(id);
        List<DataLogDomain> list = dataLogDomainMapper.select(dataLogDomain);
        if(list != null){
            List<String> a=new LinkedList<String>();
            String [] industryArray =null;
            for(DataLogDomain d:list){
                a.add(d.getDomainName());
                industryArray = new String[a.size()];
                industryArray=a.toArray(industryArray);

                for(LabelList labelList : industrys){
                    for(String string : industryArray){
                        if(string.equals(labelList.getName())){
                            labelList.setChecked(true);
                        }
                    }
                }
            }
        }
        map.put("industrys",industrys);
        //工作框回显
        DataLogWork dataLogWork =new DataLogWork();
        dataLogWork.setLogId(id);
        List<DataLogWork> work = dataLogWorkMapper.select(dataLogWork);
        if(work !=null){
        	List<String> a=new LinkedList<String>();
            String [] workArray =null;
            for(DataLogWork d:work){
                a.add(d.getWorkName());
                workArray= new String[a.size()];
                workArray=a.toArray(workArray);
        	    map.put("work",workArray);
            }
        }else{
        	result.setStatus(51002);
            result.setMessage("工作必填的");
        }
        //教育背景框回填
        DataLogEducation  dataLogEducation  =new  DataLogEducation ();
        dataLogEducation.setLogId(id);
        List<DataLogEducation> education  = dataLogEducationMapper.select(dataLogEducation );
        if(work !=null){
        	List<String> a=new LinkedList<String>();
            String [] educationArray =null;
            for(DataLogEducation d:education){
                a.add(d.getEducationName());
                educationArray= new String[a.size()];
                educationArray=a.toArray(educationArray);
        	    map.put("education",educationArray);
            }
        	
        }else{
        	result.setStatus(51003);
            result.setMessage("工作必填的");
        }
        //轮次
        List<LabelList> label =new LinkedList<>();
        LabelList labelList =new LabelList();
        labelList.setName("天使轮");
        labelList.setValue("天使轮");
        labelList.setChecked(false);

        LabelList labelList2 =new LabelList();
        labelList2.setName("Pre-A轮");
        labelList2.setValue("Pre-A轮");
        labelList2.setChecked(false);

        LabelList labelList3 =new LabelList();
        labelList3.setName("A轮");
        labelList3.setValue("A轮");
        labelList3.setChecked(false);

        LabelList labelList4 =new LabelList();
        labelList4.setName("B轮");
        labelList4.setValue("B轮");
        labelList4.setChecked(false);

        LabelList labelList5 =new LabelList();
        labelList5.setName("C轮");
        labelList5.setValue("C轮");
        labelList5.setChecked(false);
        
        label.add(labelList);
        label.add(labelList2);
        label.add(labelList3);
        label.add(labelList4);
        label.add(labelList5);
        String stage =pi.getStage();
        
        for(LabelList li : label){
            if(stage.equals(li.getName())){
            	li.setChecked(true);
            }
            }
        map.put("agency",label);//轮次
        result.setData(map);
        return result;
    }
    
    /**
     * 修改页面
     */
    
    @Transactional
    @Override
    public CommonDto<String> saveInvestmentData1(String investment_institution_name , String project_name, String project_full_name, String summary, String field,  String city, String rounds, String amount,  String currency,  String stock_right,  Date dateDate,  String founder_name, String founder_work, String founder_education,String userId,String zhiwu,Integer id){
        CommonDto<String> result = new CommonDto<String>();


        //参数验证
        if (StringUtil.isEmpty(investment_institution_name)){
            result.setStatus(50001);
            result.setMessage("请填写投资机构名称");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(project_name)){
            result.setStatus(50001);
            result.setMessage("请填写投资项目简称");
            result.setData(null);

            return result;
        }
//        if (StringUtil.isEmpty(project_full_name)){
//            result.setStatus(50001);
//            result.setMessage("请填写投资项目的工商注册全称");
//            result.setData(null);
//
//            return result;
//        }
     /*   if (StringUtil.isEmpty(summary)){
            result.setStatus(50001);
            result.setMessage("请填写一句话介绍");
            result.setData(null);

            return result;
        }else if (summary.length()>50){
            result.setStatus(50003);
            result.setMessage("一句话介绍长度不能超过50个字符");
            result.setData(null);

            return result;
        }*/
        if (StringUtil.isEmpty(field)){
            result.setStatus(50001);
            result.setMessage("请填写所属细分领域");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(city)){
            result.setStatus(50001);
            result.setMessage("请填写地域");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(rounds)){
            result.setStatus(50001);
            result.setMessage("请填写投资轮次");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(amount)){
            result.setStatus(50001);
            result.setMessage("请填写投资金额");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(currency)){
            result.setStatus(50001);
            result.setMessage("请填写投资币种");
            result.setData(null);

            return result;
        }
//        if (StringUtil.isEmpty(stock_right)){
//            result.setStatus(50001);
//            result.setMessage("请填写股份占比");
//            result.setData(null);
//
//            return result;
//        }
        if (dateDate  == null){
            result.setStatus(50001);
            result.setMessage("请填写投资时间");
            result.setData(null);

            return result;
        }
        if (StringUtil.isEmpty(founder_name)){
            result.setStatus(50001);
            result.setMessage("请填写创始人姓名");
            result.setData(null);

            return result;
        }
//        if (StringUtil.isEmpty(founder_work)){
//            result.setStatus(50001);
//            result.setMessage("请填写创始人工作背景");
//            result.setData(null);
//
//            return result;
//        }
//        if (StringUtil.isEmpty(founder_education)){
//            result.setStatus(50001);
//            result.setMessage("请填写创始人毕业院校");
//            result.setData(null);
//
//            return result;
//        }
        if (StringUtil.isEmpty(userId)){
            result.setStatus(50001);
            result.setMessage("缺少用户id");
        }
        if(id == null){
        	 result.setStatus(50001);
             result.setMessage("页面出现错误");
        }
        //插入主表信息
        Integer userID= usersMapper.findByUuid(userId);
        InvestmentDataLog i2 =new InvestmentDataLog();
        i2.setUserId(userID);
        i2.setInstitutionalName(investment_institution_name);
        i2.setShortName(project_name);
        i2.setStage(rounds);
        InvestmentDataLog one = investmentDataLogMapper.selectOne(i2);
        
        if(one==null ){

            int currency1 =Integer.parseInt(currency);
            InvestmentDataLog i =new InvestmentDataLog();
            i.setYn(currency1);
            i.setCity(city);
            BigDecimal bigDecimalAmount = new BigDecimal(amount);
            if(stock_right ==null || "".equals(stock_right)) {

            }else{
                BigDecimal bigDecimalStockRight = new BigDecimal(stock_right);
                i.setStockRight(bigDecimalStockRight);
            }
            i.setAmont(bigDecimalAmount);
            i.setCompanyName(project_full_name);
            i.setCreateName(founder_name);
            i.setCreateTime(new Date());
            i.setFinanTime(dateDate);
            i.setInstitutionalName(investment_institution_name);
            i.setShortName(project_name);
            i.setStage(rounds);
            i.setUserId(userID);
            i.setWordIntroduction(summary);
            i.setYn(currency1);
            i.setZhiwu(zhiwu);
            i.setId(id);
            //
            i.setAuditYn(0);
            investmentDataLogMapper.updateByPrimaryKey(i);
            //插入细分领域
            DataLogDomain projectSegmentation1 =new DataLogDomain();
            projectSegmentation1.setLogId(id);
            dataLogDomainMapper.delete(projectSegmentation1);
            String[] fieldArry = field.split(",");
            List<DataLogDomain> projectSegmentationList =new Page<DataLogDomain>();
            for (int a=0; a < fieldArry.length;a++){
                DataLogDomain projectSegmentation =new DataLogDomain();
                projectSegmentation.setCreateTime(new Date());
                projectSegmentation.setDomainName(fieldArry[a]);
                projectSegmentation.setLogId(id);
                projectSegmentation.setUserId(userID);
                projectSegmentationList.add(projectSegmentation);
            }
            dataLogDomainMapper.insertList(projectSegmentationList);
            //教育背景
            DataLogEducation dataLogEducation1 =new DataLogEducation();
            dataLogEducation1.setLogId(id);
            List<DataLogEducation> list =dataLogEducationMapper.select(dataLogEducation1);
            if(list.size()>0) {
                dataLogEducationMapper.delete(dataLogEducation1);
            }
            if(founder_education ==null || "".equals(founder_education))  {

            } else {
                String[] educationArry = founder_education.split(",");
                List<DataLogEducation> dataLogEducationlist = new Page<DataLogEducation>();
                for (int a = 0; a < educationArry.length; a++) {
                    DataLogEducation dataLogEducation = new DataLogEducation();
                    dataLogEducation.setEducationName(educationArry[a]);
                    dataLogEducation.setLogId(i.getId());
                    dataLogEducation.setUserId(userID);
                    dataLogEducationlist.add(dataLogEducation);
                }
                dataLogEducationMapper.insertList(dataLogEducationlist);
            }
            //工作背景
            DataLogWork dataLogEducation2 =new DataLogWork();
            dataLogEducation2.setLogId(id);
            List<DataLogWork> list2 =dataLogWorkMapper.select(dataLogEducation2);
            if(list2.size()>0) {
                dataLogWorkMapper.delete(dataLogEducation2);
            }
            if(founder_work ==null || "".equals(founder_work))  {

            }else {
                String[] workArry = founder_work.split(",");
                List<DataLogWork> dataLogWorklist = new Page<DataLogWork>();
                for (int a = 0; a < workArry.length; a++) {
                    DataLogWork dataLogEducation = new DataLogWork();
                    dataLogEducation.setWorkName(workArry[a]);
                    dataLogEducation.setLogId(i.getId());
                    dataLogEducation.setUserId(userID);
                    dataLogWorklist.add(dataLogEducation);
                }
                dataLogWorkMapper.insertList(dataLogWorklist);
            }

        }else{
        	if(id == one.getId()){
                int currency1 =Integer.parseInt(currency);
                InvestmentDataLog i =new InvestmentDataLog();
                i.setYn(currency1);
                i.setCity(city);
                BigDecimal bigDecimalAmount = new BigDecimal(amount);
                if(stock_right ==null || "".equals(stock_right)) {

                }else{
                    BigDecimal bigDecimalStockRight = new BigDecimal(stock_right);
                    i.setStockRight(bigDecimalStockRight);
                }
                i.setAmont(bigDecimalAmount);
                i.setCompanyName(project_full_name);
                i.setCreateName(founder_name);
                i.setCreateTime(new Date());
                i.setFinanTime(dateDate);
                i.setInstitutionalName(investment_institution_name);
                i.setShortName(project_name);
                i.setStage(rounds);
                i.setUserId(userID);
                i.setWordIntroduction(summary);
                i.setYn(currency1);
                i.setZhiwu(zhiwu);
                i.setId(id);
                //
                i.setAuditYn(0);
                investmentDataLogMapper.updateByPrimaryKey(i);
                //插入细分领域
                DataLogDomain projectSegmentation1 =new DataLogDomain();
                projectSegmentation1.setLogId(id);
                dataLogDomainMapper.delete(projectSegmentation1);
                String[] fieldArry = field.split(",");
                List<DataLogDomain> projectSegmentationList =new Page<DataLogDomain>();
                for (int a=0; a < fieldArry.length;a++){
                    DataLogDomain projectSegmentation =new DataLogDomain();
                    projectSegmentation.setCreateTime(new Date());
                    projectSegmentation.setDomainName(fieldArry[a]);
                    projectSegmentation.setLogId(id);
                    projectSegmentation.setUserId(userID);
                    projectSegmentationList.add(projectSegmentation);
                }
                dataLogDomainMapper.insertList(projectSegmentationList);
                //教育背景
                DataLogEducation dataLogEducation1 =new DataLogEducation();
                dataLogEducation1.setLogId(id);
                List<DataLogEducation> list =dataLogEducationMapper.select(dataLogEducation1);
                if(list.size()>0) {
                    dataLogEducationMapper.delete(dataLogEducation1);
                }
                if(founder_education ==null || "".equals(founder_education))  {

                } else {
                    String[] educationArry = founder_education.split(",");
                    List<DataLogEducation> dataLogEducationlist = new Page<DataLogEducation>();
                    for (int a = 0; a < educationArry.length; a++) {
                        DataLogEducation dataLogEducation = new DataLogEducation();
                        dataLogEducation.setEducationName(educationArry[a]);
                        dataLogEducation.setLogId(i.getId());
                        dataLogEducation.setUserId(userID);
                        dataLogEducationlist.add(dataLogEducation);
                    }
                    dataLogEducationMapper.insertList(dataLogEducationlist);
                }
                //工作背景
                DataLogWork dataLogEducation2 =new DataLogWork();
                dataLogEducation2.setLogId(id);
                List<DataLogWork> list2 =dataLogWorkMapper.select(dataLogEducation2);
                if(list2.size()>0) {
                    dataLogWorkMapper.delete(dataLogEducation2);
                }
                if(founder_work ==null || "".equals(founder_work))  {

                }else {
                    String[] workArry = founder_work.split(",");
                    List<DataLogWork> dataLogWorklist = new Page<DataLogWork>();
                    for (int a = 0; a < workArry.length; a++) {
                        DataLogWork dataLogEducation = new DataLogWork();
                        dataLogEducation.setWorkName(workArry[a]);
                        dataLogEducation.setLogId(i.getId());
                        dataLogEducation.setUserId(userID);
                        dataLogWorklist.add(dataLogEducation);
                    }
                    dataLogWorkMapper.insertList(dataLogWorklist);
                }

            }else{
        		result.setStatus(204);
                result.setMessage("不能提交重复项目");	
        	}
        }
        return result;
    }
}