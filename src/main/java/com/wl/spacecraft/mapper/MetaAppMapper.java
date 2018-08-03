package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.MetaApp;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MetaAppMapper extends OwnerMapper<MetaApp> {

}
