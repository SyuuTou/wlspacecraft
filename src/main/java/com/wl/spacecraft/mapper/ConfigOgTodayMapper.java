package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.ConfigOgToday;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ConfigOgTodayMapper extends OwnerMapper<ConfigOgToday> {

    /**
     * 获取最新的OG当日领取上限
     * @return
     */
    Integer getConfigOgToday();
}