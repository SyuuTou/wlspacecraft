package com.wl.spacecraft.service.impl;

import com.lhjl.tzzs.proxy.mapper.UsersMapper;
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
import com.wl.spacecraft.model.*;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.community.CommunityService;
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
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserServiceImpl extends GenericService implements UserService {

    // 自定义秘钥
    private static final String KEY = "SPACECRAFT";

    @Resource
    private GameService gameService;
    /**
     * 每日积分上限
     */
//    private Integer topLimit;
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


    @Autowired
    private Environment env;

    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private UserGameMapper userGameMapper;
    @Autowired
    private AppIntergralMapper appIntergralMapper;

    @Resource
    private CommunityService communityService;


    /**
     * 用户信息的校验
     *
     * @param token String
     * @return
     */
    private boolean validateUser(String phone, String token, Date expire, String validateStr) {
        if (getUserByPhone(phone) != null
                && valivdateToken(token, expire, validateStr)) {
            return true;
        }
        return false;
    }

    /**
     * 验证前端传递的appKey是否有效
     *
     * @param appKey
     * @return true表示有效。false表示无效
     */
    private boolean validateAppKey(String appKey) {
        boolean flag = false;

        if (StringUtils.isBlank(appKey)) {
            return false;
        }

        List<MetaApp> metaApps = gameService.selectAllApps();
        for (MetaApp e : metaApps) {
            if (e.getAppKey().equals(appKey)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 用户存在性检查
     *
     * @param phone 用户的手机号
     * @return 用户信息
     */
    private AppUser getUserByPhone(String phone) {
        //数据格式校验
        if (StringUtils.isBlank(phone)) {
            throw new DataFormatException("手机号数据格式不正确");
        }

        AppUser au = new AppUser();
        au.setPhonenum(phone);
        List<AppUser> list = appUserMapper.select(au);

        //用户不存在
        if (null == list || list.size() == 0) {
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
     *
     * @param token
     * @return
     */
    private boolean valivdateToken(String token, Date expire, String tokenValidateStr) {
        System.err.println("原token校验串---》》" + tokenValidateStr);
        System.err.println("server端生成的token校验串---》》》" + MD5Util.md5Encode(KEY + "@" + token + "@" + expire.toString(), null));

        //数据格式校验
        if (StringUtils.isAnyBlank(token, tokenValidateStr) || expire == null) {
            throw new DataFormatException("数据格式有误");
        }

        if (expire.compareTo(new Date()) < 0) {
            throw new TokenIlleaglException("token过期");
        }
        //检验token的有效性
        if (!MD5Util.md5Encode(KEY + "@" + token + "@" + expire.toString(), null).equals(tokenValidateStr)) {
            throw new TokenIlleaglException("token非法");
        }

        return true;
    }

    /**
     * 短信验证码的校验
     *
     * @param msgCode        短信验证码
     * @param expire         该验证码的过期时间
     * @param msgValidateStr 短信Md5校验串
     * @return bool
     */
    private boolean validateMsg(String msgCode, Date expire, String msgValidateStr) {

        System.err.println("原短信校验串---》》" + msgValidateStr);
        System.err.println("server生成的短信校验串---》》》" + MD5Util.md5Encode(KEY + "@" + msgCode + "@" + expire.toString(), null));


        //数据格式校验
        if (StringUtils.isAnyBlank(msgCode, msgValidateStr) || expire == null) {
            throw new DataFormatException("数据格式有误");
        }

        if (expire.compareTo(new Date()) < 0) {
            throw new MsgCodeException("短信验证码过期");
        }

        //检验验证码输入的有效性
        if (!MD5Util.md5Encode(KEY + "@" + msgCode + "@" + expire.toString(), null).equals(msgValidateStr)) {

            throw new MsgCodeException("短信验证码有误");
        }
        return true;
    }

    /**
     * 返回用户今日获取的金币
     *
     * @param phone 用户手机号
     * @return 用户今日获取的金币
     */

    private Integer getTodayLimite(String phone) {
        return userGameMapper.getTodayLimite(phone, DateUtils.getStartTime(), DateUtils.getEndTime());
    }

    /**
     * @param phone  用户手机号
     * @param appKey 游戏Key
     * @return 返回今日内用户在这个游戏
     */
    private Integer getTodayLimiteByApp(String phone, String appKey) {
        return userGameMapper.getTodayLimiteByApp(appKey, phone, DateUtils.getStartTime(), DateUtils.getEndTime());
    }

    /**
     * 获取用户的持有的金币总额
     */
    private Integer getAmountByUser(String phone) {
        AppUser user = this.getUserByPhone(phone);
        return user.getAmount() == null ? 0 : user.getAmount();
    }

    /**
     * 获取用户游戏中获取的金币总量
     *
     * @param phone
     * @return
     */
    private Integer getOgRewardViaGame(String phone) {
        Integer myOgAmount = userGameMapper.getOgRewardViaGame(phone);
        return myOgAmount == null ? 0 : myOgAmount;
    }

    /**
     * 获取金币总共赠送总量
     *
     * @return 金币赠送总量
     */
    private Integer getOgRewardAmount(List<String> communityUsersPhones) {
        return userGameMapper.getOgRewardAmount(communityUsersPhones);
    }

    /**
     * 获取我的排名
     * communityId不为null则取得社区排名
     * communityId为null则取得世界排名
     *
     * @return 排名
     */
    //TODO
    private Long getMyRank(String phone, Integer communityId) {

        UserGame userGame = new UserGame();
        userGame.setPhonenum(phone);
        List<UserGame> userGames = userGameMapper.select(userGame);
        //无rank记录，返回-1表示未参与,-2表示该社区暂无用户,-3表示用户不在该社区内
        Long rankFlag = -1L;
        if (userGames == null || userGames.size() == 0) {
            System.err.println("没有本人的rank记录");
            return rankFlag;
        }
        //排行List
        List<GameRankEntity> rankList = new ArrayList<>();
        if (communityId != null) {
            List<String> userPhonesByCommunityId = this.getUserPhonesByCommunityId(communityId);
            if (userPhonesByCommunityId == null || userPhonesByCommunityId.size() == 0) {
                System.err.println("该社区暂无用户");
                rankFlag = -2L;
                return rankFlag;
            }
            if (!userPhonesByCommunityId.contains(phone)) {
                System.err.println("用户不在该社区内");
                rankFlag = -3L;
                return rankFlag;
            }
            rankList = userGameMapper.getRankList(userPhonesByCommunityId);
        } else { //世界排名
            rankList = userGameMapper.getRankList(null);
        }

        Long index = 1L;
        if (rankList != null && rankList.size() > 0) {
            for (GameRankEntity e : rankList) {
                if (phone.equals(e.getPhone())) {
                    System.err.println("我的rank排名" + index);
                    return index;
                }
                index++;
            }
        }

        //无rank记录
        return rankFlag;
    }

    /**
     * 根据社区编号获取该社区下的所有人员
     *
     * @param communityId 社区id
     * @return communityId为null的话会返回所有用户的手机号, 否则返回该社区下的用户的手机号
     */
    private List<String> getUserPhonesByCommunityId(Integer communityId) {
        AppUser appUser = new AppUser();
        appUser.setCommunityId(communityId);
        List<AppUser> select = appUserMapper.select(appUser);

        List<String> userPhones = new ArrayList<>();
        for (AppUser e : select
        ) {
            userPhones.add(e.getPhonenum());
        }

        System.err.println("社区id " + communityId + " 下的所有用户-->" + userPhones);

        return userPhones;
    }

    //供测试使用
    @Override
    public Object test() {

        Map<String, Object> map = new HashMap<>();
        map.put("ogtoday", gameService.getConfigOgToday());
        map.put("ogPrice", gameService.getConfigOgPrice());
        map.put("diff", gameService.getConfigGameDifficulty());
        map.put("drop", gameService.getConfigDropogAmount());
        Integer qwe = this.getTodayLimiteByApp("13691066251", "SPACECRAFT");
//        map.put("topLimit",topLimit);

        return qwe;
    }

    @Override
    public AppUser getUserViaPhone(String phone) {
        return this.getUserByPhone(phone);
    }

    @Override
    @Transactional
    public void delVirtualUser() {
        AppUser appUser=new AppUser();
        //虚拟用户
        appUser.setIsReal(0);
        List<AppUser> select = appUserMapper.select(appUser);
        for (AppUser e:select
             ) {
            String phonenum = e.getPhonenum();
            UserGame ug=new UserGame();
            ug.setPhonenum(phonenum);
            userGameMapper.delete(ug);

        }
        appUserMapper.delete(appUser);
    }

    @Override
    @Transactional
    public CommonDto<LoginOutputDto> login(LoginInputDto body) {

        //首先进行验证码的有效性判断
        try {
            validateMsg(body.getMsgCode(), body.getExpire(), body.getMsgValidateStr());
        } catch (Exception e) {
            throw e;
        }

        //执行token的生成策略
        String token = UUID.randomUUID().toString().replace("-", "");

        Calendar calendar = Calendar.getInstance();
        //设置token失效时间
        calendar.add(Calendar.DATE, Integer.valueOf(env.getProperty("tokenExpireTime")));
        Date expire = calendar.getTime();

        //生成token的校验串
        String tokenValidateStr = MD5Util.md5Encode(KEY + "@" + token + "@" + expire.toString(), "");

        LoginOutputDto output = new LoginOutputDto();

        try {//无异常，表示存在该用户，更新用户的token以及登录时间
            AppUser user = this.getUserByPhone(body.getPhone());
            //设置社区id为null即可，后端不做任何更新
            //TODO 社区id，登录
            user.setCommunityId(null);
            user.setToken(token);
            user.setLastLoginTime(new Date());
            appUserMapper.updateByPrimaryKeySelective(user);

            output.setNote("登录成功");
        } catch (Exception e) { //捕获到异常说明用户不存在，执行新用户的注册

            AppUser user = new AppUser();
            //设置社区id
            //TODO。注册
            user.setCommunityId(body.getCommunityId());
            user.setPhonenum(body.getPhone());
            user.setToken(token);
            //新用户注册赠送金币
            user.setAmount(Integer.valueOf(env.getProperty("OgReward")));
            user.setLastLoginTime(new Date());
            user.setCreateTime(new Date());
            appUserMapper.insertSelective(user);

            output.setNote("注册成功");
        }

        output.setResult(true);

        CommonDto<LoginOutputDto> result = new CommonDto<>();

        //设置用户的信息
        UserInfoCommonOutputDto user = new UserInfoCommonOutputDto();
        //TODO 设置社群id
        user.setCommunityId(this.getUserByPhone(body.getPhone()).getCommunityId());
        user.setPhone(body.getPhone());
        user.setToken(token);
        user.setAmount(this.getAmountByUser(body.getPhone()));
        user.setExpire(expire);
        user.setLimit(this.getTodayLimite(body.getPhone()));
        user.setTokenValidateStr(tokenValidateStr);
        user.setTopLimit(gameService.getConfigOgToday());

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
        try {
            validateUser(body.getPhone(), body.getToken(), body.getExpire(), body.getTokenValidateStr());
        } catch (Exception e) {
            throw e;
        }

        CommonDto<UserInfoOutputDto> result = new CommonDto<>();

        UserInfoOutputDto outputDto = new UserInfoOutputDto();
        //设置用户的基本信息数据
        UserInfoCommonOutputDto user = new UserInfoCommonOutputDto();
        user.setCommunityId(this.getUserByPhone(body.getPhone()).getCommunityId());
        user.setPhone(body.getPhone());
        user.setToken(body.getToken());
        user.setAmount(this.getAmountByUser(body.getPhone()));
        user.setExpire(body.getExpire());
        user.setLimit(this.getTodayLimite(body.getPhone()));
        user.setTokenValidateStr(body.getTokenValidateStr());
        user.setTopLimit(gameService.getConfigOgToday());

        GameConfigCommonOutputDto gameData = gameService.getGameConfig().getData();

        outputDto.setGameData(gameData);
        outputDto.setUserData(user);
        //设置社区
        outputDto.setCommunities(communityService.selectAllOrderBySort());

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
        try {
            validateUser(body.getPhone(), body.getToken(), body.getExpire(), body.getTokenValidateStr());
        } catch (Exception e) {
            throw e;
        }

        //验证appKey的正确性
        boolean flag = validateAppKey(body.getAppKey());
        if (!flag) {
            throw new ProjectException("appKey有误");
        }

        //用户总积分的判断
        AppUser au = this.getUserByPhone(body.getPhone());
        if (au.getAmount() < Integer.valueOf(env.getProperty("coinSubtract"))) {
            throw new OgLackException("用户金币不足");
        }

        //本日游戏积分没有到达上限
        System.err.println("用户在" + body.getAppKey() + "中的今日Og已获取总量：" + this.getTodayLimiteByApp(body.getPhone(), body.getAppKey()));
        if (this.getTodayLimiteByApp(body.getPhone(), body.getAppKey()) < gameService.getConfigOgToday()) {
            au.setAmount(au.getAmount() - Integer.valueOf(env.getProperty("coinSubtract")));
            appUserMapper.updateByPrimaryKeySelective(au);
        }

        CommonDto<GameStartOutputDto> result = new CommonDto<>();
        GameStartOutputDto output = new GameStartOutputDto();

        //插入用户的游戏记录
        //生成随机16位数字字母的随机游戏ID
        String random = RandomStr.randomStr(Integer.valueOf(env.getProperty("hexRandom")));

        System.err.println("游戏的唯一16位识别码" + random);

        UserGame userGame = new UserGame();
        //设置appKey
        userGame.setAppKey(body.getAppKey().toUpperCase());
        userGame.setPhonenum(body.getPhone());
        userGame.setToken(body.getToken());
        userGame.setGameId(random);
        userGame.setBeginTime(new Date());
        //游戏记录积分的扣除
        if (this.getTodayLimiteByApp(body.getPhone(), body.getAppKey()) < gameService.getConfigOgToday()) {
            userGame.setOgConsume(Integer.valueOf(env.getProperty("coinSubtract")));
        } else {
            userGame.setOgConsume(0);
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
        try {
            validateUser(body.getPhone(), body.getToken(), body.getExpire(), body.getTokenValidateStr());
        } catch (Exception e) {
            throw e;
        }
        //根据游戏id搜寻用户唯一的一条游戏记录，并获取appKey
        UserGame userGame = new UserGame();
        userGame.setGameId(body.getGameId());
        try {
            userGame = userGameMapper.selectOne(userGame);
        } catch (Exception e) {
            System.err.println("DB存在gameId重复数据");
            throw e;
        }
        String appKey = userGame.getAppKey();
        System.err.println("appKey--->" + appKey);

        //获取用户今天在该游戏App中已经获得的积分总数
        Integer todaySum = this.getTodayLimiteByApp(body.getPhone(), appKey);

        System.err.println("***todaySum==" + todaySum + "*******");

        //超出了该游戏内每日获取og的最大限制,则只能增加到最大限额
        if (todaySum + body.getScore() > gameService.getConfigOgToday()) {
            //重新设置应该增加的og总数
            body.setScore(gameService.getConfigOgToday() - todaySum);
        }

        CommonDto<GameOverOutputDto> result = new CommonDto<>();

        GameOverOutputDto output = new GameOverOutputDto();

        //首先进行用户游戏记录插入
        UserGame ug = new UserGame();
        ug.setGameId(body.getGameId());
        //获取该条游戏记录
        try {
            ug = userGameMapper.selectOne(ug);
        } catch (Exception e) {
            System.err.println("DB中gameId存在重复，数据异常");
            throw e;
        }
        ug.setEndTime(new Date());
        ug.setOgScore(body.getScore());
        userGameMapper.updateByPrimaryKeySelective(ug);

        //用户积分变更
        AppUser appuser = this.getUserByPhone(body.getPhone());
        appuser.setAmount(appuser.getAmount() + body.getScore());
        appUserMapper.updateByPrimaryKeySelective(appuser);

        output.setResult(true);
        output.setPhone(body.getPhone());
        output.setLimit(this.getTodayLimiteByApp(body.getPhone(), appKey) > gameService.getConfigOgToday() ? gameService.getConfigOgToday() : this.getTodayLimiteByApp(body.getPhone(), appKey));
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
        if (StringUtils.isBlank(body.getAddress())) {
            throw new DataFormatException("提币地址输入有误");
        }
        //进行用户身份信息校验
        try {
            validateUser(body.getPhone(), body.getToken(), body.getExpire(), body.getTokenValidateStr());
        } catch (Exception e) {
            throw e;
        }

        if (body.getIntegralChange() < Integer.valueOf(env.getProperty("moneyDrawBaseLine"))) {
            throw new AccountException("最小提币数量500 OG");
        }

        System.err.println("目前用户拥有的og总量" + this.getUserByPhone(body.getPhone()).getAmount());

        if (body.getIntegralChange() > this.getUserByPhone(body.getPhone()).getAmount()) {
            throw new AccountException("余额不足");
        }

        CommonDto<CoinToAccountOutputDto> result = new CommonDto<>();
        CoinToAccountOutputDto output = new CoinToAccountOutputDto();

        //执行用户的金币总量扣除
        AppUser au = this.getUserByPhone(body.getPhone());
        au.setAmount(au.getAmount() - body.getIntegralChange());
        appUserMapper.updateByPrimaryKeySelective(au);

        //用户充币提币记录的新增
        AppIntergral appIntergral = new AppIntergral();
        appIntergral.setWallet(body.getAddress());
        appIntergral.setUserid(this.getUserByPhone(body.getPhone()).getUserid());
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


        System.err.println("用户提币后金币总量" + this.getAmountByUser(body.getPhone()));
        output.setPhone(body.getPhone());
        output.setResult(true);
        output.setAmount(this.getAmountByUser(body.getPhone()));

//        output.setLimit( this.getTodayLimite(body.getPhone()) );

        result.setData(output);
        result.setMessage("success");
        result.setStatus(200);


        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CommonDto<GameRankOutputDto> gameRank(String phone, RankPagingInputDto body) {

        try {
            this.getUserByPhone(phone);
        } catch (Exception e) {
            throw e;
        }

        CommonDto<GameRankOutputDto> result = new CommonDto<>();
        //分页输出数据
        PagingOutputDto<GameRankEntity> pod = new PagingOutputDto<>();

        GameRankOutputDto rankOutputDto = new GameRankOutputDto();

        //输入参数格式化
        if (body.getCurrentPage() == null) {
            body.setCurrentPage(currentPageDefault);
        }

        if (body.getPageSize() == null) {
            body.setPageSize(pageSizeDefault);
        }

        //设置起始索引
        body.setStart((long) (body.getCurrentPage() - 1) * body.getPageSize());

        System.err.println("communityId: " + body.getCommunityId());

        //获取游戏排行
        List<GameRankEntity> gameRankEntities = new ArrayList<>();
        //获取人员总数
        Integer total = 0;

        if (body.getCommunityId() != null) {  //获取社区排行以及空投总量信息
            //根据社群id获取社群内所有用户的手机号
            List<String> userPhones = this.getUserPhonesByCommunityId(body.getCommunityId());

            if (userPhones != null && userPhones.size() > 0) {  //该社区内存在用户
                body.setCommunityUsersPhones(userPhones);

                System.err.println("分页数据" + body);
                gameRankEntities = userGameMapper.gameRankList(body);
                total = userGameMapper.getRankTotal(body);
                //设置空投总量
                rankOutputDto.setOgRewardAmount(this.getOgRewardAmount(userPhones));
            } else { //社区内无用户
                rankOutputDto.setOgRewardAmount(0);
            }
        } else {//获取世界排行以及空投总量信息
            gameRankEntities = userGameMapper.gameRankList(body);
            total = userGameMapper.getRankTotal(body);
            //communityUsersPhones设置为null则会查询所有空投总量
            rankOutputDto.setOgRewardAmount(this.getOgRewardAmount(null));
        }
        //排名索引设置以及手机号加密处理
        Long index = body.getStart() + 1;
        for (GameRankEntity e : gameRankEntities) {
            e.setRank(index++);
            e.setPhone(e.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        }

        pod.setList(gameRankEntities);
        pod.setTotal((long) total);
        pod.setCurrentPage(body.getCurrentPage());
        pod.setPageSize(body.getPageSize());

        rankOutputDto.setRankList(pod);
        //TODO 用户排名
        Long myRank = this.getMyRank(phone, body.getCommunityId());
        switch (String.valueOf(myRank)) {
            case "-1": {
                rankOutputDto.setMyRankNote("用户未参与");
            }
            break;
            case "-2": {
                rankOutputDto.setMyRankNote("该社区暂无用户");
            }
            break;
            case "-3": {
                rankOutputDto.setMyRankNote("用户不在该社区内");
            }
            break;
            default: {
                rankOutputDto.setMyRankNote("用户真实排名");
            }

        }
        rankOutputDto.setMyRank(myRank);

        //设置用户的社群信息
        if (body.getCommunityId() != null) {
            rankOutputDto.setMyCommunity(communityService.getCommunityById(body.getCommunityId()));
        }

        //设置用户手机号
        rankOutputDto.setPhone(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        //设置用户的og获取总量
        rankOutputDto.setMyOgAmount(this.getOgRewardViaGame(phone));

        result.setData(rankOutputDto);
        result.setMessage("success");
        result.setStatus(200);

        return result;
    }

}
