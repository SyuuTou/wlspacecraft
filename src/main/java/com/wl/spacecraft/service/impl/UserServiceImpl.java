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

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserServiceImpl extends GenericService implements UserService {

    // 自定义秘钥
    private static final String KEY = "SPACECRAFT";
    //社区key
    private static final String COMMUNITY_KEY = "COMMUNITY";
    //子群key
    private static final String GROUP_KEY = "GROUP";

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
     * @param appKey 游戏app代码
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
    //TODO 该方法目前已经作废
    private Integer getTodayLimite(String phone) {
        return userGameMapper.getTodayLimite(phone, DateUtils.getStartTime(), DateUtils.getEndTime());
    }

    /**
     * 获取用户今日在每款游戏app中获取的og总量
     *
     * @param phone 用户的手机号
     * @return key：appKey ;value:用户在在app中的今日获取总量
     */
    private List<MetaAppOutputDto> getMyAppOgTodayAmountList(String phone) {
        //获取所有的app元数据
        CommonDto<List<MetaAppOutputDto>> listCommonDto = gameService.metaAppInfo();
        List<MetaAppOutputDto> list = listCommonDto.getData();
        //设置每个app的用户今日获取总量
        list.forEach((e) -> {
            e.setTodaytGotAmount(this.getTodayLimiteByApp(phone, e.getAppKey()));
        });
        return list;
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
     * 获取用户的金币总共赠送总量
     *
     * @return 金币赠送总量
     */
    public Integer getOgRewardAmount(List<String> usersPhones) {
        return userGameMapper.getOgRewardAmount(usersPhones);
    }

    /**
     * 我的排名
     *
     * @param phone
     * @param communityId
     * @param groupId
     * @return
     */
    private Long getMyRank(String phone, Integer communityId, Integer groupId) {

        UserGame userGame = new UserGame();
        userGame.setPhonenum(phone);
        List<UserGame> userGames = userGameMapper.select(userGame);
        //无rank记录，返回-1表示未参与,-2表示该社区暂无用户,-3表示用户不在该社区内,-4表示该群组暂无用户，-5表示用户不在该群组内
        Long rankFlag = -1L;
        if (userGames == null || userGames.size() == 0) {
            System.err.println("该用户无rank记录");
            return rankFlag;
        }
        //排行List
        List<GameRankEntity> rankList;
        if (communityId != null) {
            if (groupId != null) {//群组排名
                List<String> userPhonesByGroupId = this.getUserPhonesByCommunityOrGroupId(groupId, GROUP_KEY);
                if (userPhonesByGroupId == null || userPhonesByGroupId.size() == 0) {
                    System.err.println("该群组暂无用户");
                    rankFlag = -4L;
                    return rankFlag;
                }
                if (!userPhonesByGroupId.contains(phone)) {
                    System.err.println("用户不在该群组内");
                    rankFlag = -5L;
                    return rankFlag;
                }
                rankList = userGameMapper.getRankList(userPhonesByGroupId);
            } else {//社区排名
                List<String> userPhonesByCommunityId = this.getUserPhonesByCommunityOrGroupId(communityId, COMMUNITY_KEY);
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
            }
        } else { //世界排名
            rankList = userGameMapper.getRankList(null);
        }
        //设置用户的真实排名
        Long index = 1L;
        if (rankList != null && rankList.size() > 0) {
            for (GameRankEntity e : rankList) {
                if (phone.equals(e.getPhone())) {
                    System.err.println("我的rank排名" + index);
                    //返回用户的真实rank
                    return index;
                }
                index++;
            }
        }
        return rankFlag;
    }

    /**
     * @param communityOrGroupId 社区或者子群id
     * @return 社区或者子群的所有用户的手机号list
     * public,社区服务要使用到该方法
     */
    @Override
    public List<String> getUserPhonesByCommunityOrGroupId(Integer communityOrGroupId, String key) {
        AppUser appUser = new AppUser();
        switch (key) {
            case COMMUNITY_KEY: {
                //设置社群id
                appUser.setCommunityId(communityOrGroupId);
            }
            break;
            case GROUP_KEY: {
                //设置子群id
                appUser.setGroupId(communityOrGroupId);
            }
            break;
            default:
        }
        List<AppUser> select = appUserMapper.select(appUser);

        List<String> userPhones = new ArrayList<>();
        for (AppUser e : select
        ) {
            userPhones.add(e.getPhonenum());
        }

        System.err.println(key + "下 " + communityOrGroupId + " ：下的所有用户-->" + userPhones);

        return userPhones;
    }

    //供测试使用
    @Override
    @Transactional
    public Object test() {

//        Map<String, Object> map = new HashMap<>();
//        map.put("ogtoday", gameService.getConfigOgToday());
//        map.put("ogPrice", gameService.getConfigOgPrice());
//        map.put("diff", gameService.getConfigGameDifficulty());
//        map.put("drop", gameService.getConfigDropogAmount());
//        Integer qwe = this.getTodayLimiteByApp("13691066251", "SPACECRAFT");
//        map.put("topLimit",topLimit);
        AppUser au = new AppUser();
        au.setUserid(1);
        au.setNickName("小明");
        appUserMapper.updateByPrimaryKeySelective(au);
//        throw new ProjectException("自行抛出的异常");
        return null;

    }


    @Override
    public AppUser getUserViaPhone(String phone) {
        return this.getUserByPhone(phone);
    }

    @Override
    @Transactional
    public void delVirtualUser() {
        AppUser appUser = new AppUser();
        //虚拟用户
        appUser.setIsReal(0);
        List<AppUser> select = appUserMapper.select(appUser);
        for (AppUser e : select
        ) {
            String phonenum = e.getPhonenum();
            UserGame ug = new UserGame();
            ug.setPhonenum(phonenum);
            userGameMapper.delete(ug);

        }
        appUserMapper.delete(appUser);
    }

    @Override
    @Transactional
    public CommonDto<Boolean> communityRegistry(CommunityRegistryInputDto body) {
        //进行用户有效性判断
        validateUser(body.getPhone(), body.getToken(), body.getExpire(), body.getTokenValidateStr());

        CommonDto<Boolean> result = new CommonDto<>();
        //查询该手机号码的用户
        AppUser appUser = new AppUser();
        appUser.setPhonenum(body.getPhone());
        appUser = appUserMapper.selectOne(appUser);

        if (appUser.getCommunityId() != null) {
//            throw new ProjectException("用户已经注册社区");
            result.setData(false);
            result.setStatus(500);
            result.setMessage("用户已经注册社区");
        } else {
            appUser.setCommunityId(body.getCommunityId());
            appUser.setGroupId(body.getGroupId());
            appUserMapper.updateByPrimaryKeySelective(appUser);

            result.setData(true);
            result.setStatus(200);
            result.setMessage("success");
        }

        result.setType(null);
        return result;
    }

    @Override
    @Transactional
    public CommonDto<LoginOutputDto> login(LoginInputDto body) {

        //首先进行验证码的有效性判断
        validateMsg(body.getMsgCode(), body.getExpire(), body.getMsgValidateStr());

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

            user.setToken(token);
            user.setLastLoginTime(new Date());
            appUserMapper.updateByPrimaryKeySelective(user);

            output.setNote("登录成功");
            output.setUserStatus("login");
        } catch (Exception e) { //捕获到异常说明用户不存在，执行新用户的注册

            AppUser user = new AppUser();
            user.setPhonenum(body.getPhone());
            user.setToken(token);
            //新用户注册赠送金币
            user.setAmount(Integer.valueOf(env.getProperty("OgReward")));
            user.setLastLoginTime(new Date());
            user.setCreateTime(new Date());
            appUserMapper.insertSelective(user);

            output.setNote("注册成功");
            output.setUserStatus("register");
        }
        output.setResult(true);

        output.setCommunities(communityService.selectAllCommunitiesOrderBySort());

        CommonDto<LoginOutputDto> result = new CommonDto<>();

        //设置用户的信息
        UserInfoCommonOutputDto user = new UserInfoCommonOutputDto();

        user.setPhone(body.getPhone());
        user.setToken(token);
        user.setExpire(expire);
        user.setTokenValidateStr(tokenValidateStr);

        //注册用户此刻社群id是为null的，当前业务前端暂时不需要
        user.setCommunityId(this.getUserByPhone(body.getPhone()).getCommunityId());
        user.setAmount(this.getAmountByUser(body.getPhone()));
        //返回用户在今日每个app已经获取的og总量
        user.setMyAppOgTodayAmountList(this.getMyAppOgTodayAmountList(body.getPhone()));

        user.setTopLimit(gameService.getConfigOgToday());
        output.setUserData(user);

        //设置游戏元数据
        GameConfigCommonOutputDto gameData = gameService.getGameConfig().getData();
        output.setGameData(gameData);

        System.err.println("登录成功之后返回体：" + output);

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
        validateUser(body.getPhone(), body.getToken(), body.getExpire(), body.getTokenValidateStr());

        CommonDto<UserInfoOutputDto> result = new CommonDto<>();
        UserInfoOutputDto outputDto = new UserInfoOutputDto();

        //设置用户的基本信息数据
        UserInfoCommonOutputDto user = new UserInfoCommonOutputDto();
        //取得社区id
        user.setCommunityId(this.getUserByPhone(body.getPhone()).getCommunityId());
        user.setPhone(body.getPhone());
        user.setToken(body.getToken());
        user.setAmount(this.getAmountByUser(body.getPhone()));
        user.setExpire(body.getExpire());
        //返回用户在今日内没局app内已经获取的游戏总量
        user.setMyAppOgTodayAmountList(this.getMyAppOgTodayAmountList(body.getPhone()));

        user.setTokenValidateStr(body.getTokenValidateStr());
        user.setTopLimit(gameService.getConfigOgToday());

        GameConfigCommonOutputDto gameData = gameService.getGameConfig().getData();

        outputDto.setGameData(gameData);
        outputDto.setUserData(user);
        //设置社区
        outputDto.setCommunities(communityService.selectAllCommunitiesOrderBySort());

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
        validateUser(body.getPhone(), body.getToken(), body.getExpire(), body.getTokenValidateStr());

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
        validateUser(body.getPhone(), body.getToken(), body.getExpire(), body.getTokenValidateStr());

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
        validateUser(body.getPhone(), body.getToken(), body.getExpire(), body.getTokenValidateStr());

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
        //游戏类型的区分
        appIntergral.setAppKey(body.getAppKey());
        appIntergral.setUserid(this.getUserByPhone(body.getPhone()).getUserid());
        //1充币 2提币
        appIntergral.setType(2);
        //1表示飞船游戏(暂时该字段是没有意义的)
        appIntergral.setSystem(1);
        appIntergral.setIntegralChange(body.getIntegralChange());
        appIntergral.setCreateTime(new Date());
        //1待审核，2已审核
        appIntergral.setAuditStatus(1);
        //插入用户的提币记录
        appIntergralMapper.insertSelective(appIntergral);

        System.err.println("用户提币后金币总量" + this.getAmountByUser(body.getPhone()));
        output.setPhone(body.getPhone());
        output.setResult(true);
        output.setAmount(this.getAmountByUser(body.getPhone()));

        result.setData(output);
        result.setMessage("success");
        result.setStatus(200);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CommonDto<GameRankOutputDto> gameRank(String phone, RankPagingInputDto body) {

        this.getUserByPhone(phone);

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

        System.err.println("communityId->社区id: " + body.getCommunityId());
        System.err.println("gruopId->子群id: " + body.getGroupId());

        //获取游戏排行
        List<GameRankEntity> gameRankEntities = new ArrayList<>();
        //获取人员总数
        Integer total = 0;
        /**
         * 只可能是以下三种情况中一种
         * body.getCommunityId()==null && body.getGroupId()==null:获取世界排行
         * body.getCommunityId()!=null && body.getGroupId()==null:获取社区排行
         * body.getCommunityId()!=null && body.getGroupId()!=null:获取子群排行
         */
        if (body.getCommunityId() != null) {//获取社区或者子群排行信息

            if (body.getGroupId() != null) {//获取子群排行
                //设置排行类型
                rankOutputDto.setRankType("群组排行");
                //设置排行类型的具体名称
                CommunityGroup group = communityService.getGroupByGroupId(body.getGroupId());
                if(group!=null){
                    rankOutputDto.setRankTypeName(group.getGroupName());
                }

                //根据群组id获取群组内所有用户的手机号
                List<String> userPhones = this.getUserPhonesByCommunityOrGroupId(body.getGroupId(), GROUP_KEY);
                if (userPhones != null && userPhones.size() > 0) {
                    //设置子群用户
                    body.setGroupUserPhones(userPhones);

                    System.err.println("分页数据" + body);
                    gameRankEntities = userGameMapper.gameRankList(body);
                    total = userGameMapper.getRankTotal(body);
                    //设置子群内空投总量
                    rankOutputDto.setOgRewardAmount(this.getOgRewardAmount(userPhones));
                } else {//子群内无用户
                    rankOutputDto.setOgRewardAmount(0);
                }

            } else {//获取社区排行
                //设置排行类型
                rankOutputDto.setRankType("社区排行");
                //设置排行类型的具体名称
                Community community = communityService.getCommunityByCommunityId(body.getCommunityId());
                if(community!=null){
                    rankOutputDto.setRankTypeName(community.getCommName());
                }

                //根据社区id获取社群内所有用户的手机号
                List<String> userPhones = this.getUserPhonesByCommunityOrGroupId(body.getCommunityId(), COMMUNITY_KEY);

                if (userPhones != null && userPhones.size() > 0) {  //该社区内存在用户
                    //设置社区用户
                    body.setCommunityUsersPhones(userPhones);

                    System.err.println("分页数据" + body);
                    gameRankEntities = userGameMapper.gameRankList(body);
                    total = userGameMapper.getRankTotal(body);
                    //设置社区内空投总量
                    rankOutputDto.setOgRewardAmount(this.getOgRewardAmount(userPhones));
                } else { //社区内无用户
                    rankOutputDto.setOgRewardAmount(0);
                }
            }
        } else {//获取世界排行以及空投总量信息
            //设置排行类型
            rankOutputDto.setRankType("世界排行");
            //设置排行类型的具体名称
            rankOutputDto.setRankTypeName("世界");

            gameRankEntities = userGameMapper.gameRankList(body);
            total = userGameMapper.getRankTotal(body);
            //设置所有空投总量
            rankOutputDto.setOgRewardAmount(this.getOgRewardAmount(null));

        }

        //排名索引设置以及手机号加密处理
        long index = body.getStart() + 1;
        for (GameRankEntity e : gameRankEntities) {
            e.setRank(index++);
            e.setPhone(e.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        }

        pod.setList(gameRankEntities);
        pod.setTotal((long) total);
        pod.setCurrentPage(body.getCurrentPage());
        pod.setPageSize(body.getPageSize());

        rankOutputDto.setRankList(pod);
        //取得用户排名
        Long myRank = this.getMyRank(phone, body.getCommunityId(), body.getGroupId());
        rankOutputDto.setMyRank(myRank);
        //设置提示信息
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
            case "-4": {
                rankOutputDto.setMyRankNote("该群组暂无用户");
            }
            break;
            case "-5": {
                rankOutputDto.setMyRankNote("用户不在该群组内");
            }
            break;
            default: {
                rankOutputDto.setMyRankNote("用户真实排名");
            }
        }

        //设置用户的社区信息
        Integer communityId = this.getUserByPhone(phone).getCommunityId();
        if (communityId != null) {
            rankOutputDto.setMyCommunity(communityService.getCommunityByCommunityId(communityId));
        }
        //设置用户的群组信息
        Integer groupId = this.getUserByPhone(phone).getGroupId();
        if (groupId != null) {
            rankOutputDto.setMyGroup(communityService.getGroupByGroupId(groupId));
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
