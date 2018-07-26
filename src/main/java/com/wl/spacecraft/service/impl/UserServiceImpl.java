package com.wl.spacecraft.service.impl;

import com.wl.spacecraft.dto.commondto.*;
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

    /**
     * 每日积分上限
     */
    @Value("${topLimit}")
    private Integer topLimit;
    /**
     * 当前页
     */
    @Value("${currentPageDefault}")
    private Integer currentPageDefault;

    /**
     * 每页显示的条数
     */
    @Value("${pageSizeDefault}")
    private Integer pageSizeDefault;

    @Resource
    private GameService gameService;

    @Autowired
    private Environment env;

    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private UserGameMapper userGameMapper;
    @Autowired
    private AppIntergralMapper appIntergralMapper;


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

//    public static boolean checkCellphone(String cellphone) {
//        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
//        return check(cellphone, regex);
//    }
    /**
     * 校验token的合法性
     * @param token
     * @return
     */
    private boolean valivdateToken(String token,Date expire,String tokenValidateStr){
        System.err.println("原token校验串---》》"+tokenValidateStr);
        System.err.println("server端生成的token校验串---》》》"+ MD5Util.md5Encode(KEY + "@" + token + "@" + expire.toString(),null ) );

        //数据格式校验
        if(StringUtils.isAnyBlank(token,tokenValidateStr)){
            throw new DataFormatException("token数据格式不正确");
        }
        if(expire == null){
            throw new DataFormatException("时间校验串不能为null");
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
     * @param msgValidateStr 短信Md5校验串
     * @return
     */
    private boolean validateMsg(String msgCode,Date expire,String msgValidateStr){

        System.err.println("原短信校验串---》》"+msgValidateStr+"》》》》》》");
        System.err.println("server生成的短信校验串---》》》"+ MD5Util.md5Encode(KEY + "@" + msgCode + "@" + expire.toString(),null )+"》》》》》》" );


        //数据格式校验
        if(StringUtils.isAnyBlank(msgCode,msgValidateStr)){
            throw new DataFormatException("短信验证码、或者失效时间的格式不正确");
        }

        if(expire == null){
            throw new DataFormatException("时间校验串不能为null");
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

    /**
     * 获取用户游戏中获取的金币总量
     * @param phone
     * @return
     */
    private Integer getOgRewardViaGame(String phone ){
        Integer myOgAmount = userGameMapper.getOgRewardViaGame(phone);
        return  myOgAmount== null ? 0: myOgAmount ;
    }

    /**
     * 获取金币总共赠送总量
     * @return
     */
    private Integer getOgRewardAmount(){
        return userGameMapper.getOgRewardAmount();
    }
    //供测试使用
    @Override
    public Object test() {
        Integer amount = this.getTodayLimite("4");
        //测试数据是否为null
        System.out.println( amount );

        return null;
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
        //设置token失效时间
        calendar.add(Calendar.DATE , Integer.valueOf(env.getProperty("tokenExpireTime")) );
        Date expire = calendar.getTime();

        //生成token的校验串
        String tokenValidateStr = MD5Util.md5Encode(KEY + "@" + token + "@" + expire.toString(),"");

        LoginOutputDto output=new LoginOutputDto();

        try{//存在该用户，更新用户的token以及登录时间
            AppUser user = this.getUserByPhone(body.getPhone());
            user.setToken(token);
            user.setLastLoginTime(new Date());
            appUserMapper.updateByPrimaryKeySelective(user);

            output.setNote("登录成功");
        }catch (Exception e){ //捕获到异常说明用户不存在，执行新用户的注册

            AppUser user=new AppUser();
            user.setPhonenum(body.getPhone());
            user.setToken(token);
            //新用户注册赠送金币
            user.setAmount( Integer.valueOf(env.getProperty("OgReward")) );
            user.setLastLoginTime(new Date());
            user.setCreateTime(new Date());
            appUserMapper.insertSelective(user);

            output.setNote("注册成功");
        }

        output.setResult(true);

        CommonDto<LoginOutputDto> result =new CommonDto<>();

        //设置用户的信息
        UserInfoCommonOutputDto user =new UserInfoCommonOutputDto();
        user.setPhone(body.getPhone());
        user.setToken(token);
        user.setAmount( this.getAmountByUser(body.getPhone()) );
        user.setExpire(expire);
        user.setLimit(this.getTodayLimite(body.getPhone()));
        user.setTokenValidateStr(tokenValidateStr);
        user.setTopLimit( Integer.valueOf(env.getProperty("topLimit")) );
        output.setUserData(user);

        //设置游戏元数据
        GameConfigCommonOutputDto gameData = gameService.getGameConfig().getData();
        output.setGameData(gameData);

        result.setData(output);
        result.setMessage("success");
        result.setStatus(200);
        result.setType("login");

        return result;
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

        UserInfoOutputDto outputDto=new UserInfoOutputDto();
        //设置用户的基本信息数据
        UserInfoCommonOutputDto user =new UserInfoCommonOutputDto();
        user.setPhone(body.getPhone());
        user.setToken(body.getToken());
        user.setAmount( this.getAmountByUser(body.getPhone()) );
        user.setExpire( body.getExpire() );
        user.setLimit(this.getTodayLimite(body.getPhone()));
        user.setTokenValidateStr(body.getTokenValidateStr());
        user.setTopLimit( Integer.valueOf(env.getProperty("topLimit")) );


        GameConfigCommonOutputDto gameData = gameService.getGameConfig().getData();

        outputDto.setGameData(gameData);
        outputDto.setUserData(user);

        result.setData(outputDto);
        result.setMessage("success");
        result.setStatus(200);
        result.setType("query");

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

        //积分的扣除
        AppUser au = this.getUserByPhone(body.getPhone());
        if(au.getAmount() < Integer.valueOf(env.getProperty("coinSubtract")) ){
            throw new OgLackException("用户金币不足");
        }

        //本日游戏积分没有到达上限
        if(this.getTodayLimite(body.getPhone()) < topLimit ){
            au.setAmount(au.getAmount()- Integer.valueOf(env.getProperty("coinSubtract")));
            appUserMapper.updateByPrimaryKeySelective(au);
        }


        CommonDto<GameStartOutputDto> result=new CommonDto<>();
        GameStartOutputDto output =new GameStartOutputDto();

        //插入用户的游戏记录
        //生成随机16位数字字母的随机游戏ID
        String random = RandomStr.randomStr( Integer.valueOf(env.getProperty("hexRandom")) );

        System.err.println("游戏的唯一16位识别码"+random);

        UserGame userGame =new UserGame();
        userGame.setPhonenum(body.getPhone());
        userGame.setToken(body.getToken());
        userGame.setGameId(random);
        userGame.setBeginTime(new Date());
        if(this.getTodayLimite(body.getPhone()) < topLimit){
            userGame.setOgConsume( Integer.valueOf(env.getProperty("coinSubtract")) );
        }else{
            userGame.setOgConsume( 0 );


        }
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
        output.setLimit( this.getTodayLimite(body.getPhone())>topLimit ? topLimit: this.getTodayLimite(body.getPhone()) );
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

        System.err.println("目前用户拥有的og总量"+this.getUserByPhone(body.getPhone()).getAmount() );

        if(body.getIntegralChange() > this.getUserByPhone(body.getPhone()).getAmount()){
            throw new AccountException("余额不足");
        }

        CommonDto<CoinToAccountOutputDto> result=new CommonDto<>();
        CoinToAccountOutputDto output=new CoinToAccountOutputDto();

        //执行用户的金币总量扣除
        AppUser au = this.getUserByPhone(body.getPhone());
        au.setAmount( au.getAmount() - body.getIntegralChange() );
        appUserMapper.updateByPrimaryKeySelective(au);

        //用户充币提币记录的新增
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


        System.err.println("用户提币后金币总量"+this.getAmountByUser(body.getPhone()));
        output.setPhone(body.getPhone());
        output.setResult(true);
        output.setAmount( this.getAmountByUser(body.getPhone()) );

//        output.setLimit( this.getTodayLimite(body.getPhone()) );

        result.setData(output);
        result.setMessage("success");
        result.setStatus(200);


        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CommonDto<GameRankOutputDto> gameRank(String phone,PagingInputDto body) {

        CommonDto<GameRankOutputDto> result=new CommonDto<>();
        //分页输出数据
        PagingOutputDto<GameRankEntity> pod=new PagingOutputDto<>();

        GameRankOutputDto rankOutputDto=new GameRankOutputDto();

        //输入参数格式化
        if( body.getCurrentPage()== null ) {
            body.setCurrentPage(currentPageDefault);
        }
        if(body.getPageSize() == null) {
            body.setPageSize(pageSizeDefault);
        }
        //设置起始索引
        body.setStart( (long)(body.getCurrentPage()-1) * body.getPageSize());


        //获取游戏排行榜的list
        List<GameRankEntity> gameRankEntities = userGameMapper.gameRankList(body);
        gameRankEntities.forEach((e)->{
            e.setPhone(e.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        });
        Integer total = userGameMapper.getRankTotal();

        pod.setList(gameRankEntities);
        pod.setTotal( (long)total );
        pod.setCurrentPage(body.getCurrentPage());
        pod.setPageSize(body.getPageSize());

        rankOutputDto.setRankList(pod);
        rankOutputDto.setOgRewardAmount( this.getOgRewardAmount() );

        try{
            this.getUserByPhone(phone);
        }catch(Exception e){
            throw e;
        }
        rankOutputDto.setPhone(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        rankOutputDto.setMyOgAmount(this.getOgRewardViaGame(phone) );

        result.setData(rankOutputDto);
        result.setMessage("success");
        result.setStatus(200);

        return result;
    }

}
