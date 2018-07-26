package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.ConfigOgPrice;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ConfigOgPriceMapper extends OwnerMapper<ConfigOgPrice> {
    /**
     * 获取最新的Eth同OG兑换比例
     * @return
     */
    ConfigOgPrice getConfigOgPrice();
}