package com.wl.spacecraft.service.impl;

import com.wl.spacecraft.mapper.CommunityMapper;
import com.wl.spacecraft.model.Community;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.community.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityServiceImpl extends GenericService implements CommunityService {

    @Autowired
    private CommunityMapper communityMapper;


    @Override
    public List<Community> selectAll() {
        return communityMapper.selectAll();
    }
}
