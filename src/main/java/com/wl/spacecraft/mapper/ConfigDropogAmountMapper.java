package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.ConfigDropogAmount;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ConfigDropogAmountMapper extends OwnerMapper<ConfigDropogAmount> {
    /**
     * 获取最新的飞船游戏空投OG数量记录
     */
    void getgetConfigDropogAmount();
}