package com.lhjl.tzzs.proxy.service.angeltoken.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.util.crypt.WxMaCryptUtils;
import cn.binarywang.wx.miniapp.util.json.WxMaGsonBuilder;
import com.github.binarywang.wxpay.bean.request.WxEntPayRequest;
import com.github.binarywang.wxpay.bean.result.WxEntPayResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.angeltoken.*;
import com.lhjl.tzzs.proxy.mapper.*;
import com.lhjl.tzzs.proxy.model.*;
import com.lhjl.tzzs.proxy.service.GenericService;
import com.lhjl.tzzs.proxy.service.UserInfoService;
import com.lhjl.tzzs.proxy.service.angeltoken.RedEnvelopeService;
import com.lhjl.tzzs.proxy.service.common.SessionKeyService;
import com.lhjl.tzzs.proxy.utils.MD5Util;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.util.*;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicReference;

@Transactional(readOnly = true)
@Service("redEnvelopeService")
public class RedEnvelopeServiceImpl extends GenericService implements RedEnvelopeService {

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Autowired
    private RedEnvelopeMapper redEnvelopeMapper;

    @Autowired
    private UserIntegralsMapper userIntegralsMapper;

    @Autowired
    private MetaObtainIntegralMapper metaObtainIntegralMapper;

    @Autowired
    private UserIntegralConsumeMapper userIntegralConsumeMapper;

    @Autowired
    private RedEnvelopeLimitMapper redEnvelopeLimitMapper;

    @Autowired
    private RedEnvelopeLogMapper redEnvelopeLogMapper;


    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersWeixinMapper usersWeixinMapper;

    @Autowired
    private SessionKeyService sessionKeyService;

    @Autowired
    private RedEnvelopeWechatgroupMapper redEnvelopeWechatgroupMapper;

    @Autowired
    private WxMaService wxService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WxPayConfig payConfig;
    @Autowired
    private WxPayService payService;
    @Value("${wechat.pay.entPayDescription}")
    private String entPayDescription;
    @Value("${wechat.pay.spbillCreateIp}")
    private String spbillCreateIp;


    private final static Integer obtainIntegralPeriod = 965;
    private String partnerTradeNo;


