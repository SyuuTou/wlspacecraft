package com.lhjl.tzzs.proxy.service.impl;

        import com.lhjl.tzzs.proxy.dto.*;
        import com.lhjl.tzzs.proxy.dto.flow.FlowModel;
        import com.lhjl.tzzs.proxy.dto.flow.User;
        import com.lhjl.tzzs.proxy.mapper.*;
        import com.lhjl.tzzs.proxy.model.*;
        import com.lhjl.tzzs.proxy.service.*;
        import com.lhjl.tzzs.proxy.service.bluewave.UserLoginService;
        import com.lhjl.tzzs.proxy.service.common.CommonUserService;
        import com.lhjl.tzzs.proxy.utils.DateUtils;
        import com.lhjl.tzzs.proxy.utils.MD5Util;
        import org.apache.commons.lang3.StringUtils;
        import org.joda.time.DateTime;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.core.ParameterizedTypeReference;
        import org.springframework.http.*;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;
        import org.springframework.web.client.RestClientException;
        import org.springframework.web.client.RestTemplate;
        import tk.mybatis.mapper.entity.Example;

        import javax.annotation.Resource;
        import java.math.BigDecimal;
        import java.text.SimpleDateFormat;
        import java.util.*;

/**
 * Created by 蓝海巨浪 on 2017/10/24.
 */
@Service
public class InvestorsDemandServiceImpl extends GenericService implements InvestorsDemandService{

    @Value("${pageNum}")
    private Integer defaultPageNum;

    @Value("${pageSize}")
    private Integer defaultPageSize;

    @Value("${event.trigger.url}")
    private String eventTriggerUrl;

    @Resource
    private CommonUserService commonUserService;

    @Autowired
    private InvestorDemandMapper investorDemandMapper;

    @Resource
    private EvaluateService evaluateService;

    @Resource
    private UserLoginService userLoginService;

    @Autowired
    private InvestorDemandSegmentationService investorDemandSegmentationService;

    @Autowired
    private InvestorDemandCharacterService investorDemandCharacterService;

    @Autowired
    private InvestorDemandSpeedwayService investorDemandSpeedwayService;

    @Autowired
    private InvestorDemandStageService investorDemandStageService;

    @Autowired
    private InvestorDemandSegmentationMapper investorDemandSegmentationMapper;

    @Autowired
    private InvestorDemandStageMapper investorDemandStageMapper;

    @Autowired
    private InvestorDemandCharacterMapper investorDemandCharacterMapper;

    @Autowired
    private InvestorDemandSpeedwayMapper investorDemandSpeedwayMapper;

    @Autowired
    private MetaProjectStageMapper metaProjectStageMapper;
    @Autowired
    private UserTokenMapper userTokenMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private RestTemplate restTemplate;

    //投资阶段
    private static final String[] ROUNDNAME = {"种子轮","天使轮","Pre-A轮","A轮","B轮","C轮","Pre-IPO轮","战略投资","并购"};

    //地域偏好
    private static final String[] SECTION = {"全球","中国","海外","硅谷","华南","华东","华北","西南","北京","上海","广州","深圳","成都","厦门","福州","长沙"
            ,"武汉","西安","大连","天津","杭州","南京","苏州","青岛"};

    private final static String EVENT_KEY_URL = "/event/message/%s";

