package com.lhjl.tzzs.proxy.controller.reference;

import com.lhjl.tzzs.proxy.controller.weixin.GenericController;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.ReportCommentDto.ReportCommentInputDto;
import com.lhjl.tzzs.proxy.dto.ReportCommentDto.ReportCommentOutputDto;
import com.lhjl.tzzs.proxy.model.ReportCollection;
import com.lhjl.tzzs.proxy.model.ReportComment;
import com.lhjl.tzzs.proxy.model.ReportConcern;
import com.lhjl.tzzs.proxy.service.bluewave.ReportEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ReportDetailController extends GenericController {

    @Autowired
    private ReportEventService reportEventService;

    @PostMapping("/v{appId}/report/collection")
    public CommonDto<String> saveReportCollection(@PathVariable Integer appId, ReportCollection reqBody){

        CommonDto<String> result = null;

        try {
            result = reportEventService.saveReportCollection(appId, reqBody);
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e.fillInStackTrace());
        }


        return result;
    }

    @PostMapping("/v{appId}/report/comment")
    public CommonDto<String> saveReportComment(@PathVariable Integer appId, @RequestBody ReportCommentInputDto reportCommentInputDto){
        CommonDto<String> result = new CommonDto<>();
        try {
            result = reportEventService.saveReportComment(appId, reportCommentInputDto);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(500);
            result.setMessage("服务端错误");
            result.setData(null);
        }
        return result;
    }


    @PutMapping("/v{appId}/report/comment")
    public CommonDto<String> updateReportComment(@PathVariable Integer appId,@RequestBody ReportComment reportComment){
        CommonDto<String> result = null;

        try {
            result = reportEventService.updateReportComment(appId, reportComment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    @PutMapping("/v{appId}/report/comment/{commentId}/concen")
    public CommonDto<String> addReportCommentConcen(@PathVariable Integer appId,@PathVariable Long commentId, String token){

        CommonDto<String> result = null;

        try {
            result = reportEventService.updateReportCommentConcen(appId, commentId,token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    @DeleteMapping("/v{appId}/report/comment/{commentId}/concen")
    public CommonDto<String> deleteReportCommentConcen(@PathVariable Integer appId,@PathVariable Long commentId, String token){

        CommonDto<String> result = null;

        try {
            result = reportEventService.deleteReportCommentConcen(appId, commentId,token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    @GetMapping("/v{appId}/report/comment/{commentId}/concen")
    public CommonDto<Integer> getReportCommentConcenNum(@PathVariable Integer appId,@PathVariable Long commentId,String token){

        CommonDto<Integer> result = null;

        try {
            result = reportEventService.getReportCommentConcenNum(appId, commentId, token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    @GetMapping("/v{appId}/report/comment/{reportId}")
    public CommonDto<List<ReportCommentOutputDto>> findReportComment(@PathVariable Integer appId, @PathVariable Integer reportId, String token, Integer pageNo, Integer pageSize){
        CommonDto<List<ReportCommentOutputDto>> result = new CommonDto<>();
        if (null == pageSize){
            pageSize = 10;
        }
        try {
            result = reportEventService.findReportComment(appId, reportId, token,pageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping("/v{appId}/report/concen")
    public CommonDto<String> saveReportConcen(@PathVariable Integer appId,@RequestBody ReportConcern reportConcern){
        CommonDto<String> result = null;

        try {
            result = reportEventService.saveReportConcen(appId, reportConcern);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @PutMapping("/v{appId}/report/concen")
    public CommonDto<String> updateReportConcen(@PathVariable Integer appId, @RequestBody ReportConcern reportConcern){
        CommonDto<String> result = null;

        try {
            result = reportEventService.updateReportConcen(appId, reportConcern);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping("/v{appId}/report/concen/num")
    public CommonDto<Integer> getReportConcenNum(@PathVariable Integer appId, Integer reportId){
        CommonDto<Integer> result = null;

        try {
            result = reportEventService.findReportConcenNum(appId, reportId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




}
