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
     * @return 群组list
     */
    private List<CommunityGroup> getGroupsByCommunityId(Integer communityId) {

        Example example=new Example(CommunityGroup.class);
        example.and().andEqualTo("delFlag",0).andEqualTo("communityId",communityId);
        example.setOrderByClause("isnull(sort) asc,sort asc");
        List<CommunityGroup> list =communityGroupMapper.selectByExample(example);

        System.err.println("communityId "+communityId+" ：下面的子群"+list);

        return list;
    }

    @Override
    public List<Community> selectAllCommunitiesOrderBySort() {

        Example example = new Example(Community.class);
        example.setOrderByClause("isnull(sort) asc,sort asc");
        //根据自定义排序获取所有的社区
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
    public Community getCommunityByCommunityId(Integer communityId) {

//        Example example = new Example(Community.class);
//        example.and().andEqualTo("delFlag",0).andEqualTo("id",communityId);
        Community community=new Community();
        community.setDelFlag(0);
        community.setId(communityId);
        List<Community> communities = communityMapper.select(community);
        if(communities.size()>0){
            return communities.get(0);
        }
        return null;
    }

    @Override
    public CommunityGroup getGroupByGroupId(Integer groupId) {

        Example example = new Example(CommunityGroup.class);
        example.and().andEqualTo("delFlag",0).andEqualTo("id",groupId);

        CommunityGroup group=new CommunityGroup();
        group.setDelFlag(0);
        group.setId(groupId);

        List<CommunityGroup> communityGroups = communityGroupMapper.select(group);

        if(communityGroups.size()>0){
            return communityGroups.get(0);
        }
        return null;
    }

    @Override
    public Object test() {
        return this.getCommunityByCommunityId(1);
    }
}