    @Override
    public CommonDto<String> newulingyu(Integer appId, InvestorsDemandDto body) {

        CommonDto<String> result = new CommonDto<>();
        InvestorDemand investorDemand = new InvestorDemand();
        investorDemand.setAppid(appId);
        int userId = commonUserService.getLocalUserId(body.getToken());
        investorDemand.setUserid(userId);
        Users users = new Users();
        users.setId(userId);
        Users users1 = usersMapper.selectByPrimaryKey(users);
        investorDemand.setCompanyName(users1.getCompanyName());
        investorDemand.setCompanyDuties(users1.getCompanyDuties());
        investorDemand.setUserName(users1.getActualName());
        investorDemand.setPhonenumber(users1.getPhonenumber());

        if(!StringUtils.isEmpty(body.getIndustryta7tradename())){
            investorDemand.setIndustry(body.getIndustryta7tradename());
        }

        if(!StringUtils.isEmpty(body.getTouzi())){
            investorDemand.setFinancingStage(body.getTouzi());
        }
        if(!StringUtils.isEmpty(body.getCity())){
            investorDemand.setCityPreference(body.getCity());
        }
        if(body.getXiaxian() != null && !"".equals(body.getXiaxian())){
            investorDemand.setInvestmentAmountLow(new BigDecimal(body.getXiaxian()));
        }
        if(!StringUtils.isEmpty(body.getShangxian())){
            investorDemand.setInvestmentAmountHigh(new BigDecimal(body.getShangxian()));
        }
        if (body.getStartdoller() != null){
            investorDemand.setInvestmentAmountLowDollars(body.getStartdoller());
        }
        if (body.getEnddoller() != null){
            investorDemand.setInvestmentAmountHighDollars(body.getEnddoller());
        }
        if(!StringUtils.isEmpty(body.getUser7recentlyco_noana())){
            investorDemand.setRecentlyConcernedSubdivisionCircuit(body.getUser7recentlyco_noana());
        }
        if(!StringUtils.isEmpty(body.getUser7foundertra_noana())){
            investorDemand.setConcernedFoundersCharacteristic(body.getUser7foundertra_noana());
        }
        if(!StringUtils.isEmpty(body.getXuqiu())){
            investorDemand.setDemand(body.getXuqiu());
        }

        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Example idExample = new Example(InvestorDemand.class);
        idExample.and().andEqualTo("userid",userId);
        idExample.setOrderByClause("creat_time desc");

        //Integer investorDemandId = null;
        String createTime = null;
        List<InvestorDemand> investorDemandList = investorDemandMapper.selectByExample(idExample);
        if (investorDemandList.size() > 0){
            Integer investorDemandId = investorDemandList.get(0).getId();
            investorDemand.setId(investorDemandId);
            try {
                createTime = sdf.format(new Date(now));
            } catch (Exception e) {
                e.printStackTrace();
            }
            investorDemand.setUpdateTime(DateUtils.parse(createTime));//如果不是第一次，就是更新时间
            investorDemandMapper.updateByPrimaryKeySelective(investorDemand);

        }else {
            try {
                createTime = sdf.format(new Date(now));
            } catch (Exception e) {
                e.printStackTrace();
            }
            investorDemand.setCreatTime(DateUtils.parse(createTime));//如果是第一次插入，为创建时间
            investorDemandMapper.insertSelective(investorDemand);
        }

        investorDemandStageService.deleteAll(investorDemand.getId());
        List<InvestorDemandStage> investorDemandStageList = new ArrayList<>();
        if(StringUtils.isEmpty(body.getTouzi())){
            InvestorDemandStage investorDemandStage1 = new InvestorDemandStage();
            investorDemandStage1.setInvestorDemandId(investorDemand.getId());
            investorDemandStage1.setAppid(appId);
            investorDemandStage1.setMetaProjectStageId(null);
            investorDemandStageList.add(investorDemandStage1);
        }else{
            String[] investDemandStages = body.getTouzi().trim().split(",");
            for(int i = 0; i<investDemandStages.length; i++){
                InvestorDemandStage investorDemandStage1 = new InvestorDemandStage();
                investorDemandStage1.setInvestorDemandId(investorDemand.getId());
                investorDemandStage1.setAppid(appId);
                Integer stageId = metaProjectStageMapper.selectIdByStageName(investDemandStages[i]);
                investorDemandStage1.setMetaProjectStageId(stageId);
                investorDemandStageList.add(investorDemandStage1);
            }
        }
        investorDemandStageService.insertList(investorDemandStageList);

        investorDemandSegmentationService.deleteAll(investorDemand.getId());
        List<InvestorDemandSegmentation> investorDemandSegmentationList = new ArrayList<>();
        if(StringUtils.isEmpty(body.getIndustryta7tradename())){
            InvestorDemandSegmentation investorDemandSegmentation1 = new InvestorDemandSegmentation();
            investorDemandSegmentation1.setInvestorDemandId(investorDemand.getId());
            investorDemandSegmentation1.setAppid(appId);
            investorDemandSegmentation1.setSegmentation("");
            investorDemandSegmentationList.add(investorDemandSegmentation1);
        }else{
            String[] investorDemandSegmentations = body.getIndustryta7tradename().trim().split(",");
            for(int i = 0; i<investorDemandSegmentations.length; i++){
                InvestorDemandSegmentation investorDemandSegmentation1 = new InvestorDemandSegmentation();
                investorDemandSegmentation1.setInvestorDemandId(investorDemand.getId());
                investorDemandSegmentation1.setAppid(appId);
                investorDemandSegmentation1.setSegmentation(investorDemandSegmentations[i]);
                investorDemandSegmentationList.add(investorDemandSegmentation1);
            }
        }
        investorDemandSegmentationService.insertList(investorDemandSegmentationList);

        investorDemandSpeedwayService.deleteAll(investorDemand.getId());
        List<InvestorDemandSpeedway> investorDemandSpeedwayList = new ArrayList<>();
        if(StringUtils.isEmpty(body.getUser7recentlyco_noana())){
            InvestorDemandSpeedway investorDemandSpeedway1 = new InvestorDemandSpeedway();
            investorDemandSpeedway1.setInvestorDemandId(investorDemand.getId());
            investorDemandSpeedway1.setAppid(appId);
            investorDemandSpeedway1.setSpeedway("");
            investorDemandSpeedwayList.add(investorDemandSpeedway1);
        }else{
            String[] investorDemandSpeedways = body.getUser7recentlyco_noana().trim().split(",");
            for(int i = 0; i<investorDemandSpeedways.length; i++){
                InvestorDemandSpeedway investorDemandSpeedway1 = new InvestorDemandSpeedway();
                investorDemandSpeedway1.setInvestorDemandId(investorDemand.getId());
                investorDemandSpeedway1.setAppid(appId);
                investorDemandSpeedway1.setSpeedway(investorDemandSpeedways[i]);
                investorDemandSpeedwayList.add(investorDemandSpeedway1);
            }
        }
        investorDemandSpeedwayService.insertList(investorDemandSpeedwayList);

        investorDemandCharacterService.deleteAll(investorDemand.getId());
        List<InvestorDemandCharacter> investorDemandCharacterList = new ArrayList<>();
        if(StringUtils.isEmpty(body.getUser7foundertra_noana())){
            InvestorDemandCharacter investorDemandCharacter1 = new InvestorDemandCharacter();
            investorDemandCharacter1.setInvestorDemandId(investorDemand.getId());
            investorDemandCharacter1.setAppid(appId);
            investorDemandCharacter1.setCharacter("");
            investorDemandCharacterList.add(investorDemandCharacter1);
        }else{
            String[] investorDemandCharacters = body.getUser7foundertra_noana().trim().split(",");
            for(int i = 0; i<investorDemandCharacters.length; i++){
                InvestorDemandCharacter investorDemandCharacter1 = new InvestorDemandCharacter();
                investorDemandCharacter1.setInvestorDemandId(investorDemand.getId());
                investorDemandCharacter1.setAppid(appId);
                investorDemandCharacter1.setCharacter(investorDemandCharacters[i]);
                investorDemandCharacterList.add(investorDemandCharacter1);
            }
        }
        investorDemandCharacterService.insertList(investorDemandCharacterList);

        result.setStatus(200);
        result.setMessage("投资偏好记录成功");
        return result;
    }

