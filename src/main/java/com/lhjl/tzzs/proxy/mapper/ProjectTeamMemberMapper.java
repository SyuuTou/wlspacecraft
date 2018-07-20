package com.lhjl.tzzs.proxy.mapper;

import com.lhjl.tzzs.proxy.model.ProjectTeamMember;
import com.lhjl.tzzs.proxy.utils.OwnerMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectTeamMemberMapper extends OwnerMapper<ProjectTeamMember> {

    List<ProjectTeamMember> getProjectMemberList(@Param("projectId") Integer projectId);
}