package com.lhjl.tzzs.proxy.service.impl;

import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.HistogramList;
import com.lhjl.tzzs.proxy.dto.ProjectReqDto;
import com.lhjl.tzzs.proxy.dto.ProjectResDto;
import com.lhjl.tzzs.proxy.mapper.ProjectsMapper;
import com.lhjl.tzzs.proxy.service.GenericService;
import com.lhjl.tzzs.proxy.service.ProjectSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("projectSearchService")
public class ProjectSearchServiceImpl extends GenericService implements ProjectSearchService {

    @Autowired
    private ProjectsMapper projectsMapper;

    @Value("${statistics.beginTime}")
    private String beginTime;

    @Value("${statistics.endTime}")
    private String endTime;

    @Value("${pageNum}")
    private Integer pageNumDefault;

    @Value("${pageSize}")
    private Integer pageSizeDefault;

//    @Value("${statistics.beginTime}")
//    private String beginTime;
//
//    @Value("${statistics.endTime}")
//    private String endTime;

    @Transactional(readOnly = true)
    @Override
    public CommonDto<List<ProjectResDto>> projectFilter(ProjectReqDto reqDto) {

        if (StringUtils.isNotEmpty(reqDto.getDataVcType())) {
            reqDto.setDataVcType(reqDto.getDataVcType().replace("50指数机构", "1"));
            reqDto.setBeginTime(beginTime);
            reqDto.setEndTime(endTime);
        }

        if (StringUtils.isNotEmpty(reqDto.getCity())) {
            reqDto.setCity("'" + reqDto.getCity().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getSegmentation())) {
            reqDto.setSegmentation("'" + reqDto.getSegmentation().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getEdus())) {
            reqDto.setEdus("'" + reqDto.getEdus().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getWorks())) {
            reqDto.setWorks("'" + reqDto.getWorks().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getStage())) {
            reqDto.setStage("'" + reqDto.getStage().replace(",", "','") + "'");
        }

        if (null != reqDto.getSourceType() &&"chart".equals(reqDto.getSourceType())) {
            reqDto.setBeginTime(beginTime);
            reqDto.setEndTime(endTime);
        }

        if (StringUtils.isEmpty(reqDto.getDataVcType())&&StringUtils.isEmpty(reqDto.getCity())&&StringUtils.isEmpty(reqDto.getSegmentation())&&StringUtils.isEmpty(reqDto.getEdus())&&StringUtils.isEmpty(reqDto.getWorks())&&StringUtils.isEmpty(reqDto.getStage())&&StringUtils.isEmpty(reqDto.getKeyWords())){
            reqDto.setBeginTime(beginTime);
            reqDto.setEndTime(endTime);
        }

        if (StringUtils.isNotEmpty(reqDto.getKeyWords())) {
            if (reqDto.getKeyWords().lastIndexOf(",") == reqDto.getKeyWords().length() - 1) {
                reqDto.setKeyWords(reqDto.getKeyWords().substring(0, reqDto.getKeyWords().length() - 1));
            }
        }

        reqDto.setOffset((reqDto.getPageNo() - 1) * reqDto.getPageSize());
        List<ProjectResDto> projectResDtos = projectsMapper.projectFilter(reqDto);
        if (projectResDtos.size() > 0){
            for (ProjectResDto prd:projectResDtos){
                String segmentationStringa = "";
                if (prd.getSegmentation() != null){
                    String[] segmentation = prd.getSegmentation().split(",");
                    String[] segmentationForString = {};
                    if (segmentation.length < 4){
                        segmentationForString = segmentation;
                    }else {
                        String[] zhongzhuan = new String[3];
                        for (int i=0;i<3;i++){
                            zhongzhuan[i] = segmentation[i];
                        }
                        segmentationForString = zhongzhuan;
                    }

                    segmentationStringa = StringUtils.join(segmentationForString,",");
                }

                prd.setSegmentation(segmentationStringa);
            }
        }


        return new CommonDto<>(projectResDtos, "success", 200);
    }

