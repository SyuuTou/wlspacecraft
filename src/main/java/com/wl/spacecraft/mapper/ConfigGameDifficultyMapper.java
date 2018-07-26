package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.ConfigGameDifficulty;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ConfigGameDifficultyMapper extends OwnerMapper<ConfigGameDifficulty> {
    /**
     * 获取最新的游戏难度配置记录
     * @return
     */
    ConfigGameDifficulty getConfigGameDifficulty();
}