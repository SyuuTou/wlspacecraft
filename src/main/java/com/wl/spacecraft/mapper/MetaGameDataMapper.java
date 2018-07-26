package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.MetaGameData;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MetaGameDataMapper extends OwnerMapper<MetaGameData> {
    /**
     * 获取游戏的最新配置
     * @return（根据时间倒序取得最新配置)
     */
    MetaGameData getGameConfig();
}