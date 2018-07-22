package com.wl.spacecraft.service.game;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.commondto.GameConfigCommonOutputDto;

public interface GameService {

    /**
     * 获取游戏的配置数据
     * @return 根据时间倒序返回游戏的最新配置
     */
    CommonDto<GameConfigCommonOutputDto> getGameConfig();


}
