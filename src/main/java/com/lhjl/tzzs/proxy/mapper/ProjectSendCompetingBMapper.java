package com.lhjl.tzzs.proxy.mapper;

import com.lhjl.tzzs.proxy.dto.ProjectSendSearchCommenDto;
import com.lhjl.tzzs.proxy.model.ProjectSendCompetingB;
import com.lhjl.tzzs.proxy.utils.OwnerMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectSendCompetingBMapper extends OwnerMapper<ProjectSendCompetingB> {
    /**
     * 复制提交项目竞品的查询
     * @param projectSendSearchCommenDto
     * @return
     */
    Integer copyProjectSendCompetingB(ProjectSendSearchCommenDto projectSendSearchCommenDto);

    List<ProjectSendCompetingB> selectByProjectId(@Param("projectId") Integer projectId);
}