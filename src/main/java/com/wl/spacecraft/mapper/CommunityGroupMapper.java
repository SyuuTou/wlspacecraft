package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.AppIntergral;
import com.wl.spacecraft.model.CommunityGroup;
import com.wl.spacecraft.utils.OwnerMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface CommunityGroupMapper extends OwnerMapper<CommunityGroup> {
}