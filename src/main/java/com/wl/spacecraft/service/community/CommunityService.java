package com.wl.spacecraft.service.community;

import com.wl.spacecraft.model.Community;
import com.wl.spacecraft.model.CommunityGroup;

import java.util.List;

public interface CommunityService {

    /**
     * 获取所有的社区以及社区,按照自定义排序（包括社区下的所有群组以及该社区的og投放总量）
     * @return 所有社区以及社区下的子群List，按照自定义排序进行排序
     */
    List<Community> selectAllCommunitiesOrderBySort();

    /**
     * 根据社区id获取社群信息
     * @return 返回社群信息
     */
    Community getCommunityByCommunityId(Integer communityId);

    /**
     * 根据群组id获取群组信息
     * @return 返回群组信息
     */
    CommunityGroup getGroupByGroupId(Integer groupId);
    /**
     * 测试用
     */
    Object test();

}
