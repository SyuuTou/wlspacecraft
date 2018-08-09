package com.wl.spacecraft.service.impl;

import com.wl.spacecraft.mapper.CommunityGroupMapper;
import com.wl.spacecraft.mapper.CommunityMapper;
import com.wl.spacecraft.model.Community;
import com.wl.spacecraft.model.CommunityGroup;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.community.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityServiceImpl extends GenericService implements CommunityService {

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private CommunityGroupMapper communityGroupMapper;


    /**
     * 根据社区id获取子群列表
     *
     * @param communityId
     * @return
     */
    public List<CommunityGroup> getGroupsByCommunityId(Integer communityId) {
        List<CommunityGroup> list = new ArrayList<>();

        Example example=new Example(CommunityGroup.class);
        example.and().andEqualTo("delFlag",0).andEqualTo("communityId",communityId);
        example.setOrderByClause("isnull(sort) asc,sort asc");
        list =communityGroupMapper.selectByExample(example);

        System.err.println("communityId： "+communityId+" 下面的子群"+list);

        return list;
    }

    @Override
    public List<Community> selectAllCommunitiesOrderBySort() {

        Example example = new Example(Community.class);
        example.setOrderByClause("isnull(sort) asc,sort asc");
        /**
         * 获取所有的社区
         */
        List<Community> communities = communityMapper.selectByExample(example);

        for (Community e : communities
        ) {
            //设置社区下的所有子群
            List<CommunityGroup> groups = this.getGroupsByCommunityId(e.getId());
            e.setGroups(groups);
        }
        return communities;
    }

    @Override
    public Community getCommunityById(Integer communityId) {

        return communityMapper.selectByPrimaryKey(communityId);
    }
}
