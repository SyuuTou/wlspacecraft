package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.AppUser;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AppUserMapper extends OwnerMapper<AppUser> {

    @Select("select * from app_user where community_id=#{community_id}")
    @ResultType(AppUser.class)
    AppUser testMapper(@Param("community_id") Integer community_id);
}