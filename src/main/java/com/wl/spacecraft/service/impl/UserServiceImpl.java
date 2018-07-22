package com.wl.spacecraft.service.impl;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.commondto.GameConfigCommonOutputDto;
import com.wl.spacecraft.dto.commondto.UserInfoCommonOutputDto;
import com.wl.spacecraft.dto.requestdto.*;
import com.wl.spacecraft.dto.responsedto.*;
import com.wl.spacecraft.exception.account.AccountException;
import com.wl.spacecraft.exception.common.DataFormatException;
import com.wl.spacecraft.exception.login.MsgCodeException;
import com.wl.spacecraft.exception.login.TokenIlleaglException;
import com.wl.spacecraft.exception.login.UserNotExistException;
import com.wl.spacecraft.exception.project.OgLackException;
import com.wl.spacecraft.exception.project.ProjectException;
import com.wl.spacecraft.mapper.AppIntergralMapper;
import com.wl.spacecraft.mapper.AppUserMapper;
import com.wl.spacecraft.mapper.UserGameMapper;
import com.wl.spacecraft.model.AppIntergral;
import com.wl.spacecraft.model.AppUser;
import com.wl.spacecraft.model.UserGame;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.game.GameService;
import com.wl.spacecraft.service.user.UserService;
import com.wl.spacecraft.utils.DateUtils;
import com.wl.spacecraft.utils.MD5Util;
import com.wl.spacecraft.utils.RandomStr;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl extends GenericService implements UserService {

    // 自定义秘钥
    private static final String KEY = "SPACECRAFT";

    @Resource
    GameService gameService;

    @Autowired
    private Environment env;

    /**
     * 开始一局游戏的积分扣除个数
     */
    @Value("${coinSubtract}")
    private Integer coinSubtract;
    /**
     * 每日积分上限
     */
    @Value("${topLimit}")
    private Integer topLimit;

    @Autowired
    AppUserMapper appUserMapper;
    @Autowired
    UserGameMapper userGameMapper;
    @Autowired
    AppIntergralMapper appIntergralMapper;

    /**
     * 用户信息的校验
     * @param token
     * @return
     */
    private boolean validateUser(String phone ,String token,Date expire,String validateStr){
        if( getUserByPhone(phone) != null
                && valivdateToken(token,expire,validateStr) ){
            return true;
        }
        return false;
    }

    /**
     * 用户存在性检查
     * @param phone 用户的手机号
     * @return 用户信息
     */
    private AppUser getUserByPhone(String phone){
        //数据格式校验
        if(StringUtils.isBlank(phone)){
            throw new DataFormatException("手机号数据格式不正确");
        }

        AppUser au =new AppUser();
        au.setPhonenum(phone);
        List<AppUser> list = appUserMapper.select(au);

        //用户不存在
        if(null == list || list.size() == 0 ){
            throw new UserNotExistException("该用户不存在");
        }
        return list.get(0);
    }

    /**
     * 校验token的合法性
     * @param token
     * @return
     */
    private boolean valivdateToken(String token,Date expire,String tokenValidateStr){

        System.err.println("token校验串---》》》"+ MD5Util.md5Encode(KEY + "@" + token + "@" + expire.toString(),null ) );

        //数据格式校验
        if(StringUtils.isAnyBlank(token,tokenValidateStr)){
            throw new DataFormatException("token数据格式不正确");
        }

        if( expire.compareTo(new Date()) <0 ){
            throw new TokenIlleaglException("token过期");
        }
        //检验token的有效性
        if(! MD5Util.md5Encode(KEY + "@" + token + "@" + expire.toString(),null ).equals(tokenValidateStr) ){
            throw new TokenIlleaglException("token非法");
        }
  
        return true;
    }

    /**
     * 短信验证码的校验
     * @param msgCode 短信验证码
     * @param expire 该验证码的过期时间
     * @param msgValidateStr Md5校验串
     * @return
     */
    private boolean validateMsg(String msgCode,Date expire,String msgValidateStr){

        System.err.println("短信校验串---》》》"+ MD5Util.md5Encode(KEY + "@" + msgCode + "@" + expire.toString(),null ) );
        //数据格式校验
        if(StringUtils.isAnyBlank(msgCode,msgValidateStr)){
            throw new DataFormatException("短信验证码或者检验串的格式不正确");
        }

        if( expire.compareTo(new Date()) <0 ){
            throw new MsgCodeException("短信验证码过期");
        }

        //检验验证码输入的有效性
        if(! MD5Util.md5Encode(KEY + "@" + msgCode + "@" + expire.toString(),null ).equals(msgValidateStr) ){

            throw new MsgCodeException("短信验证码输入错误");
        }
        return true;
    }

    /**
     * 返回用户今日获取的金币上限
     * @param phone 用户手机号
     * @return
     */
    private Integer getTodayLimite(String phone){
        return userGameMapper.getLimite( phone, DateUtils.getStartTime(), DateUtils.getEndTime() );
    }
    /**
     * 获取用户的持有的金币总额
     */
    private Integer getAmountByUser(String phone){
        AppUser user = this.getUserByPhone(phone);
        return user.getAmount() ==null ? 0 : user.getAmount();
    }

    @Override
    @Transactional
    public CommonDto<LoginOutputDto> login(LoginInputDto body) {

        //首先进行验证码的有效性判断
        try{
            validateMsg(body.getMsgCode(),body.getExpire(),body.getMsgValidateStr());
        }catch(Exception e){
            throw e;
        }

        //执行token的生成策略
        String token = UUID.randomUUID().toString().replace("-", "");

        Calendar calendar = Calendar.getInstance();
//        Date time = calendar.getTime();
        //设置token失效时间
        calendar.add(Calendar.DATE , Integer.valueOf(env.getProperty("tokenExpireTime")) );
        Date expire = calendar.getTime();

        //生成token的校验串
        String tokenValidateStr = MD5Util.md5Encode(KEY + "@" + token + "@" + expire.toString(),"");

        try{//存在该用户，更新用户的token以及登录时间
            AppUser user = this.getUserByPhone(body.getPhone());
            user.setToken(token);
            user.setLastLoginTime(new Date());
            appUserMapper.updateByPrimaryKeySelective(user);

        }catch (Exception e){ //捕获到异常说明用户不存在，执行新用户的注册

            AppUser user=new AppUser();
            user.setPhonenum(body.getPhone());
            user.setToken(token);
            //新用户注册赠送金币
            user.setAmount( Integer.valueOf(env.getProperty("OgReward")) );
            user.setLastLoginTime(new Date());
            user.setCreateTime(new Date());
            appUserMapper.insertSelective(user);
        }

        CommonDto<LoginOutputDto> result =new CommonDto<>();

        LoginOutputDto output=new LoginOutputDto();

        output.setResult(true);
        output.setNote("登录成功");

        //设置用户的信息
        UserInfoCommonOutputDto user =new UserInfoCommonOutputDto();
        user.setPhone(body.getPhone());
        user.setToken(token);
        user.setAmount( this.getAmountByUser(body.getPhone()) );
        user.setExpire(expire);
        user.setLimit(this.getTodayLimite(body.getPhone()));
        user.setTokenValidateStr(tokenValidateStr);
        output.setUserData(user);

        //设置游戏元数据
        GameConfigCommonOutputDto gameData = gameService.getGameConfig().getData();
        output.setGameData(gameData);


        result.setData(output);
        result.setMessage("success");
        result.setStatus(200);

        return result;
    }

    @Override
    public Object test() {
        Integer amount = this.getTodayLimite("4");
        System.out.println(amount);

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public CommonDto<UserInfoOutputDto> getUserInfo(UserInfoInputDto body) {
        //进行用户有效性判断
        try{
            validateUser(body.getPhone(), body.getToken(),body.getExpire(),body.getTokenValidateStr());
        }catch(Exception e){
            throw e;
        }

        CommonDto<UserInfoOutputDto> result=new CommonDto<>();

        UserInfoOutputDto userInfo =new UserInfoOutputDto();

        userInfo.setLimit(this.getTodayLimite(body.getPhone()));
        userInfo.setAmount( this.getAmountByUser(body.getPhone()) );
        userInfo.setPhone(body.getPhone());

        result.setData(userInfo);
        result.setMessage("success");
        result.setStatus(200);

        return result;
    }

    @Override
    @Transactional
    public CommonDto<GameStartOutputDto> startGame(GameStartInputDto body) {

        //进行用户有效性判断
        try{
            validateUser(body.getPhone(), body.getToken(),body.getExpire(),body.getTokenValidateStr());
        }catch(Exception e){
            throw e;
        }

        //用户本日游戏积分已经达到上限，游戏玩就扣积分，所以下面的代码暂时注释
//        if(this.getTodayLimite(phone) >= topLimit ){
//            throw new ProjectException("本日赠送积分已达上限");
//        }

        //用户积分的扣除
        AppUser au = this.getUserByPhone(body.getPhone());
        if(au.getAmount() < coinSubtract ){
            throw new OgLackException("用户金币不足");
        }
        au.setAmount(au.getAmount()- coinSubtract );
        appUserMapper.updateByPrimaryKeySelective(au);
        
        CommonDto<GameStartOutputDto> result=new CommonDto<>();
        GameStartOutputDto output =new GameStartOutputDto();

        //插入用户的游戏记录
        //生成随机16位数字字母的随机游戏ID
        String random = RandomStr.randomStr( Integer.valueOf(env.getProperty("hexRandom")) );
        UserGame userGame =new UserGame();
        userGame.setPhonenum(body.getPhone());
        userGame.setToken(body.getToken());
        userGame.setGameId(random);
        userGame.setBeginTime(new Date());
        userGame.setOgConsume(coinSubtract);
        userGameMapper.insertSelective(userGame);

        output.setResult(true);
        output.setGameId(random);
        output.setPhone(body.getPhone());
        output.setAmount(au.getAmount());

        result.setData(output);
        result.setMessage("success");
        result.setStatus(200);

        return result;
    }

    @Override
    @Transactional
    public CommonDto<GameOverOutputDto> overGame(GameOverInputDto body) {
        //进行用户身份信息校验
        try{
            validateUser(body.getPhone(), body.getToken(),body.getExpire(),body.getTokenValidateStr());
        }catch(Exception e){
            throw e;
        }

        //获取用户今天已经获得的积分总数
        Integer todaySum = this.getTodayLimite(body.getPhone());

        System.err.println("***todaySum=="+todaySum+"*******");

        //超出了每日获取og的最大限制,则只能增加到最大限额
        if( todaySum+body.getScore() > topLimit){
            body.setScore( topLimit-todaySum);
        }

        CommonDto<GameOverOutputDto> result=new CommonDto<>();

        GameOverOutputDto output=new GameOverOutputDto();

        //首先进行用户游戏记录插入
        UserGame ug=new UserGame();
        ug.setGameId(body.getGameId());
        //获取该条游戏记录
        try{
            ug = userGameMapper.selectOne(ug);
        }catch(Exception e){
            throw new ProjectException("gameId存在重复，数据异常");
        }
        ug.setEndTime(new Date());
        ug.setOgScore(body.getScore());
        userGameMapper.updateByPrimaryKeySelective(ug);



        //用户积分变更
        AppUser appuser = this.getUserByPhone(body.getPhone());
        appuser.setAmount(appuser.getAmount()+body.getScore());
        appUserMapper.updateByPrimaryKeySelective(appuser);

        output.setResult(true);
        output.setPhone(body.getPhone());
        output.setLimite( this.getTodayLimite(body.getPhone())>topLimit ? topLimit: this.getTodayLimite(body.getPhone()) );
        output.setAmount(appuser.getAmount());

        result.setData(output);
        result.setMessage("success");
        result.setStatus(200);


        return result;
    }

    @Override
    @Transactional
    public CommonDto<CoinToAccountOutputDto> coinToAccount(CoinToAccountInputDto body) {
        //输入数据格式有效性验证
        if(StringUtils.isBlank(body.getAddress())){
            throw new DataFormatException("提币地址输入不正确");
        }
            //进行用户身份信息校验
        try{
            validateUser(body.getPhone(), body.getToken(),body.getExpire(),body.getTokenValidateStr());
        }catch(Exception e){
            throw e;
        }

        if(body.getIntegralChange() < Integer.valueOf(env.getProperty("moneyDrawBaseLine")) ){
            throw new AccountException("最小提币数量500 OG");
        }
        if(body.getIntegralChange() > this.getUserByPhone(body.getPhone()).getAmount()){
            throw new AccountException("余额不足");
        }

        CommonDto<CoinToAccountOutputDto> result=new CommonDto<>();
        CoinToAccountOutputDto output=new CoinToAccountOutputDto();

        AppIntergral appIntergral =new AppIntergral();
        appIntergral.setWallet(body.getAddress());
        appIntergral.setUserid( this.getUserByPhone(body.getPhone()).getUserid() );
        //1充币 2提币
        appIntergral.setType(2);
        //1表示飞船游戏
        appIntergral.setSystem(1);
        appIntergral.setIntegralChange(body.getIntegralChange());
        appIntergral.setCreateTime(new Date());
        //1待审核\r\n2已审核
        appIntergral.setAuditStatus(1);
        //插入用户的提币记录
        appIntergralMapper.insertSelective(appIntergral);

        output.setPhone(body.getPhone());
        output.setResult(true);
        output.setAmount( this.getAmountByUser(body.getPhone()) );
        output.setLimite( this.getTodayLimite(body.getPhone()) );

        result.setData(output);
        result.setMessage("success");
        result.setStatus(200);


        return result;
    }



    @Override
    @Transactional(readOnly = true)
    public CommonDto<List<OgObtainRankOutputDto>> ogObtainRank() {

        CommonDto<List<OgObtainRankOutputDto>> result=new CommonDto<>();

        List<OgObtainRankOutputDto> listRank = userGameMapper.ogObtainRank();

        System.err.println(listRank);

        result.setData(listRank);
        result.setMessage("用户获取金币排行榜");
        result.setStatus(200);

        return result;
    }

}