    @Transactional(readOnly = true)
    @Override
    public CommonDto<List<ProjectResDto>> projectFilterOrderByFinancing(ProjectReqDto reqDto) {

        if (StringUtils.isNotEmpty(reqDto.getCity())) {
            reqDto.setCity("'" + reqDto.getCity().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getSegmentation())) {
            reqDto.setSegmentation("'" + reqDto.getSegmentation().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getEdus())) {
            reqDto.setEdus("'" + reqDto.getEdus().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getWorks())) {
            reqDto.setWorks("'" + reqDto.getWorks().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getStage())) {
            reqDto.setStage("'" + reqDto.getStage().replace(",", "','") + "'");
        }

        if (null != reqDto.getSourceType() &&"chart".equals(reqDto.getSourceType())) {
            reqDto.setBeginTime(beginTime);
            reqDto.setEndTime(endTime);
        }

        if (StringUtils.isEmpty(reqDto.getDataVcType())&&StringUtils.isEmpty(reqDto.getCity())&&StringUtils.isEmpty(reqDto.getSegmentation())&&StringUtils.isEmpty(reqDto.getEdus())&&StringUtils.isEmpty(reqDto.getWorks())&&StringUtils.isEmpty(reqDto.getStage())&&StringUtils.isEmpty(reqDto.getKeyWords())){
            reqDto.setBeginTime(beginTime);
            reqDto.setEndTime(endTime);
        }

        if (StringUtils.isNotEmpty(reqDto.getKeyWords())) {
            if (reqDto.getKeyWords().lastIndexOf(",") == reqDto.getKeyWords().length() - 1) {
                reqDto.setKeyWords(reqDto.getKeyWords().substring(0, reqDto.getKeyWords().length() - 1));
            }
        }
        reqDto.setOffset((reqDto.getPageNo() - 1) * reqDto.getPageSize());
        List<ProjectResDto> projectResDtos = new ArrayList<>();
        if (reqDto.getFinancingRecently() != null) {
            if (reqDto.getFinancingRecently() == 1) {
                projectResDtos = projectsMapper.projectFilterTwo(reqDto);
            } else {
                projectResDtos = projectsMapper.projectFilterOne(reqDto);
            }
        } else {
            projectResDtos = projectsMapper.projectFilterOne(reqDto);
        }

        if (projectResDtos.size() > 0){
            for (ProjectResDto prd:projectResDtos){
                String segmentationStringa = "";
                if (prd.getSegmentation() != null){
                    String[] segmentation = prd.getSegmentation().split(",");
                    String[] segmentationForString = {};
                    if (segmentation.length < 4){
                        segmentationForString = segmentation;
                    }else {
                        String[] zhongzhuan = new String[3];
                        for (int i=0;i<3;i++){
                            zhongzhuan[i] = segmentation[i];
                        }
                        segmentationForString = zhongzhuan;
                    }

                    segmentationStringa = StringUtils.join(segmentationForString,",");
                }

                prd.setSegmentation(segmentationStringa);
            }
        }

        return new CommonDto<>(projectResDtos, "success", 200);
    }

