package com.wl.spacecraft.service.user;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.requestdto.*;
import com.wl.spacecraft.dto.responsedto.*;

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
    CommonDto<List<OgObtainRankOutputDto>> ogObtainRank();

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
}