    public Integer getUserId(Integer appId, String token){

        UserToken query = new UserToken();
        query.setToken(token);
//        query.setMetaAppId(String.valueOf(appId));

        UserToken userToken = userTokenMapper.selectOne(query);

        if (null == userToken){
            return null;
        }

        return userToken.getUserId();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public CommonDto<String> checkAndAddAngelToken(Integer appId, String token, String senceKey) {

        //日期
//        DateTime.now().dayOfMonth()

        //获取用户ID
        Integer userId = this.getUserId(appId, token);

        //根据当前日期判断用户是否已赠送令牌
        String beginTime = DateTime.now().withTime(0,0,0,0).toString("yyyy-MM-dd HH:mm:ss");
        String endTime = DateTime.now().withTime(23,59,59,999).toString("yyyy-MM-dd HH:mm:ss");

        Example userIntegralsQuery = new Example(UserIntegrals.class);
        userIntegralsQuery.and().andBetween("createTime",beginTime,endTime).andEqualTo("userId",userId).andEqualTo("appId", appId).andEqualTo("sceneKey",senceKey);


        CommonDto<BigDecimal> limitQuanlity = this.checkMaxQuanlity(appId);
        if (limitQuanlity.getData().compareTo(new BigDecimal(0))< 0){
            return new CommonDto<>("MaxLimited","success", 200);
        }

        CommonDto<BigDecimal> receiveQuantity = this.checkReceiveQuantity(appId, token);
        if (receiveQuantity.getData().compareTo(new BigDecimal(0))<= 0){
            return new CommonDto<>("DayLimited","success", 200);
        }

        List<UserIntegrals> userIntegralsList = userIntegralsMapper.selectByExample(userIntegralsQuery);
        if (null != userIntegralsList && userIntegralsList.size()>0){
            return new CommonDto<>("Completed","success", 200);
        }else{
            MetaObtainIntegral queryObtainIntegral = new MetaObtainIntegral();
            queryObtainIntegral.setSceneKey(senceKey);
            queryObtainIntegral.setUserLevel(0); // 0 代表所有的会员级别

            MetaObtainIntegral obtainIntegral = metaObtainIntegralMapper.selectOne(queryObtainIntegral);


            addUserIntegralsLog(appId, senceKey, userId, obtainIntegral.getIntegral(), obtainIntegralPeriod,true, new BigDecimal(1), 1);



            return new CommonDto<>("Presented","success", 200);

        }



//        return null;
    }

    /**
     *  @param appId 系统ID
     * @param senceKey 场景
     * @param userId 用户ID
     * @param obtainIntegral 积分/令牌
     * @param obtainIntegralPeriod  有效期
     * @param flag 标志，是否计入令牌领取限制统计
     * @param plusOrMinus 标志，正的/负的
     * @param currency 币种，人民币/令牌
     */
    @Override
    public void addUserIntegralsLog(Integer appId, String senceKey, Integer userId, BigDecimal obtainIntegral, Integer obtainIntegralPeriod, Boolean flag, BigDecimal plusOrMinus, Integer currency) {
        try {
            BigDecimal obtainIntegralCopy = obtainIntegral;
            if (plusOrMinus.compareTo(new BigDecimal(0))>=0) {
                UserIntegrals addUserIntegrals = new UserIntegrals();
                addUserIntegrals.setEndTime(DateTime.now().withTime(23, 59, 59, 999).withFieldAdded(DurationFieldType.days(), obtainIntegralPeriod).toDate());
                addUserIntegrals.setBeginTime(DateTime.now().withTime(0, 0, 0, 0).toDate());
                addUserIntegrals.setCreateTime(DateTime.now().toDate());
                addUserIntegrals.setSceneKey(senceKey);
                addUserIntegrals.setIntegralNum(obtainIntegralCopy);
                addUserIntegrals.setUserId(userId);
                addUserIntegrals.setAppId(appId);
                addUserIntegrals.setCurrency(currency);
                userIntegralsMapper.insert(addUserIntegrals);
            }else{
                UserIntegrals query = new UserIntegrals();
                query.setAppId(appId);
                query.setUserId(userId);
                List<UserIntegrals> userIntegralsList = userIntegralsMapper.select(query);
                BigDecimal temp = null;
                for (UserIntegrals userIntegrals : userIntegralsList){
                    if (null != userIntegrals.getConsumeNum()) {
                        temp = userIntegrals.getIntegralNum().add(userIntegrals.getConsumeNum());
                    }else {
                        temp = userIntegrals.getIntegralNum();
                        userIntegrals.setConsumeNum(new BigDecimal(0));
                    }

                    if (temp.compareTo(new BigDecimal(0)) > 0  ){
                        if (temp.compareTo(obtainIntegralCopy) >= 0){
                            userIntegrals.setConsumeNum(userIntegrals.getConsumeNum().add(obtainIntegralCopy.multiply(new BigDecimal(-1))));
                            obtainIntegralCopy = new BigDecimal(0);
                        }else{
                            userIntegrals.setConsumeNum(userIntegrals.getIntegralNum().multiply(new BigDecimal(-1)));
                            obtainIntegralCopy = obtainIntegralCopy.subtract(temp);
                        }
                        userIntegralsMapper.updateByPrimaryKey(userIntegrals);
                    }

                    if (obtainIntegralCopy.compareTo(new BigDecimal(0)) == 0){
                        break;
                    }
                }
            }

            UserIntegralConsume addUserIntegralConsume = new UserIntegralConsume();
            addUserIntegralConsume.setSceneKey(senceKey);
            addUserIntegralConsume.setUserId(userId);
            addUserIntegralConsume.setBeginTime(DateTime.now().withTime(0,0,0,0).toDate());
            addUserIntegralConsume.setEndTime(DateTime.now().withTime(23,59,59,999).withFieldAdded(DurationFieldType.days(),obtainIntegralPeriod).toDate());
            addUserIntegralConsume.setCostNum(obtainIntegral.multiply(plusOrMinus));
            addUserIntegralConsume.setCreateTime(DateTime.now().toDate());
            addUserIntegralConsume.setAppId(appId);
            addUserIntegralConsume.setCurrency(currency);
            userIntegralConsumeMapper.insert(addUserIntegralConsume);

            if (flag) {
                RedEnvelopeLimit redEnvelopeLimit = this.getRedEnvelopeLimit(appId, "MAX_LIMIT");

                redEnvelopeLimit.setGrantValue(redEnvelopeLimit.getGrantValue().add(obtainIntegral));

                redEnvelopeLimitMapper.updateByPrimaryKey(redEnvelopeLimit);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public CommonDto<String> createRedEnvelope(Integer appId, RedEnvelopeDto redEnvelopeDto) {

        if (null == redEnvelopeDto.getRedEnvelopeType()){
            return new CommonDto<>("","红包类型不可为空",200);
        }else if (redEnvelopeDto.getRedEnvelopeType() == 0) {
            if (redEnvelopeDto.getAmount().compareTo(new BigDecimal(0)) <= 0) {
                return new CommonDto<>("", "红包金额必须大于0", 200);
            }
        }else if (redEnvelopeDto.getRedEnvelopeType() == 1){
            if (redEnvelopeDto.getTotalAmount().compareTo(new BigDecimal(0)) <= 0) {
                return new CommonDto<>("", "红包总金额必须大于0", 200);
            }
        }

        if(redEnvelopeDto.getQuantity()<=0){
            return new CommonDto<>("","红包数量必须大于0",200);
        }

        RedEnvelope redEnvelope = new RedEnvelope();
        redEnvelope.setAmount(redEnvelopeDto.getAmount());
        redEnvelope.setTotalAmount(redEnvelopeDto.getTotalAmount());
        redEnvelope.setAppId(appId);
        redEnvelope.setCreateTime(DateTime.now().toDate());
        redEnvelope.setQuantity(redEnvelopeDto.getQuantity());
        redEnvelope.setStatus(0);
        redEnvelope.setReceiveQuantity(0);
        redEnvelope.setReceiveAmount(new BigDecimal(0));
        redEnvelope.setToken(redEnvelopeDto.getToken());
        redEnvelope.setUnionKey(MD5Util.md5Encode(DateTime.now().millisOfDay().getAsString(),""));
        redEnvelope.setMessage(redEnvelopeDto.getMessage());
        redEnvelope.setRedEnvelopeType(redEnvelopeDto.getRedEnvelopeType());
        redEnvelope.setDescription(redEnvelopeDto.getDescription());
        redEnvelope.setCurrency(redEnvelopeDto.getCurrency());
        redEnvelopeMapper.insert(redEnvelope);

        Users users = this.getUserInfo(appId, redEnvelopeDto.getToken());
        if (!redEnvelopeDto.getDescription().equals("INVITATIONED")) {
            this.addUserIntegralsLog(appId, "SEND_ANGEL_TOKEN ", users.getId(), redEnvelope.getTotalAmount(), obtainIntegralPeriod, false, new BigDecimal(-1), redEnvelope.getCurrency());
        }
        return new CommonDto<>(redEnvelope.getUnionKey(),"succcess", 200);
    }

    @Override
    public CommonDto<BigDecimal> checkMaxQuanlity(Integer appId) {

        RedEnvelopeLimit redEnvelopeLimit = getRedEnvelopeLimit(appId, "MAX_LIMIT");

        BigDecimal limit = redEnvelopeLimit.getLimitValue().subtract(redEnvelopeLimit.getGrantValue());

        return new CommonDto<>(limit,"success",200);
    }


    public RedEnvelopeLimit getRedEnvelopeLimit(Integer appId, String max_limit) {
        RedEnvelopeLimit queryRedEnvelopeLimit = new RedEnvelopeLimit();
        queryRedEnvelopeLimit.setAppId(appId);
        queryRedEnvelopeLimit.setKey(max_limit);
        return redEnvelopeLimitMapper.selectOne(queryRedEnvelopeLimit);
    }

    @Override
    public CommonDto<BigDecimal> checkReceiveQuantity(Integer appId, String token) {

        //根据当前日期判断用户是否已赠送令牌
        String beginTime = DateTime.now().withTime(0,0,0,0).toString("yyyy-MM-dd HH:mm:ss");
        String endTime = DateTime.now().withTime(23,59,59,999).toString("yyyy-MM-dd HH:mm:ss");

        //获取用户ID
        Integer userId = this.getUserId(appId, token);

        Example userIntegralsQuery = new Example(UserIntegrals.class);
        userIntegralsQuery.and().andBetween("createTime",beginTime,endTime).andEqualTo("userId",userId).andEqualTo("appId", appId);

        List<UserIntegrals> userIntegralsList = userIntegralsMapper.selectByExample(userIntegralsQuery);

        AtomicReference<BigDecimal> quanlity = new AtomicReference<>(new BigDecimal("0"));

        userIntegralsList.forEach(item -> {
            quanlity.set(quanlity.get().add(item.getIntegralNum()));
        });

        RedEnvelopeLimit redEnvelopeLimit = getRedEnvelopeLimit(appId, "DAY_LIMIT");
        BigDecimal limit = redEnvelopeLimit.getLimitValue().subtract(quanlity.get());


        return new CommonDto<>(limit,"success",200);
    }


    @Override
    public CommonDto<BigDecimal> checkRemainingBalance(Integer appId, String token, Integer currency) {

        //获取用户ID
        Integer userId = this.getUserId(appId, token);

        if (null == userId){
            return new CommonDto<>(new BigDecimal(0), "未找到用户", 200);
        }
        BigDecimal totalAmount = userIntegralConsumeMapper.selectSumIntegralConsume(appId, userId, currency);

        if (null == totalAmount){
            return new CommonDto<>(new BigDecimal(0), "success", 200);
        }else {
            return new CommonDto<>(totalAmount, "success", 200);
        }
    }


    @Transactional
    @Override
    public CommonDto<RedEnvelopeResDto> receiveRedEnvelope(Integer appId, String unionId, String token, String unionKey) {

        CommonDto<RedEnvelopeResDto> result = new CommonDto<>();
        RedEnvelopeResDto redEnvelopeResDto = new RedEnvelopeResDto();

        try {
            Users users = null;
            if(StringUtil.isEmpty(token)){
                return  new CommonDto<>(null, "用户不存在", 200);
            }else {
               users = this.getUserInfo(appId, token);
                if (StringUtils.isEmpty(users.getActualName())) {
                    UsersWeixin query = new UsersWeixin();
                    query.setUserId(users.getId());
                    UsersWeixin usersWeixin = this.usersWeixinMapper.selectOne(query);
                    if (StringUtils.isEmpty(usersWeixin.getNickName())){

                        return new CommonDto<>(null, "用户不存在", 200);
                    }
                }
            }
            RedEnvelope queryRedEnvelope = new RedEnvelope();
            queryRedEnvelope.setUnionKey(unionId);
            RedEnvelope redEnvelope = redEnvelopeMapper.selectOne(queryRedEnvelope);


             users = this.getUserInfo(appId,redEnvelope.getToken());
            if (null == users){
                return  new CommonDto<>(null, "用户不存在", 200);
            }
            if (StringUtil.isNotEmpty(users.getHeadpicReal())) {
                redEnvelopeResDto.setHeadPic(users.getHeadpicReal());
            }else{
                redEnvelopeResDto.setHeadPic(users.getHeadpic());
            }
            if (StringUtil.isNotEmpty(users.getActualName())) {
                redEnvelopeResDto.setNeckName(users.getActualName());
            }else {
                UsersWeixin queryUserWeixin = new UsersWeixin();
                queryUserWeixin.setUserId(users.getId());

                UsersWeixin usersWeixin = usersWeixinMapper.selectOne(queryUserWeixin);
                if (null != usersWeixin) {
                    redEnvelopeResDto.setNeckName(usersWeixin.getNickName());
                }
            }
            redEnvelopeResDto.setCompanyDuties(users.getCompanyDuties());
            redEnvelopeResDto.setCompanyName(users.getCompanyName());

            redEnvelopeResDto.setDescription(redEnvelope.getDescription());
            redEnvelopeResDto.setQuantity(redEnvelope.getReceiveQuantity());
            redEnvelopeResDto.setTotalQuantity(redEnvelope.getQuantity());
            redEnvelopeResDto.setMessage(redEnvelope.getMessage());
            redEnvelopeResDto.setRedEnvelopeID(redEnvelope.getUnionKey());
            redEnvelopeResDto.setToken(redEnvelope.getToken());
            if (null != redEnvelope.getCurrency()) {
                if (redEnvelope.getCurrency() == 0) {
                    redEnvelopeResDto.setCurrency("元");
                } else if (redEnvelope.getCurrency() == 1) {
                    redEnvelopeResDto.setCurrency("令牌");
                }
            }


            if (overTime24HourHandler(appId,redEnvelope)){
                    redEnvelopeResDto.setStatus("OverTimed24");
                    redEnvelopeResDto.setMessage(redEnvelope.getMessage());
            }else if (null != redEnvelope.getDescription() && redEnvelope.getDescription().equals("INVITATIONED")){

                Users newUser = this.getUserInfo(appId,token);

                if (null != newUser&& null != newUser.getPhonenumber() && newUser.getPhonenumber().length() > 0){
                    redEnvelopeResDto.setStatus("NewCompleted");
                    redEnvelopeResDto.setMessage(redEnvelope.getMessage());
                }else if (redEnvelopeMapper.checkNewPeopleByToken(token) == 1){
                    redEnvelopeResDto.setStatus("NewCompleted");
                    redEnvelopeResDto.setMessage(redEnvelope.getMessage());
                }else{
                    recived(appId, unionId, token, redEnvelopeResDto, redEnvelope);
                }

            }else {
                recived(appId, unionId, token, redEnvelopeResDto, redEnvelope);
            }

//            queryRedEnvelopeLog = new RedEnvelopeLog();
//            queryRedEnvelopeLog.setRedEnvelopeId(redEnvelope.getId());

            Example queryRedEnvelopeLogExample = new Example(RedEnvelopeLog.class);
            queryRedEnvelopeLogExample.and().andEqualTo("redEnvelopeId",redEnvelope.getId());
            queryRedEnvelopeLogExample.setOrderByClause("create_time desc");
            List<RedEnvelopeLog> redEnvelopeLogs = redEnvelopeLogMapper.selectByExample(queryRedEnvelopeLogExample);

            List<RedEnvelopeLogDto> redEnvelopeLogDtos = new ArrayList<>();

            redEnvelopeLogs.forEach(item -> {
                RedEnvelopeLogDto redEnvelopeLogDto = new RedEnvelopeLogDto();
                try {
                    redEnvelopeLogDto.setAmount(item.getAmount());
                    redEnvelopeLogDto.setCreateTime(item.getCreateTime());
                    redEnvelopeLogDto.setToken(item.getToken());
                    handlerCurrency(redEnvelope, redEnvelopeLogDto);
                    Users temp = this.getUserInfo(appId,item.getToken());
                    if (null != temp) {
                        if (StringUtil.isNotEmpty(temp.getHeadpicReal())) {
                            redEnvelopeLogDto.setHeadPic(temp.getHeadpicReal());
                        } else {
                            redEnvelopeLogDto.setHeadPic(temp.getHeadpic());
                        }
                        if (StringUtil.isNotEmpty(temp.getActualName())) {
                            redEnvelopeLogDto.setName(temp.getActualName());
                        } else {
                            UsersWeixin query = new UsersWeixin();
                            query.setUserId(temp.getId());
                            UsersWeixin usersWeixin = this.usersWeixinMapper.selectOne(query);
                            if (null != usersWeixin) {
                                redEnvelopeLogDto.setName(usersWeixin.getNickName());
                            }
                        }
                        redEnvelopeLogDto.setCompanyDuties(temp.getCompanyDuties());
                        redEnvelopeLogDto.setCompanyName(temp.getCompanyName());
                    }
                } catch (Exception e) {
                   this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
                }
                redEnvelopeLogDtos.add(redEnvelopeLogDto);
            });

            redEnvelopeResDto.setRedEnvelopeLogs(redEnvelopeLogDtos);
            result.setData(redEnvelopeResDto);
            result.setMessage("success");
            result.setStatus(200);
        } catch (Exception e) {
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            throw e;
        }

        return result;
    }

    private boolean overTime24HourHandler(Integer appId, RedEnvelope redEnvelope) {

        Date endDate = new DateTime(redEnvelope.getCreateTime()).plusDays(1).toDate();
        if (endDate.before(DateTime.now().toDate())) {

            //超24时，不可领取红包，退回剩余金额
            BigDecimal residueMoney = redEnvelope.getTotalAmount().subtract(redEnvelope.getReceiveAmount());
            Users users = this.getUserInfo(appId, redEnvelope.getToken());
            if (null != users) {
                Integer currency = redEnvelope.getCurrency();
                if (null == currency){
                    currency = 1;
                }
                if (!redEnvelope.getDescription().equals("INVITATIONED")) {
                    this.addUserIntegralsLog(appId, "RED_ENVELOPE_RETURN", users.getId(), residueMoney, obtainIntegralPeriod, false, new BigDecimal(1), currency);
                }
                redEnvelope.setStatus(3);//红包状态过期
                this.redEnvelopeMapper.updateByPrimaryKey(redEnvelope);
            }
            return true;
        }else{
            return false;
        }
    }

    public void handlerCurrency(RedEnvelope redEnvelope, RedEnvelopeLogDto redEnvelopeLogDto) {
        if (null != redEnvelope.getCurrency()) {
            if (redEnvelope.getCurrency() == 0) {
                redEnvelopeLogDto.setCurrency("元");
            } else if (redEnvelope.getCurrency() == 1) {
                redEnvelopeLogDto.setCurrency("令牌");
            }
        }
    }

    public void recived(Integer appId, String unionId, String token, RedEnvelopeResDto redEnvelopeResDto, RedEnvelope redEnvelope) {
        RedEnvelopeLog queryRedEnvelopeLog = new RedEnvelopeLog();
        queryRedEnvelopeLog.setAppId(appId);
        queryRedEnvelopeLog.setToken(token);


        queryRedEnvelopeLog.setRedEnvelopeId(redEnvelope.getId());
        RedEnvelopeLog checkRedEnvelopeLog = redEnvelopeLogMapper.selectOne(queryRedEnvelopeLog);
        if (null != checkRedEnvelopeLog) {

            redEnvelopeResDto.setStatus("Completed");
            redEnvelopeResDto.setMessage(redEnvelope.getMessage());
            redEnvelopeResDto.setAmount(checkRedEnvelopeLog.getAmount());

        } else {


            //尝试3次获取红包
            for (int i = 0; i < 3; i++) {


                if (redEnvelope.getQuantity() == redEnvelope.getReceiveQuantity()) {

                    if (StringUtil.isEmpty(redEnvelopeResDto.getMessage())) {
                        redEnvelopeResDto.setMessage(redEnvelope.getMessage());
                    }
                    redEnvelopeResDto.setStatus("Finished");
                    break;
                } else {
                    RedEnvelopeLog redEnvelopeLog = new RedEnvelopeLog();
                    BigDecimal reciveAmount = null;
                    if (redEnvelope.getRedEnvelopeType() == 0) {
                        reciveAmount = redEnvelope.getAmount();
                        redEnvelopeLog.setAmount(reciveAmount);
                        redEnvelope.setReceiveAmount(redEnvelope.getReceiveAmount().add(redEnvelope.getAmount()));
                        redEnvelopeResDto.setAmount(reciveAmount);
                    } else if (redEnvelope.getRedEnvelopeType() == 1) {
                        reciveAmount = randomAmount(redEnvelope.getQuantity(), redEnvelope.getReceiveQuantity(), redEnvelope.getTotalAmount(), redEnvelope.getReceiveAmount(), appId);
                        redEnvelopeLog.setAmount(reciveAmount);
                        redEnvelope.setReceiveAmount(redEnvelope.getReceiveAmount().add(reciveAmount));
                        redEnvelopeResDto.setAmount(reciveAmount);
                    }
                    Example example = new Example(RedEnvelope.class);
                    example.and().andEqualTo("appId", appId).andEqualTo("unionKey", unionId).andEqualTo("receiveQuantity", redEnvelope.getReceiveQuantity());
                    redEnvelope.setReceiveQuantity(redEnvelope.getReceiveQuantity() + 1);
                    redEnvelopeResDto.setQuantity(redEnvelope.getReceiveQuantity());


                    if (redEnvelopeMapper.updateByExample(redEnvelope, example) > 0) {

                        redEnvelopeResDto.setStatus("Received");


                        redEnvelopeLog.setAppId(appId);
                        redEnvelopeLog.setCreateTime(DateTime.now().toDate());
                        redEnvelopeLog.setRedEnvelopeId(redEnvelope.getId());
                        redEnvelopeLog.setToken(token);
                        redEnvelopeLog.setFromToken(redEnvelope.getToken());
                        redEnvelopeLog.setUnionKey(unionId);
                        redEnvelopeLog.setCurrency(redEnvelope.getCurrency());
                        redEnvelopeLog.setPayStatus(0);

                        redEnvelopeLogMapper.insert(redEnvelopeLog);
                        Users reciveUser = this.getUserInfo(appId, token);
                        if (redEnvelope.getCurrency() == 0) { //人民币没有限制，令牌每天有最高领取限制
                            this.addUserIntegralsLog(appId, "GET_ANGEL_TOKEN", reciveUser.getId(), reciveAmount, obtainIntegralPeriod, false, new BigDecimal(1), redEnvelope.getCurrency());
                        }else{
                            this.addUserIntegralsLog(appId, "GET_ANGEL_TOKEN", reciveUser.getId(), reciveAmount, obtainIntegralPeriod, true, new BigDecimal(1), redEnvelope.getCurrency());

                        }

                        if (null != redEnvelope.getCurrency() && redEnvelope.getCurrency()==0) {
                            UsersWeixin query = new UsersWeixin();
                            query.setUserId(reciveUser.getId());
                            UsersWeixin userToken = usersWeixinMapper.selectOne(query);
                            WxEntPayRequest wxEntPayRequest = new WxEntPayRequest();
                            wxEntPayRequest.setAmount(redEnvelopeLog.getAmount().multiply(new BigDecimal(100)).intValue());
                            wxEntPayRequest.setCheckName("NO_CHECK");
                            wxEntPayRequest.setOpenid(userToken.getOpenid());
                            wxEntPayRequest.setDescription("蓝海巨浪资本");
                            wxEntPayRequest.setPartnerTradeNo(getPartnerTradeNo());
                            wxEntPayRequest.setSpbillCreateIp(spbillCreateIp);
                            try {
                                WxEntPayResult wxEntPayResult = payService.entPay(wxEntPayRequest);
                                if (null != wxEntPayResult){
                                    redEnvelopeLog.setPayStatus(1);
                                    redEnvelopeLog.setPayTime(wxEntPayResult.getPaymentTime());
                                    redEnvelopeLog.setPayTradeNo(wxEntPayResult.getPartnerTradeNo());
                                    redEnvelopeLogMapper.updateByPrimaryKey(redEnvelopeLog);
                                }
                            } catch (WxPayException e) {
                                this.LOGGER.error(e.getXmlString());
                            }
                        }

                       break;
                    }
                }
            }
        }
    }

    @Override
    public CommonDto<Map<String, BigDecimal>> getInvitationedLimit(Integer appId, String token) {

        RedEnvelopeLimit redEnvelopeLimitAmount = this.getRedEnvelopeLimit(appId, "INVITATION_AMOUNT");
        RedEnvelopeLimit redEnvelopeLimitQuanlity = this.getRedEnvelopeLimit(appId, "INVITATION_QUANLITY");
        Map<String, BigDecimal> limitMap = new HashMap<>();
        limitMap.put("invitationAmount",redEnvelopeLimitAmount.getLimitValue());
        limitMap.put("invitationQuanlity",redEnvelopeLimitQuanlity.getLimitValue());



        return new CommonDto<>(limitMap,"success", 200);
    }

    @Transactional
    @Override
    public CommonDto<String> resolveWechatGroupId(Integer appId, String redEnvelopeId, String token, RedEnvelopeGroupDto groupDto) {

        UserToken userToken = new UserToken();
        userToken.setToken(token);

        userToken = userTokenMapper.selectOne(userToken);
        Users users = usersMapper.selectByPrimaryKey(userToken.getUserId());


        //sessionkey加前缀
        String redisKeyId = "sessionkey:" + users.getId();
        //取到sessionKey
        String sessionKey = sessionKeyService.getSessionKey(redisKeyId);

        OpenGIdJsonDto openGIdJsonDto =  WxMaGsonBuilder.create().fromJson(WxMaCryptUtils.decrypt(sessionKey, groupDto.getEncryptedData(), groupDto.getIv()),OpenGIdJsonDto.class);

        RedEnvelope recordRedEnvelop = new RedEnvelope();
        recordRedEnvelop.setUnionKey(redEnvelopeId);

        RedEnvelopeWechatgroup redEnvelopeWechatgroupRecord = new RedEnvelopeWechatgroup();
        redEnvelopeWechatgroupRecord.setUnionKey(groupDto.getUnionKey());

        RedEnvelopeWechatgroup redEnvelopeWechatgroup = redEnvelopeWechatgroupMapper.selectOne(redEnvelopeWechatgroupRecord);
        redEnvelopeWechatgroup.setWechatGroupId(openGIdJsonDto.getOpenGId());

        redEnvelopeWechatgroupMapper.updateByPrimaryKey(redEnvelopeWechatgroup);


        return new CommonDto<>("ok","success", 200);
    }

    @Transactional
    @Override
    public CommonDto<String> getRedEnvelopeWechatGroupKey(Integer appId, String redEnvelopeId, String token) {

        RedEnvelope redEnvelopeRecord = new RedEnvelope();
        redEnvelopeRecord.setUnionKey(redEnvelopeId);

        RedEnvelope redEnvelope = redEnvelopeMapper.selectOne(redEnvelopeRecord);

        RedEnvelopeWechatgroup redEnvelopeWechatgroup = new RedEnvelopeWechatgroup();
        redEnvelopeWechatgroup.setRedEnvelopeId(redEnvelope.getId());
        redEnvelopeWechatgroup.setToken(token);
        redEnvelopeWechatgroup.setAppId(appId);
        redEnvelopeWechatgroup.setCreateTime(DateTime.now().toDate());
        String unionKey = MD5Util.md5Encode(DateTime.now().millisOfDay().getAsString(),"");
        redEnvelopeWechatgroup.setUnionKey(unionKey);

        redEnvelopeWechatgroupMapper.insert(redEnvelopeWechatgroup);

        return new CommonDto<>(unionKey,"success", 200);
    }

    @Override
    public CommonDto<List<RedEnvelopeResDto>> queryRedEnvelopeAllByToken(Integer appId, String token, Integer pageNo, Integer pageSize) {

//        Set<String> friendToken = getFriendToken(token);
        Set<String> tempTokens = null;
        Set<String> tokens = null;
//        for (String temp : friendToken) {
            tokens = getRecivedTokens(token);


            tempTokens = new ConcurrentSkipListSet<>();
            for (String tempToken : tokens) {
                tempTokens.addAll(getRecivedTokens(tempToken));
            }
//        }
        tokens = tempTokens;

        if (null == tokens || tokens.size() <= 0){
            return  new CommonDto<>(null,"success", 200);
        }
        RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize,pageSize);
        Example query = new Example(RedEnvelope.class);
        query.and().andIn("token",tokens);
        query.setOrderByClause("create_time desc");
        List<RedEnvelope> redEnvelopes =   redEnvelopeMapper.selectByExampleAndRowBounds(query,rowBounds);



        List<RedEnvelopeResDto> redEnvelopeResDtos = new ArrayList<>();
        redEnvelopes.forEach(redEnvelope -> {
            RedEnvelopeResDto redEnvelopeResDto = new RedEnvelopeResDto();
            redEnvelopeResDto.setToken(redEnvelope.getToken());
            redEnvelopeResDto.setRedEnvelopeID(redEnvelope.getUnionKey());
            redEnvelopeResDto.setDescription(redEnvelope.getDescription());
            redEnvelopeResDto.setQuantity(redEnvelope.getReceiveQuantity());
            redEnvelopeResDto.setTotalQuantity(redEnvelope.getQuantity());
            redEnvelopeResDto.setMessage(redEnvelope.getMessage());
            redEnvelopeResDto.setCreateTime(redEnvelope.getCreateTime());
            redEnvelopeResDto.setAmount(redEnvelope.getAmount());
            redEnvelopeResDto.setTotalAmount(redEnvelope.getTotalAmount());

            if (null != redEnvelope.getCurrency()) {
                if (redEnvelope.getCurrency() == 0) {
                    redEnvelopeResDto.setCurrency("元");
                } else if (redEnvelope.getCurrency() == 1) {
                    redEnvelopeResDto.setCurrency("令牌");
                }
            }
            if (overTime24HourHandler(appId,redEnvelope)) {
                redEnvelopeResDto.setStatus("OverTimed24");
            }else if (redEnvelope.getReceiveQuantity() == redEnvelope.getQuantity()) {
                redEnvelopeResDto.setStatus("Finished");
            }else{
                RedEnvelopeLog redEnvelopeLogQuery = new RedEnvelopeLog();
                redEnvelopeLogQuery.setToken(token);
                redEnvelopeLogQuery.setRedEnvelopeId(redEnvelope.getId());
                if (redEnvelopeLogMapper.selectCount(redEnvelopeLogQuery)==1){
                    redEnvelopeResDto.setStatus("Received");
                }else{
                    redEnvelopeResDto.setStatus("Unreceived");
                }
            }
            editUserInfo(appId, redEnvelope, redEnvelopeResDto);


            redEnvelopeResDtos.add(redEnvelopeResDto);
        });

        return new CommonDto<>(redEnvelopeResDtos, "success", 200);
    }

    private Set<String> getFriendToken(String token) {
        RedEnvelopeLog redEnvelopeLogRecord = new RedEnvelopeLog();
        redEnvelopeLogRecord.setToken(token);

        List<RedEnvelopeLog> redEnvelopeLogs = redEnvelopeLogMapper.select(redEnvelopeLogRecord);
        Set<String> tokens = new HashSet<>();
        List<RedEnvelopeLog> temp = null;
        for (RedEnvelopeLog redEnvelopeLog : redEnvelopeLogs){
            redEnvelopeLogRecord = new RedEnvelopeLog();
            redEnvelopeLogRecord.setFromToken(redEnvelopeLog.getFromToken());
            temp = redEnvelopeLogMapper.select(redEnvelopeLogRecord);
            tokens.add(redEnvelopeLog.getFromToken());
            for (RedEnvelopeLog rel : temp) {
                tokens.add(rel.getToken());
            }
        }
        return tokens;

    }

    public Set<String> getRecivedTokens(String token) {

        RedEnvelopeLog redEnvelopeLogRecord = new RedEnvelopeLog();
        redEnvelopeLogRecord.setFromToken(token);

        List<RedEnvelopeLog> redEnvelopeLogs = redEnvelopeLogMapper.select(redEnvelopeLogRecord);
        Set<String> tokens = new HashSet<>();
        for (RedEnvelopeLog redEnvelopeLog : redEnvelopeLogs){
            tokens.add(redEnvelopeLog.getToken());
        }
        return tokens;
    }

    @Override
    public CommonDto<RedEnvelopeResDto> getRedEnvelopeInfo(Integer appId, String unionId, String token) {

        RedEnvelope redEnvelopeQuery = new RedEnvelope();
        redEnvelopeQuery.setToken(token);
        redEnvelopeQuery.setUnionKey(unionId);
        redEnvelopeQuery.setAppId(appId);

        RedEnvelope redEnvelope = redEnvelopeMapper.selectOne(redEnvelopeQuery);
        RedEnvelopeResDto redEnvelopeResDto = new RedEnvelopeResDto();
        redEnvelopeResDto.setDescription(redEnvelope.getDescription());
        redEnvelopeResDto.setQuantity(redEnvelope.getReceiveQuantity());
        redEnvelopeResDto.setTotalQuantity(redEnvelope.getQuantity());
        redEnvelopeResDto.setMessage(redEnvelope.getMessage());
        redEnvelopeResDto.setCreateTime(redEnvelope.getCreateTime());
        redEnvelopeResDto.setAmount(redEnvelope.getAmount());
        redEnvelopeResDto.setTotalAmount(redEnvelope.getTotalAmount());
        redEnvelopeResDto.setRedEnvelopeID(redEnvelope.getUnionKey());
        redEnvelopeResDto.setToken(redEnvelope.getToken());
        if (null != redEnvelope.getCurrency()) {
            if (redEnvelope.getCurrency() == 0){
                redEnvelopeResDto.setCurrency("元");
            }else if (redEnvelope.getCurrency() == 1){
                redEnvelopeResDto.setCurrency("令牌");
            }
        }

        editUserInfo(appId, redEnvelope, redEnvelopeResDto);


        return new CommonDto<>(redEnvelopeResDto, "success", 200);
    }

    public void editUserInfo(Integer appId, RedEnvelope redEnvelope, RedEnvelopeResDto redEnvelopeResDto) {
        Users temp = this.getUserInfo(appId,redEnvelope.getToken());
        if (null != temp) {
            if (StringUtil.isNotEmpty(temp.getHeadpicReal())) {
                redEnvelopeResDto.setHeadPic(temp.getHeadpicReal());
            } else {
                redEnvelopeResDto.setHeadPic(temp.getHeadpic());
            }
            if (StringUtil.isNotEmpty(temp.getActualName())) {
                redEnvelopeResDto.setNeckName(temp.getActualName());
            } else {
                UsersWeixin queryUsersWeixin = new UsersWeixin();
                queryUsersWeixin.setUserId(temp.getId());
                UsersWeixin usersWeixin = this.usersWeixinMapper.selectOne(queryUsersWeixin);
                if (null != usersWeixin) {
                    redEnvelopeResDto.setNeckName(usersWeixin.getNickName());
                }
            }
            redEnvelopeResDto.setCompanyDuties(temp.getCompanyDuties());
            redEnvelopeResDto.setCompanyName(temp.getCompanyName());
        }
    }

    @Override
    public CommonDto<Map<String, Integer>> getStatisticesRedEnvelope() {

        Map<String, Integer> map = new HashMap<>();

        map.put("peoples", 100+redEnvelopeMapper.getRedEnvelopePeopleNums());
        map.put("contacts", 200+redEnvelopeMapper.getRecivedRedEnvelopePeopleNums());
        map.put("sendRedNum", 300+redEnvelopeMapper.getRedEnvelopeNums());

        return new CommonDto<>(map, "success", 200);
    }

    private BigDecimal randomAmount(Integer quantity, Integer receiveQuantity, BigDecimal totalAmount, BigDecimal receiveAmount, Integer appId) {

        RedEnvelopeLimit redEnvelopeLimit = this.getRedEnvelopeLimit(appId, "RED_ENVELOPE_LIMIT");
        if (totalAmount.compareTo(redEnvelopeLimit.getLimitValue().multiply(new BigDecimal(quantity))) == 0){
            return redEnvelopeLimit.getLimitValue();
        }
        BigDecimal minValue = new BigDecimal(0.01);
        if (totalAmount.compareTo(minValue.multiply(new BigDecimal(quantity))) == 0){
            return minValue;
        }
            if (quantity - receiveQuantity == 1){
                return totalAmount.subtract(receiveAmount);
            }else {
                return totalAmount.subtract(receiveAmount).divide(new BigDecimal(quantity - receiveQuantity),2).multiply(new BigDecimal(Math.random())).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            }
    }


    public Users getUserInfo(Integer appId,String token){
        UserToken queryUserToken = new UserToken();
        queryUserToken.setToken(token);
        UserToken userToken = userTokenMapper.selectOne(queryUserToken);

        Users users = null;
        if (null != userToken) {
            users = usersMapper.selectByPrimaryKey(userToken.getUserId());
        }

        return users;
    }

    public static void main(String[] args) {
//        System.out.println(DateTime.now().withTime(0,0,0,0).withFieldAdded(DurationFieldType.days(),30).toString("yyyy-MM-dd HH:mm:ss"));
//        System.out.println(DateTime.now().withTime(23,59,59,999).toString("yyyy-MM-dd HH:mm:ss"));
//        Random random = new Random();
//        System.out.println(String.valueOf());

        System.out.println(DateTime.now().millisOfDay().getAsString().length());

    }

    public String getPartnerTradeNo() {
         String timestamp = DateTime.now().millisOfDay().getAsShortText();
         StringBuffer stringBuffer = new StringBuffer(MD5Util.md5Encode(timestamp,"").substring(0,24)).append(timestamp);
        return stringBuffer.toString();
    }
}
