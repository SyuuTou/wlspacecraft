package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.AppUser;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppUserMapper extends OwnerMapper<AppUser> {


}