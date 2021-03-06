package com.wl.spacecraft.service.game;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.commondto.GameConfigCommonOutputDto;
import com.wl.spacecraft.dto.responsedto.MetaAppOutputDto;
import com.wl.spacecraft.model.ConfigMinExtractAmount;
import com.wl.spacecraft.model.ConfigMinRechargeAmount;
import com.wl.spacecraft.model.ConfigOgPrice;
import com.wl.spacecraft.model.MetaApp;

import java.math.BigDecimal;
import java.util.List;

public interface GameService {

    /**
     * 获取app元数据
     */
    List<MetaApp> selectAllApps();

    /**
     * 获取游戏的配置数据
     * @return 根据时间倒序返回游戏的最新配置
     */
    CommonDto<GameConfigCommonOutputDto> getGameConfig();
    /**
     * 获取OG当日领取上限配置记录
     * @return
     */
    Integer getConfigOgToday();

    /**
     * 获取最小充值总量
     * @return 最小充值总量记录
     */
    ConfigMinRechargeAmount getMinRechargeAmountRecord();

    /**
     * 获取最小提币总量
     * @return 最小提币总量记录
     */
    ConfigMinExtractAmount getMinExtractAmountRecord();

    /**
     * 获取Eth同OG兑换比例
     * @return
     */
    ConfigOgPrice getConfigOgPrice();

    /**
     * 获取飞船游戏难度系数
     * @return
     */
    Integer getConfigGameDifficulty();

    /**
     * 获取飞船游戏空投OG数量
     * @return
     */
    Integer getConfigDropogAmount();



    /**
     * 获取游戏数据
     * @return
     */
    CommonDto<List<MetaAppOutputDto>> metaAppInfo();


}
