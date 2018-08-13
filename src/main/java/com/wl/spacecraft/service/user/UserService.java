package com.wl.spacecraft.service.user;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.requestdto.*;
import com.wl.spacecraft.dto.responsedto.*;
import com.wl.spacecraft.model.AppUser;

import java.util.List;

public interface UserService {
    /**
     * 返回用户大厅界面 的基本信息
     * @param body
     * @return
     */
    CommonDto<UserInfoOutputDto> getUserInfo(UserInfoInputDto body);

    /**
     * 用户开始游戏
     * @return
     */
    CommonDto<GameStartOutputDto> startGame(GameStartInputDto body);

    /**
     * 用户金币获取排行榜
     * @return
     */
    CommonDto<GameRankOutputDto> gameRank(String phone,RankPagingInputDto body);

    /**
     * 游戏结束
     * @return
     */
    CommonDto<GameOverOutputDto> overGame(GameOverInputDto body);

    /**
     * 用户金币提取
     * @param body
     * @return
     */
    CommonDto<CoinToAccountOutputDto> coinToAccount(CoinToAccountInputDto body);

    /**
     * 用户登录
     * @param
     * @return
     */
    CommonDto<LoginOutputDto> login(LoginInputDto body);

    /**
     * 测试用
     * @return
     */
    Object test();
    /**
     * 根据手机号获取用户(供外部调用)
     */
    AppUser getUserViaPhone(String phone);

    /**
     * 删除虚拟用户
     */
    void delVirtualUser();

    /**
     * 社区注册
     * @param body 用户信息以及社区id
     * @return 社区注册成功与否的标志
     */
    CommonDto<Boolean> communityRegistry(CommunityRegistryInputDto body);

    /**
     * 根据社区或者群组id取得该领域下的所有用户
     * @param communityOrGroupId
     * @param key 社区或者群组的标志
     * @return 该领域下的手机list
     */
    List<String> getUserPhonesByCommunityOrGroupId(Integer communityOrGroupId, String key);

    /**
     * 获取用户的金币总共赠送总量
     * @param usersPhones 用户
     * @return 该用户list中的所有og投放总量
     */
    Integer getOgRewardAmount(List<String> usersPhones);
}