    /**
     * 投资偏好回显
     * @param token 用户token
     * @return
     */
    @Override
    public CommonDto<Map<String, Object>> newttouzilyrz(String token) {
        CommonDto<Map<String, Object>>  result = new CommonDto<>();
        Map<String, Object> data = new HashMap<>();

        CommonDto<Map<String, List<LabelList>>> hotsdatas = evaluateService.queryHotData();
        //获取记录信息
        int userId = commonUserService.getLocalUserId(token);
        Example example = new Example(InvestorDemand.class);
        example.and().andEqualTo("userid", userId);
        example.setOrderByClause("creat_time desc");
        List<InvestorDemand> investorDemands = investorDemandMapper.selectByExample(example);
        InvestorDemand investorDemand = null;
        if(investorDemands.size() > 0){
            investorDemand = investorDemands.get(0);
        }
        //返回上下限
        BigDecimal startdoller = BigDecimal.ZERO;
        BigDecimal enddoller = BigDecimal.ZERO;

        if (investorDemand == null){

        }else {
            if (investorDemand.getInvestmentAmountLowDollars() != null){
                startdoller = investorDemand.getInvestmentAmountLowDollars();
            }
            if (investorDemand.getInvestmentAmountHighDollars() != null){
                enddoller = investorDemand.getInvestmentAmountHighDollars();
            }

        }
        if (startdoller.equals(BigDecimal.ZERO)){
            data.put("startdoller","");
        }else {
            data.put("startdoller",startdoller);
        }

        if (enddoller.equals(BigDecimal.ZERO)){
            data.put("enddoller","");
        }else {
            data.put("enddoller",enddoller);
        }



        //行业领域(send_logs)
        List<LabelList> industrys = hotsdatas.getData().get("industryKey");
        List<InvestorsDemandLabel> demandLabels = new ArrayList<>();
        for(int i = 0; i<industrys.size(); i++){
            InvestorsDemandLabel label = new InvestorsDemandLabel();
            label.setIndustrycl7describe(i);
            label.setIndustrycl7orderofarr(i+1);
            label.setIndustrycl7tradename(industrys.get(i).getName());
            label.setIndustryta7tradename(industrys.get(i).getName());
            label.setIsgouxuan(false);
            if(investorDemand != null && investorDemand.getIndustry() != null && !"".equals(investorDemand.getIndustry())){
                String[] industryArray = investorDemand.getIndustry().split(",");
                for(String string : industryArray){
                    if(string.equals(industrys.get(i).getName())){
                        label.setIsgouxuan(true);
                    }
                }
            }
            demandLabels.add(label);
        }
        data.put("shuzu", demandLabels);

        //投资阶段
        List<String> roundsNames = new ArrayList<>();
//        for(String roundName : ROUNDNAME){
//            roundsNames.add(false);
//        }
//        for(int i = 0; i<ROUNDNAME.length; i++){
//            if(investorDemand != null && investorDemand.getFinancingStage() != null && !"".equals(investorDemand.getFinancingStage())){
//                String[] roundNameArray = investorDemand.getFinancingStage().split(",");
//                for(String string : roundNameArray){
//                    if(string.equals(ROUNDNAME[i])){
//                        roundsNames.set(i, true);
//                    }
//                }
//            }
//        }
        if (investorDemand != null){
            roundsNames = Arrays.asList(investorDemand.getFinancingStage().split(","));
        }
        data.put("a", roundsNames);

        //地域偏好
        List<Boolean> cities = new ArrayList<>();
        for(String string : SECTION){
            cities.add(false);
        }
        for(int i=0; i<SECTION.length; i++){
            if(investorDemand != null && investorDemand.getCityPreference() != null && !"".equals(investorDemand.getCityPreference())){
                String[] cityArray = investorDemand.getCityPreference().split(",");
                for(String string : cityArray){
                    if(string.equals(SECTION[i])){
                        cities.set(i, true);
                    }
                }
            }
        }
        data.put("b", cities);

        if(investorDemand != null){
            data.put("user7singleinve_noana", investorDemand.getInvestmentAmountHigh());
            data.put("user7singlethro_noana", investorDemand.getInvestmentAmountLow());
            data.put("user7excessfield4", investorDemand.getDemand());
            List<String> recentlys = new ArrayList<>();
            if(investorDemand.getRecentlyConcernedSubdivisionCircuit() != null && !"".equals(investorDemand.getRecentlyConcernedSubdivisionCircuit())){
                String[] recentlyArray = investorDemand.getRecentlyConcernedSubdivisionCircuit().split(",");
                for(String string : recentlyArray){
                    recentlys.add(string);
                }
            }
            data.put("user7recentlyco_noana", recentlys);

            List<String> founders = new ArrayList<>();
            if(investorDemand.getConcernedFoundersCharacteristic() != null && !"".equals(investorDemand.getConcernedFoundersCharacteristic())){
                String[] founderArray = investorDemand.getConcernedFoundersCharacteristic().split(",");
                for(String string : founderArray){
                    founders.add(string);
                }
            }
            data.put("user7foundertra_noana", founders);
        }else{
            data.put("user7singleinve_noana", "");
            data.put("user7singlethro_noana", "");
            data.put("user7excessfield4", "");
            data.put("user7recentlyco_noana", new ArrayList<String>());
            data.put("user7foundertra_noana", new ArrayList<String>());
        }

        result.setStatus(200);
        result.setMessage("投资偏好回显数据获取成功");
        result.setData(data);
        return result;
    }

    /**
     * 判断投资偏好是否填完
     * @param token
     * @return
     */
    @Override
    public CommonDto<Map<String,Object>> investorsDemandYn(String token){
        CommonDto<Map<String,Object>> result = new CommonDto<>();
        Map<String,Object> obj = new HashMap<>();

        Integer userId = commonUserService.getLocalUserId(token);

        InvestorDemand investorDemand = new InvestorDemand();
        investorDemand.setUserid(userId);

        List<InvestorDemand> investorDemands = investorDemandMapper.select(investorDemand);
        if (investorDemands.size() > 0){
            InvestorDemand investorDemandForJudgment  = investorDemands.get(0);

            String   finacingStage = investorDemandForJudgment.getFinancingStage();
            String  industry = investorDemandForJudgment.getIndustry();
            String  demand = investorDemandForJudgment.getDemand();

            if (finacingStage == null || "".equals(finacingStage) ){
                obj.put("message","投资偏好没有填写完成");
                obj.put("success",false);

                result.setMessage("投资偏好没有填写完成");
                result.setStatus(50001);
                result.setData(obj);

                return result;
            }

            if (industry == null || "".equals(industry) ){
                obj.put("message","投资偏好没有填写完成");
                obj.put("success",false);

                result.setMessage("投资偏好没有填写完成");
                result.setStatus(50001);
                result.setData(obj);

                return result;
            }

            obj.put("message","投资偏好填写完成");
            obj.put("success",true);

            result.setData(obj);
            result.setStatus(200);
            result.setMessage("投资偏好填写完成");

            return result;
        }

        obj.put("message","投资偏好没有填写完成");
        obj.put("success",false);

        result.setMessage("投资偏好没有填写完成");
        result.setStatus(50001);
        result.setData(obj);

        return result;
    }

