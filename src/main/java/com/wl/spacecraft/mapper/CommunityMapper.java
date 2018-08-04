package com.wl.spacecraft.mapper;

import com.wl.spacecraft.model.Community;
import com.wl.spacecraft.utils.OwnerMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommunityMapper extends OwnerMapper<Community> {
}