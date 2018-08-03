package com.wl.spacecraft.service.community;

import com.wl.spacecraft.model.Community;

import java.util.List;

public interface CommunityService {

    /**
     * 获取所有的社区
     */
    List<Community> selectAll();
}