    /**
     * 创建投资风向标/融资需求的方法
     * @param body
     * @param appid
     * @return
     */
    @Override
    @Transactional
    public CommonDto<String> createInvestorsDemand(InvestorDemandInputsDto body, Integer appid) {
        CommonDto<String> result  = new CommonDto<>();
        Date now = new Date();
        if (body.getSaveType() != null && body.getSaveType() ==1){
            if (body.getUserId() == null){
                result.setStatus(502);
                result.setMessage("用户id不能为空");
                result.setData(null);

                return result;
            }
        }else {
            if (body.getToken() == null){
                result.setStatus(502);
                result.setData(null);
                result.setMessage("用户token不能为空");

                return result;
            }
        }


        if (body.getCharacter() == null || body.getCharacter().size() < 1){
            result.setMessage("请填写关注创始人特质");
            result.setData(null);
            result.setStatus(502);

            return result;
        }

        if (body.getFuture() == null || "".equals(body.getFuture()) || "undefined".equals(body.getFuture())){
            result.setMessage("请填写2018年展望");
            result.setData(null);
            result.setStatus(502);

            return result;
        }

        if (body.getSegmentation() == null || body.getSegmentation().size() < 1){
            result.setMessage("请选择投资领域");
            result.setData(null);
            result.setStatus(502);

            return result;
        }

        if (body.getSpeedway() == null || body.getSpeedway().size() < 1){
            result.setMessage("请输入最近关注细分赛道");
            result.setData(null);
            result.setStatus(502);

            return result;
        }

        if (body.getStage() == null || body.getStage().size() < 1){
            result.setMessage("请选择投资轮次");
            result.setData(null);
            result.setStatus(502);

            return result;
        }

        if (appid == null){
            result.setMessage("请配置应用id");
            result.setData(null);
            result.setStatus(502);

            return result;
        }
        Integer userId = -1;
        if (body.getSaveType() != null && body.getSaveType() ==1){
            userId = body.getUserId();
        }else {
            userId = userLoginService.getUserIdByToken(body.getToken(),appid);
        }
        if (userId == -1){
            result.setStatus(502);
            result.setData(null);
            result.setMessage("用户token无效");

            return result;
        }

        //兼容老版本
        String industry = null;
        if (body.getSegmentation().size() > 0){
            industry = String.join(",",body.getSegmentation());
        }

        String financingStage = null;
        if (body.getStage().size() > 0){
            financingStage = String.join(",",body.getSegmentation());
        }

        String speedWay = null;
        if (body.getSpeedway().size() > 0){
            speedWay = String.join(",",body.getSpeedway());
        }

        String character = null;
        if (body.getCharacter().size() > 0){
            character = String.join(",",body.getCharacter());
        }


        //准备数据
        InvestorDemand investorDemand = new InvestorDemand();
        investorDemand.setUserid(userId);
        investorDemand.setAppid(appid);
        if (industry != null){
            investorDemand.setIndustry(industry);
        }
        if (financingStage != null){
            investorDemand.setFinancingStage(financingStage);
        }
        if (speedWay != null){
            investorDemand.setRecentlyConcernedSubdivisionCircuit(speedWay);
        }
        if (character != null){
            investorDemand.setConcernedFoundersCharacteristic(character);
        }
        if (body.getInvestmentAmountHigh() != null){
            investorDemand.setInvestmentAmountHigh(body.getInvestmentAmountHigh());
        }
        if (body.getInvestmentAmountLow() != null){
            investorDemand.setInvestmentAmountLow(body.getInvestmentAmountLow());
        }
        if (body.getInvestmentAmountHighDollars() != null){
            investorDemand.setInvestmentAmountHighDollars(body.getInvestmentAmountHighDollars());
        }
        if (body.getInvestmentAmountLowDollars() != null){
            investorDemand.setInvestmentAmountLowDollars(body.getInvestmentAmountLowDollars());
        }
        investorDemand.setFuture(body.getFuture());
        investorDemand.setDemandStatus(3);//默认不完整
        investorDemand.setAppid(appid);

        //存储用户信息
        if (body.getSaveType() != null && body.getSaveType() == 1){
            if (StringUtils.isAnyBlank(body.getCompanyDuties(),body.getCompanyName(),body.getUserName())){
                result.setMessage("用户名称，公司名称，公司职位其中任何一个都不能为空");
                result.setStatus(502);
                result.setData(null);

                return result;
            }
            if (body.getDemandStatus() == null){
                result.setMessage("请输入当前风向标的状态");
                result.setStatus(502);
                result.setData(null);

                return result;
            }

            investorDemand.setUserName(body.getUserName());
            investorDemand.setCompanyDuties(body.getCompanyDuties());
            investorDemand.setCompanyName(body.getCompanyName());
            investorDemand.setPhonenumber(body.getPhonenumber());
            investorDemand.setUpdateTime(now);
            investorDemand.setDemandStatus(body.getDemandStatus());

            Users users = new Users();
            users.setId(userId);
            users.setActualName(body.getUserName());
            users.setCompanyName(body.getCompanyName());
            users.setCompanyDuties(body.getCompanyDuties());

            usersMapper.updateByPrimaryKeySelective(users);
        }else {
            Users usersInfo  = usersMapper.selectByPrimaryKey(userId);
            if (usersInfo.getCompanyName() != null){
                investorDemand.setCompanyName(usersInfo.getCompanyName());
            }
            if (usersInfo.getCompanyDuties() != null){
                investorDemand.setCompanyDuties(usersInfo.getCompanyDuties());
            }
            if (usersInfo.getActualName() != null){
                investorDemand.setUserName(usersInfo.getActualName());
            }
            if (usersInfo.getPhonenumber() != null){
                investorDemand.setPhonenumber(usersInfo.getPhonenumber());
            }
        }

        //查找原来是否有数据
        Example idExample = new Example(InvestorDemand.class);
        idExample.and().andEqualTo("userid",userId);
        idExample.setOrderByClause("creat_time desc");

        Integer investorDemandId = null;
        List<InvestorDemand> investorDemandList = investorDemandMapper.selectByExample(idExample);
        if (investorDemandList.size()>0){
            investorDemand.setId(investorDemandList.get(0).getId());
            investorDemandId = investorDemandList.get(0).getId();
            investorDemand.setCreatTime(now);
            investorDemandMapper.updateByPrimaryKeySelective(investorDemand);
        }else {
            investorDemand.setCreatTime(now);
            investorDemandMapper.insertSelective(investorDemand);
            investorDemandId = investorDemand.getId();
        }

        // 更新领域
        InvestorDemandSegmentation investorDemandSegmentation = new InvestorDemandSegmentation();
        investorDemandSegmentation.setInvestorDemandId(investorDemandId);
        investorDemandSegmentation.setAppid(appid);

        investorDemandSegmentationService.delete(investorDemandSegmentation);

        if (body.getSegmentation().size() > 0){
            for (String s:body.getSegmentation()){
                InvestorDemandSegmentation investorDemandSegmentationForInsert = new InvestorDemandSegmentation();
                investorDemandSegmentationForInsert.setAppid(appid);
                investorDemandSegmentationForInsert.setInvestorDemandId(investorDemandId);
                investorDemandSegmentationForInsert.setSegmentation(s);

                investorDemandSegmentationMapper.insertSelective(investorDemandSegmentationForInsert);
            }
        }

        // 更新赛道
        InvestorDemandSpeedway investorDemandSpeedway = new InvestorDemandSpeedway();
        investorDemandSpeedway.setInvestorDemandId(investorDemandId);
        investorDemandSpeedway.setAppid(appid);

        investorDemandSpeedwayService.delete(investorDemandSpeedway);

        if (body.getSpeedway().size() > 0){
            for (String s:body.getSpeedway()){
                InvestorDemandSpeedway investorDemandSpeedwayForInsert = new InvestorDemandSpeedway();
                investorDemandSpeedwayForInsert.setAppid(appid);
                investorDemandSpeedwayForInsert.setInvestorDemandId(investorDemandId);
                investorDemandSpeedwayForInsert.setSpeedway(s);

                investorDemandSpeedwayMapper.insertSelective(investorDemandSpeedwayForInsert);
            }
        }

        // 更新阶段
        InvestorDemandStage investorDemandStage = new InvestorDemandStage();
        investorDemandStage.setInvestorDemandId(investorDemandId);
        investorDemandStage.setAppid(appid);

        investorDemandStageMapper.delete(investorDemandStage);

        if (body.getStage().size() > 0){
            for (Integer i :body.getStage()){
                InvestorDemandStage investorDemandStageForInsert = new InvestorDemandStage();
                investorDemandStageForInsert.setInvestorDemandId(investorDemandId);
                investorDemandStageForInsert.setAppid(appid);
                investorDemandStageForInsert.setMetaProjectStageId(i);

                investorDemandStageMapper.insertSelective(investorDemandStageForInsert);
            }
        }

        // 更新特质
        InvestorDemandCharacter investorDemandCharacter = new InvestorDemandCharacter();
        investorDemandCharacter.setAppid(appid);
        investorDemandCharacter.setInvestorDemandId(investorDemandId);

        investorDemandCharacterMapper.delete(investorDemandCharacter);

        if (body.getCharacter().size() > 0){
            for (String s1:body.getCharacter()){
                InvestorDemandCharacter investorDemandCharacterForInsert = new InvestorDemandCharacter();
                investorDemandCharacterForInsert.setInvestorDemandId(investorDemandId);
                investorDemandCharacterForInsert.setCharacter(s1);
                investorDemandCharacterForInsert.setAppid(appid);

                investorDemandCharacterMapper.insertSelective(investorDemandCharacterForInsert);
            }
        }

        result.setStatus(200);
        result.setData(null);
        result.setMessage("success");

        return result;
    }

