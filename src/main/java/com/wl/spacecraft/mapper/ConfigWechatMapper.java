package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.ConfigWechat;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ConfigWechatMapper extends OwnerMapper<ConfigWechat> {

    /**
     * 根据自定义排序获取客服相关信息，升序
     * @return 微信客服列表
     */
    @Select("select * from config_wechat where del_flag =0 order by isnull(sort) asc,sort asc")
    @ResultType(ConfigWechat.class)
    List<ConfigWechat> selectAllOrderBySort();

}