    @Transactional(readOnly = true)
    @Override
    public CommonDto<List<Map<String, Object>>> ralatedInstitution(ProjectReqDto reqDto) {
        CommonDto<List<Map<String, Object>>> result = new CommonDto<>();

        if (reqDto.getRelatedInstitution() == null) {
            result.setMessage("相关项目的参数未传入，或不正确");
            result.setStatus(502);
            result.setData(null);

            return result;

        } else if (reqDto.getRelatedInstitution() != 1) {
            result.setMessage("相关项目的参数未传入，或不正确");
            result.setStatus(502);
            result.setData(null);

            return result;
        }

        if (StringUtils.isNotEmpty(reqDto.getCity())) {
            reqDto.setCity("'" + reqDto.getCity().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getSegmentation())) {
            reqDto.setSegmentation("'" + reqDto.getSegmentation().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getEdus())) {
            reqDto.setEdus("'" + reqDto.getEdus().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getWorks())) {
            reqDto.setWorks("'" + reqDto.getWorks().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getStage())) {
            reqDto.setStage("'" + reqDto.getStage().replace(",", "','") + "'");
        }

        if (null != reqDto.getSourceType() &&"chart".equals(reqDto.getSourceType())) {
            reqDto.setBeginTime(beginTime);
            reqDto.setEndTime(endTime);
        }

        if (StringUtils.isEmpty(reqDto.getDataVcType())&&StringUtils.isEmpty(reqDto.getCity())&&StringUtils.isEmpty(reqDto.getSegmentation())&&StringUtils.isEmpty(reqDto.getEdus())&&StringUtils.isEmpty(reqDto.getWorks())&&StringUtils.isEmpty(reqDto.getStage())&&StringUtils.isEmpty(reqDto.getKeyWords())){
            reqDto.setBeginTime(beginTime);
            reqDto.setEndTime(endTime);
        }

        if (StringUtils.isNotEmpty(reqDto.getKeyWords())) {
            if (reqDto.getKeyWords().lastIndexOf(",") == reqDto.getKeyWords().length() - 1) {
                reqDto.setKeyWords(reqDto.getKeyWords().substring(0, reqDto.getKeyWords().length() - 1));
            }
        }

        reqDto.setOffset((reqDto.getPageNo() - 1) * reqDto.getPageSize());
        List<Map<String, Object>> list = new ArrayList<>();
        list = projectsMapper.relatedInvestmentInstitution(reqDto);

        result.setData(list);
        result.setStatus(200);
        result.setMessage("success");

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public CommonDto<List<HistogramList>> projectFilterStatistices(ProjectReqDto reqDto) {
        if (StringUtils.isNotEmpty(reqDto.getDataVcType())) {
            reqDto.setDataVcType(reqDto.getDataVcType().replace("50指数机构", "1"));
        }

        if (StringUtils.isNotEmpty(reqDto.getCity())) {
            reqDto.setCity("'" + reqDto.getCity().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getSegmentation())) {
            reqDto.setSegmentation("'" + reqDto.getSegmentation().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getEdus())) {
            reqDto.setEdus("'" + reqDto.getEdus().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getWorks())) {
            reqDto.setWorks("'" + reqDto.getWorks().replace(",", "','") + "'");
        }

        if (StringUtils.isNotEmpty(reqDto.getStage())) {
            reqDto.setStage("'" + reqDto.getStage().replace(",", "','") + "'");
        }

            if (null != reqDto.getSourceType() &&"chart".equals(reqDto.getSourceType())) {
                reqDto.setBeginTime(beginTime);
                reqDto.setEndTime(endTime);
            }
        if (StringUtils.isEmpty(reqDto.getDataVcType())&&StringUtils.isEmpty(reqDto.getCity())&&StringUtils.isEmpty(reqDto.getSegmentation())&&StringUtils.isEmpty(reqDto.getEdus())&&StringUtils.isEmpty(reqDto.getWorks())&&StringUtils.isEmpty(reqDto.getStage())&&StringUtils.isEmpty(reqDto.getKeyWords())){
            reqDto.setBeginTime(beginTime);
            reqDto.setEndTime(endTime);
        }
        if (StringUtils.isNotEmpty(reqDto.getKeyWords())) {
            if (reqDto.getKeyWords().lastIndexOf(",") == reqDto.getKeyWords().length() - 1) {
                reqDto.setKeyWords(reqDto.getKeyWords().substring(0, reqDto.getKeyWords().length() - 1));
            }
        }
        switch (reqDto.getStatisticsType()) {
            case "segmentation":
                reqDto.setStatisticsType("ps.segmentation_name");
                break;
            case "stage":
                reqDto.setStatisticsType("pfl.stage");
                break;
            case "city":
                reqDto.setStatisticsType("p.city");
                break;
            case "work":
                reqDto.setStatisticsType("ffw.work_experience");
                break;
            case "edus":
                reqDto.setStatisticsType("ffe.education_experience");
                break;
            case "financingTime":
                reqDto.setStatisticsType("pfl.financing_time_year");
                break;
        }

        reqDto.setOffset((reqDto.getPageNo() - 1) * reqDto.getPageSize());
        List<HistogramList> projectResDtos = projectsMapper.projectSearchStatistics(reqDto);


        return new CommonDto<>(projectResDtos, "success", 200);
    }

    @Override
    public CommonDto<List<ProjectResDto>> projectHightQuality(String token, Integer pageNum, Integer pageSize) {

        CommonDto<List<ProjectResDto>> result = new CommonDto<>();

        if (token == null || "".equals(token) || "undefined".equals(token)){
            result.setMessage("用户token不能为空");
            result.setData(null);
            result.setStatus(502);

            return result;
        }
        if (null == pageNum){
            pageNum = pageNumDefault;
        }
        if (null == pageSize){
            pageSize = pageSizeDefault;
        }

        Integer startPage = (pageNum-1)*pageSize;
        List<ProjectResDto> projectResDtos = projectsMapper.projectHighQuality(token,startPage,pageSize);

        return new CommonDto<>(projectResDtos, "success", 200);
    }


}