    /**
     * 获取投资风向标/融资需求列表
     * @param body
     * @param appid
     * @return
     */
    @Override
    public CommonDto<Map<String, Object>> getInvestorDemand(InvestorDemandListInputDto body, Integer appid) {
        CommonDto<Map<String,Object>> result = new CommonDto<>();
        Map<String,Object> map = new HashMap<>();
        List<InvestorDemandListOutputDto> list  = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (body.getPageNum() == null){
            body.setPageNum(defaultPageNum);
        }

        if (body.getPageSize() == null){
            body.setPageSize(defaultPageSize);
        }
        Integer isUser = null;
        Integer[] status = {};
        if (body.getIsAdmin() == null || body.getIsAdmin() != 1){
            isUser = 1;
        }else {
            if (body.getStatus() != null){
                Integer[] statusI = new Integer[body.getStatus().size()];
                for (int i = 0; i<body.getStatus().size();i++){
                    statusI[i] = body.getStatus().get(i);
                }
                status = statusI;
            }
        }

        Integer startPage = (body.getPageNum() -1)*body.getPageSize();
        Integer dataType = null;
        Integer userId = null;
        this.LOGGER.info(body+"body");
        if(body.getDataType() != null) {
            switch (body.getDataType()){
                case "Featured":
                    dataType = 1;
                    break;
                case "Latest":
                    break;
                case "Mine":
                    UserToken userToken = new UserToken();
                    userToken.setToken(body.getToken());
                    userToken = userTokenMapper.selectOne(userToken);
                    userId = userToken.getUserId();
                    isUser = null;
                    break;
            }
        }



        try {
            List<Map<String,Object>> inverstorDemandList = investorDemandMapper.getInvestorDemandList(startPage,
                    body.getPageSize(),status,isUser,null,dataType,userId);
            if (inverstorDemandList.size() > 0){
                for (Map<String,Object> inverstorMap:inverstorDemandList){
                    InvestorDemandListOutputDto investorDemandListOutputDto = new InvestorDemandListOutputDto();
                    investorDemandListOutputDto.setId((Integer)inverstorMap.get("id"));
                    if (body.getIsAdmin() != null && body.getIsAdmin() == 1){
                        investorDemandListOutputDto.setUserid((Integer)inverstorMap.get("userid"));
                    }
                    String userName = "";
                    if (inverstorMap.get("user_name") != null){
                        userName = (String)inverstorMap.get("user_name");
                    }
                    investorDemandListOutputDto.setUserName(userName);
                    String headpic = "";
                    if (inverstorMap.get("headpic") != null){
                        headpic = (String)inverstorMap.get("headpic");
                    }
                    investorDemandListOutputDto.setHeadpic(headpic);
                    String companyName = "";
                    if (inverstorMap.get("company_name") != null){
                        companyName = (String)inverstorMap.get("company_name");
                    }
                    investorDemandListOutputDto.setCompanyName(companyName);
                    String companyDuties = "";
                    if (inverstorMap.get("company_duties") != null){
                        companyDuties = (String) inverstorMap.get("company_duties");
                    }
                    investorDemandListOutputDto.setCompanyDuties(companyDuties);
                    String phonenumber = "";
                    if (inverstorMap.get("phonenumber") != null){
                        phonenumber = (String)inverstorMap.get("phonenumber");
                    }
                    investorDemandListOutputDto.setPhoneNum(phonenumber);
                    List<String> segmentation = new ArrayList<>();
                    if (inverstorMap.get("segmentation") != null){
                        String segmentationString = (String)inverstorMap.get("segmentation");
                        segmentation = Arrays.asList(segmentationString.split(","));
                    }
                    investorDemandListOutputDto.setSegmentation(segmentation);
                    List<String> speedway = new ArrayList<>();
                    if (inverstorMap.get("speedway") != null){
                        String speedwayString = (String)inverstorMap.get("speedway");
                        speedway = Arrays.asList(speedwayString.split(","));
                    }
                    investorDemandListOutputDto.setSpeedWay(speedway);
                    List<String> stage = new ArrayList<>();
                    if (inverstorMap.get("stage") != null){
                        String stageString = (String)inverstorMap.get("stage");
                        stage = Arrays.asList(stageString.split(","));
                    }
                    investorDemandListOutputDto.setStage(stage);
                    BigDecimal investmentAmountLow = BigDecimal.ZERO;
                    if (inverstorMap.get("investment_amount_low") != null){
                        investmentAmountLow = (BigDecimal)inverstorMap.get("investment_amount_low");
                    }
                    investorDemandListOutputDto.setInvestmentAmountLow(investmentAmountLow);
                    BigDecimal investmentAmountHigh = BigDecimal.ZERO;
                    if (inverstorMap.get("investment_amount_high") != null){
                        investmentAmountHigh = (BigDecimal)inverstorMap.get("investment_amount_high");
                    }
                    investorDemandListOutputDto.setInvestmentAmountHigh(investmentAmountHigh);
                    BigDecimal investmentAmountLowDollars = BigDecimal.ZERO;
                    if (inverstorMap.get("investment_amount_low_dollars") != null){
                        investmentAmountLowDollars = (BigDecimal)inverstorMap.get("investment_amount_low_dollars");
                    }
                    investorDemandListOutputDto.setInvestmentAmountLowDollars(investmentAmountLowDollars);
                    BigDecimal investmentAmountHighDollars = BigDecimal.ZERO;
                    if (inverstorMap.get("investment_amount_high_dollars") != null){
                        investmentAmountHighDollars = (BigDecimal)inverstorMap.get("investment_amount_high_dollars");
                    }
                    investorDemandListOutputDto.setInvestmentAmountHighDollars(investmentAmountHighDollars);
                    List<String> userCharacter = new ArrayList<>();
                    if (inverstorMap.get("user_character") != null){
                        String userCharacterString = (String)inverstorMap.get("user_character");
                        userCharacter = Arrays.asList(userCharacterString.split(","));
                    }
                    investorDemandListOutputDto.setCharacter(userCharacter);
                    String future = "";
                    if (inverstorMap.get("future") != null){
                        future = (String)inverstorMap.get("future");
                    }
                    investorDemandListOutputDto.setFuture(future);
                    String demandStatus = "";
                    Integer demandInteger = 3;
                    if (inverstorMap.get("demand_status") != null){
                        demandInteger  = (Integer) inverstorMap.get("demand_status");
                    }
                    switch (demandInteger){
                        case 0:demandStatus="";
                            break;
                        case 1:demandStatus = "精选";
                            break;
                        case 2:demandStatus = "资料完整";
                            break;
                        case 3:demandStatus = "资料未完整";
                    }
                    investorDemandListOutputDto.setStatus(demandStatus);
                    String updateTime = "";
                    if (inverstorMap.get("update_time") != null){
                        Date updateTimeD = (Date)inverstorMap.get("update_time");
                        updateTime = sdf.format(updateTimeD);
                    }
                    investorDemandListOutputDto.setUpdateTime(updateTime);


                    if (inverstorMap.get("event_key") == null || String.valueOf(inverstorMap.get("event_key")).equals("")){
                        InvestorDemand investorDemand = investorDemandMapper.selectByPrimaryKey(inverstorMap.get("id"));
                        investorDemand.setEventKey(MD5Util.md5Encode(DateTime.now().millisOfDay().getAsString(),""));
                        investorDemandMapper.updateByPrimaryKey(investorDemand);
                        investorDemandListOutputDto.setEventKey(investorDemand.getEventKey());
                    }else{
                        investorDemandListOutputDto.setEventKey(String.valueOf(inverstorMap.get("event_key")));

                        try {
                            String url = eventTriggerUrl+EVENT_KEY_URL;
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);

                            HttpEntity entity = new HttpEntity<>( headers);
                            url = String.format(url,String.valueOf(inverstorMap.get("event_key")));

                            ResponseEntity<CommonDto<FlowModel>> commonDto = restTemplate.exchange(url, HttpMethod.GET, entity,new ParameterizedTypeReference<CommonDto<FlowModel>>(){} );
                            investorDemandListOutputDto.setFlowModel(commonDto.getBody().getData());

                            if (null != commonDto.getBody().getData()) {
                                commonDto.getBody().getData().getLikers().forEach(v -> {
                                    if (null != v && null != v.getToken()) {
                                        if (v.getToken().equals(body.getToken())) {
                                            investorDemandListOutputDto.setCurrentUserLikeStatus(1);
                                        }
                                    }
                                });
                            }
                        } catch (RestClientException e) {
                            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
                        }
                    }

                    list.add(investorDemandListOutputDto);
                }
            }
            Integer allcount = investorDemandMapper.getInvestorDemandListCount(startPage,body.getPageSize(),status,isUser,null,dataType,userId);

            map.put("currentPage",body.getPageNum());
            map.put("total",allcount);
            map.put("pageSize",body.getPageSize());
            map.put("list",list);

            result.setData(map);
            result.setStatus(200);
            result.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    /**
     * 获取是否填写完毕的接口
     * @param token
     * @return
     */
    @Override
    public CommonDto<Map<String, Object>> getDemandCompeteYn(String token,Integer appid) {
        CommonDto<Map<String,Object>> result = new CommonDto<>();
        Map<String,Object> map = new HashMap<>();
        Date now = new Date();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (token == null || "".equals(token)){
            result.setMessage("用户token不能为空");
            result.setData(null);
            result.setStatus(502);

            return result;
        }

        Integer userId = userLoginService.getUserIdByToken(token,appid);
        if (userId == -1){
            result.setMessage("用户token非法");
            result.setData(null);
            result.setStatus(502);

            return result;
        }

        Integer completeYn = 0;
        Integer oldYn = 0;

        Map<String,Object> userDemand = investorDemandMapper.selectDemandByUserId(userId,appid);
        if(userDemand != null){
            if (userDemand.get("segmentation") != null){
                completeYn = 1;
            }
            if (userDemand.get("creat_time") != null){
                Date createTime = (Date)userDemand.get("creat_time");
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(createTime);
                calendar.add(Calendar.MONTH, 3);
                Date createTimeAfter = calendar.getTime();

                if (now.getTime() > createTimeAfter.getTime()){
                    oldYn =1;
                }
            }
        }
        map.put("completeYn",completeYn);
        map.put("oldYn",oldYn);

        result.setData(map);
        result.setStatus(200);
        result.setMessage("success");

        return result;
    }

    /**
     * 融资需求信息回显接口/投资风向标
     * @param token
     * @param appid
     * @return
     */
    @Override
    public CommonDto<InvestorDemandListOutputDto> getInvestorsDemand(String token, Integer appid) {
        CommonDto<InvestorDemandListOutputDto> result = new CommonDto<>();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (token == null || "".equals(token)){
            result.setMessage("用户token不能为空");
            result.setData(null);
            result.setStatus(502);

            return result;
        }

        Integer userId = userLoginService.getUserIdByToken(token,appid);
        if (userId == -1){
            result.setMessage("用户token非法");
            result.setData(null);
            result.setStatus(502);

            return result;
        }

        Map<String,Object> inverstorMap = investorDemandMapper.selectDemandByUserId(userId,appid);


        InvestorDemandListOutputDto investorDemandListOutputDto = new InvestorDemandListOutputDto();
        if (inverstorMap == null){
            investorDemandListOutputDto.setId(0);
            List<String> arrayList = new ArrayList<>();
            investorDemandListOutputDto.setSegmentation(arrayList);
            investorDemandListOutputDto.setSpeedWay(arrayList);
            investorDemandListOutputDto.setStage(arrayList);
            investorDemandListOutputDto.setInvestmentAmountLow(BigDecimal.ZERO);
            investorDemandListOutputDto.setInvestmentAmountHigh(BigDecimal.ZERO);
            investorDemandListOutputDto.setInvestmentAmountLowDollars(BigDecimal.ZERO);
            investorDemandListOutputDto.setInvestmentAmountHighDollars(BigDecimal.ZERO);
            investorDemandListOutputDto.setCharacter(arrayList);
            investorDemandListOutputDto.setFuture("");
        }else {

            investorDemandListOutputDto.setId((Integer)inverstorMap.get("id"));

            List<String> segmentation = new ArrayList<>();
            if (inverstorMap.get("segmentation") != null){
                String segmentationString = (String)inverstorMap.get("segmentation");
                segmentation = Arrays.asList(segmentationString.split(","));
            }
            investorDemandListOutputDto.setSegmentation(segmentation);
            List<String> speedway = new ArrayList<>();
            if (inverstorMap.get("speedway") != null){
                String speedwayString = (String)inverstorMap.get("speedway");
                speedway = Arrays.asList(speedwayString.split(","));
            }
            investorDemandListOutputDto.setSpeedWay(speedway);
            List<String> stage = new ArrayList<>();
            if (inverstorMap.get("stage") != null){
                String stageString = (String)inverstorMap.get("stage");
                stage = Arrays.asList(stageString.split(","));
            }
            investorDemandListOutputDto.setStage(stage);
            BigDecimal investmentAmountLow = BigDecimal.ZERO;
            if (inverstorMap.get("investment_amount_low") != null){
                investmentAmountLow = (BigDecimal)inverstorMap.get("investment_amount_low");
            }
            investorDemandListOutputDto.setInvestmentAmountLow(investmentAmountLow);
            BigDecimal investmentAmountHigh = BigDecimal.ZERO;
            if (inverstorMap.get("investment_amount_high") != null){
                investmentAmountHigh = (BigDecimal)inverstorMap.get("investment_amount_high");
            }
            investorDemandListOutputDto.setInvestmentAmountHigh(investmentAmountHigh);
            BigDecimal investmentAmountLowDollars = BigDecimal.ZERO;
            if (inverstorMap.get("investment_amount_low_dollars") != null){
                investmentAmountLowDollars = (BigDecimal)inverstorMap.get("investment_amount_low_dollars");
            }
            investorDemandListOutputDto.setInvestmentAmountLowDollars(investmentAmountLowDollars);
            BigDecimal investmentAmountHighDollars = BigDecimal.ZERO;
            if (inverstorMap.get("investment_amount_high_dollars") != null){
                investmentAmountHighDollars = (BigDecimal)inverstorMap.get("investment_amount_high_dollars");
            }
            investorDemandListOutputDto.setInvestmentAmountHighDollars(investmentAmountHighDollars);
            List<String> userCharacter = new ArrayList<>();
            if (inverstorMap.get("user_character") != null){
                String userCharacterString = (String)inverstorMap.get("user_character");
                userCharacter = Arrays.asList(userCharacterString.split(","));
            }
            investorDemandListOutputDto.setCharacter(userCharacter);
            String future = "";
            if (inverstorMap.get("future") != null){
                future = (String)inverstorMap.get("future");
            }
            investorDemandListOutputDto.setFuture(future);
            String demandStatus = "";
            Integer demandInteger = 3;
            if (inverstorMap.get("demand_status") != null){
                demandInteger  = (Integer) inverstorMap.get("demand_status");
            }
            switch (demandInteger){
                case 0:demandStatus="";
                    break;
                case 1:demandStatus = "精选";
                    break;
                case 2:demandStatus = "资料完整";
                    break;
                case 3:demandStatus = "资料未完整";
            }
            investorDemandListOutputDto.setStatus(demandStatus);
            String updateTime = "";
            if (inverstorMap.get("update_time") != null){
                Date updateTimeD = (Date)inverstorMap.get("update_time");
                updateTime = sdf.format(updateTimeD);
            }
            investorDemandListOutputDto.setUpdateTime(updateTime);

        }



        result.setData(investorDemandListOutputDto);
        result.setStatus(200);
        result.setMessage("success");

        return result;
    }

    @Override
    public CommonDto<Map<String, Object>> getInvestorDemandList(InvestorDemandListInputDto body, Integer appid) {

        CommonDto<Map<String,Object>> result = new CommonDto<>();
        Map<String,Object> map = new HashMap<>();
        List<InvestorDemandListOutputDto> list  = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (body.getPageNum() == null){
            body.setPageNum(defaultPageNum);
        }

        if (body.getPageSize() == null){
            body.setPageSize(defaultPageSize);
        }
        Integer isUser = null;
        Integer[] status = {};
        if (body.getIsAdmin() == null || body.getIsAdmin() != 1){
            isUser = 1;
        }else {
            if (body.getStatus() != null){
                Integer[] statusI = new Integer[body.getStatus().size()];
                for (int i = 0; i<body.getStatus().size();i++){
                    statusI[i] = body.getStatus().get(i);
                }
                status = statusI;
            }
        }

        Integer startPage = (body.getPageNum() -1)*body.getPageSize();
        Integer dataType = null;
        Integer userId = null;
        this.LOGGER.info(body+"body");
        if(body.getDataType() != null) {
            switch (body.getDataType()){
                case "Featured":
                    dataType = 1;
                    break;
                case "Latest":
                    break;
                case "Mine":
                    UserToken userToken = new UserToken();
                    userToken.setToken(body.getToken());

                    userId = userToken.getUserId();
                    break;
            }
        }



        try {
            List<Map<String,Object>> inverstorDemandList = investorDemandMapper.getInvestorDemandListBackStage(startPage,
                    body.getPageSize(),status,isUser,null,dataType,userId);
            if (inverstorDemandList.size() > 0){
                for (Map<String,Object> inverstorMap:inverstorDemandList){
                    InvestorDemandListOutputDto investorDemandListOutputDto = new InvestorDemandListOutputDto();
                    investorDemandListOutputDto.setId((Integer)inverstorMap.get("id"));
                    if (body.getIsAdmin() != null && body.getIsAdmin() == 1){
                        investorDemandListOutputDto.setUserid((Integer)inverstorMap.get("userid"));
                    }
                    String userName = "";
                    if (inverstorMap.get("user_name") != null){
                        userName = (String)inverstorMap.get("user_name");
                    }
                    investorDemandListOutputDto.setUserName(userName);
                    String headpic = "";
                    if (inverstorMap.get("headpic") != null){
                        headpic = (String)inverstorMap.get("headpic");
                    }
                    investorDemandListOutputDto.setHeadpic(headpic);
                    String companyName = "";
                    if (inverstorMap.get("company_name") != null){
                        companyName = (String)inverstorMap.get("company_name");
                    }
                    investorDemandListOutputDto.setCompanyName(companyName);
                    String companyDuties = "";
                    if (inverstorMap.get("company_duties") != null){
                        companyDuties = (String) inverstorMap.get("company_duties");
                    }
                    investorDemandListOutputDto.setCompanyDuties(companyDuties);
                    String phonenumber = "";
                    if (inverstorMap.get("phonenumber") != null){
                        phonenumber = (String)inverstorMap.get("phonenumber");
                    }
//            	    String headpic = "";
//            	    if (inverstorMap.get("headpic") != null){
//            	        headpic = (String)inverstorMap.get("headpic");
//            	    }
//            	    investorDemandListOutputDto.setHeadpic(headpic);
//            	    String companyName = "";
//            	    if (inverstorMap.get("short_name") != null){
//            	        companyName = (String)inverstorMap.get("short_name");
//            	    }
//            	    investorDemandListOutputDto.setCompanyName(companyName);
//            	    String companyDuties = "";
//            	    if (inverstorMap.get("position") != null){
//            	        companyDuties = (String) inverstorMap.get("position");
//            	    }
//            	    investorDemandListOutputDto.setCompanyDuties(companyDuties);
//            	    String phonenumber = "";
//            	    if (inverstorMap.get("phone") != null){
//            	        phonenumber = (String)inverstorMap.get("phone");
//            	    }
                    investorDemandListOutputDto.setPhoneNum(phonenumber);
                    List<String> segmentation = new ArrayList<>();
                    if (inverstorMap.get("segmentation") != null){
                        String segmentationString = (String)inverstorMap.get("segmentation");
                        segmentation = Arrays.asList(segmentationString.split(","));
                    }
                    investorDemandListOutputDto.setSegmentation(segmentation);
                    List<String> speedway = new ArrayList<>();
                    if (inverstorMap.get("speedway") != null){
                        String speedwayString = (String)inverstorMap.get("speedway");
                        speedway = Arrays.asList(speedwayString.split(","));
                    }
                    investorDemandListOutputDto.setSpeedWay(speedway);
                    List<String> stage = new ArrayList<>();
                    if (inverstorMap.get("stage") != null){
                        String stageString = (String)inverstorMap.get("stage");
                        stage = Arrays.asList(stageString.split(","));
                    }
                    investorDemandListOutputDto.setStage(stage);
                    BigDecimal investmentAmountLow = BigDecimal.ZERO;
                    if (inverstorMap.get("investment_amount_low") != null){
                        investmentAmountLow = (BigDecimal)inverstorMap.get("investment_amount_low");
                    }
                    investorDemandListOutputDto.setInvestmentAmountLow(investmentAmountLow);
                    BigDecimal investmentAmountHigh = BigDecimal.ZERO;
                    if (inverstorMap.get("investment_amount_high") != null){
                        investmentAmountHigh = (BigDecimal)inverstorMap.get("investment_amount_high");
                    }
                    investorDemandListOutputDto.setInvestmentAmountHigh(investmentAmountHigh);
                    BigDecimal investmentAmountLowDollars = BigDecimal.ZERO;
                    if (inverstorMap.get("investment_amount_low_dollars") != null){
                        investmentAmountLowDollars = (BigDecimal)inverstorMap.get("investment_amount_low_dollars");
                    }
                    investorDemandListOutputDto.setInvestmentAmountLowDollars(investmentAmountLowDollars);
                    BigDecimal investmentAmountHighDollars = BigDecimal.ZERO;
                    if (inverstorMap.get("investment_amount_high_dollars") != null){
                        investmentAmountHighDollars = (BigDecimal)inverstorMap.get("investment_amount_high_dollars");
                    }
                    investorDemandListOutputDto.setInvestmentAmountHighDollars(investmentAmountHighDollars);
                    List<String> userCharacter = new ArrayList<>();
                    if (inverstorMap.get("user_character") != null){
                        String userCharacterString = (String)inverstorMap.get("user_character");
                        userCharacter = Arrays.asList(userCharacterString.split(","));
                    }
                    investorDemandListOutputDto.setCharacter(userCharacter);
                    String future = "";
                    if (inverstorMap.get("future") != null){
                        future = (String)inverstorMap.get("future");
                    }
                    investorDemandListOutputDto.setFuture(future);
                    String demandStatus = "";
                    Integer demandInteger = 3;
                    if (inverstorMap.get("demand_status") != null){
                        demandInteger  = (Integer) inverstorMap.get("demand_status");
                    }
                    switch (demandInteger){
                        case 0:demandStatus="";
                            break;
                        case 1:demandStatus = "精选";
                            break;
                        case 2:demandStatus = "资料完整";
                            break;
                        case 3:demandStatus = "资料未完整";
                    }
                    investorDemandListOutputDto.setStatus(demandStatus);
                    String updateTime = "";
                    if (inverstorMap.get("update_time") != null){
                        Date updateTimeD = (Date)inverstorMap.get("update_time");
                        updateTime = sdf.format(updateTimeD);
                    }
                    investorDemandListOutputDto.setUpdateTime(updateTime);


                    if (inverstorMap.get("event_key") == null || String.valueOf(inverstorMap.get("event_key")).equals("")){
                        InvestorDemand investorDemand = investorDemandMapper.selectByPrimaryKey(inverstorMap.get("id"));
                        investorDemand.setEventKey(MD5Util.md5Encode(DateTime.now().millisOfDay().getAsString(),""));
                        investorDemandMapper.updateByPrimaryKey(investorDemand);
                        investorDemandListOutputDto.setEventKey(investorDemand.getEventKey());
                    }else{
                        investorDemandListOutputDto.setEventKey(String.valueOf(inverstorMap.get("event_key")));

                        try {
                            String url = eventTriggerUrl+EVENT_KEY_URL;
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);

                            HttpEntity entity = new HttpEntity<>( headers);
                            url = String.format(url,String.valueOf(inverstorMap.get("event_key")));

                            ResponseEntity<CommonDto<FlowModel>> commonDto = restTemplate.exchange(url, HttpMethod.GET, entity,new ParameterizedTypeReference<CommonDto<FlowModel>>(){} );
                            investorDemandListOutputDto.setFlowModel(commonDto.getBody().getData());

                            if (null != commonDto.getBody().getData()) {
                                commonDto.getBody().getData().getLikers().forEach(v -> {
                                    if (null != v && null != v.getToken()) {
                                        if (v.getToken().equals(body.getToken())) {
                                            investorDemandListOutputDto.setCurrentUserLikeStatus(1);
                                        }
                                    }
                                });
                            }
                        } catch (RestClientException e) {
                            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
                        }
                    }

                    list.add(investorDemandListOutputDto);
                }
            }
            Integer allcount = investorDemandMapper.getInvestorDemandListCount(startPage,body.getPageSize(),status,isUser,null,dataType,userId);

            map.put("currentPage",body.getPageNum());
            map.put("total",allcount);
            map.put("pageSize",body.getPageSize());
            map.put("list",list);

            result.setData(map);
            result.setStatus(200);
            result.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;

    }
}
