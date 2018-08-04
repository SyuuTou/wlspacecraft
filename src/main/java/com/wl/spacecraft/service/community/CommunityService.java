package com.wl.spacecraft.service.community;

import com.wl.spacecraft.model.Community;

import java.util.List;

public interface CommunityService {

    /**
     * 获取所有的社区
     * @return 所有社区
     */
    List<Community> selectAll();

    /**
     * 根据社区id获取社群信息
     * @return 返回社群信息
     */
    Community getCommunityById(Integer communityId);

}
