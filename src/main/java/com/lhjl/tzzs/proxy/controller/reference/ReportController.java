package com.lhjl.tzzs.proxy.controller.reference;


import com.lhjl.tzzs.proxy.controller.weixin.GenericController;
import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.bluewave.ReportReqBody;
import com.lhjl.tzzs.proxy.model.MetaColumn;
import com.lhjl.tzzs.proxy.model.MetaSegmentation;
import com.lhjl.tzzs.proxy.model.Report;
import com.lhjl.tzzs.proxy.service.bluewave.ColumnService;
import com.lhjl.tzzs.proxy.service.bluewave.ReportService;
import com.lhjl.tzzs.proxy.service.bluewave.SegmentationService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
public class ReportController extends GenericController {


    @Resource
    private ColumnService columnService;
    @Resource
    private SegmentationService segmentationService;
    @Resource
    private ReportService reportService;
    /**
     * 文章报告-栏目元数据表
     * @param appId  
     * @return
     */
    @GetMapping("/v{appid}/columns")
    public CommonDto<List<MetaColumn>> columnQuery(@PathVariable("appid") Integer appId ){
        CommonDto<List<MetaColumn>> result = null;

        try {
            result = columnService.queryAll(appId);
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e.fillInStackTrace());
            result = new CommonDto<>();
            result.setData(null);
            result.setStatus(500);
            result.setMessage(e.getMessage());
        }

        return result;
    }
    /**
     * 文章报告-栏目元数据表的保存和更新
     * @param appId
     * @param column
     * @return
     */
    @PostMapping("/v{appid}/columns")
    public CommonDto<String> columnSaveOrUpdate(@PathVariable("appid") Integer appId ,@RequestBody MetaColumn column){
        CommonDto<String> result = null;

        result = columnService.saveOrUpdate(appId,column);

        return result;
    }
    /**
     * 文章报告-栏目元数据的增加
     * @param appId
     * @param column
     * @return
     */
    @PostMapping("/v{appid}/columns/add")
    public CommonDto<String> columnSave(@PathVariable("appid") Integer appId ,@RequestBody MetaColumn column){
        CommonDto<String> result = null;

        try {
            result = columnService.save(appId,column);
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e.fillInStackTrace());
            result = new CommonDto<>();
            result.setStatus(500);
            result.setMessage("服务器繁忙，请售后再试。");
        }

        return result;
    }
    /**
     * 领域信息的获取
     * @param appId
     * @return
     */
    @GetMapping("/v{appid}/segmentations")
    public CommonDto<List<MetaSegmentation>> segmentationQuery(@PathVariable("appid") Integer appId ){
        CommonDto<List<MetaSegmentation>> result = null;

        try {
            result = segmentationService.queryAll(appId);
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e.fillInStackTrace());
            result = new CommonDto<>();
            result.setData(null);
            result.setStatus(500);
            result.setMessage(e.getMessage());
        }

        return result;
    }
    /**
     * 获取  报告基础信息表 列表分页
     * @param appId
     * @param reqBody
     * @return
     */
    @PostMapping("/v{appid}/report/list")
    public CommonDto<List<Map<String,Object>>> reportList(@PathVariable("appid") Integer appId ,@RequestBody ReportReqBody reqBody){
        CommonDto<List<Map<String,Object>>> result = null;

        try {
            result = reportService.queryReport(appId,reqBody);
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e.fillInStackTrace());
            result = new CommonDto<>();
            result.setStatus(500);
            result.setMessage("服务器繁忙，请售后再试。");
        }

        return result;

    }
    /**
     * 根据id获取report的信息
     * @param appId
     * @param id
     * @return
     */
    @GetMapping("/v{appid}/report/{id}")
    public CommonDto<Map<String,Object>> reportById(@PathVariable("appid") Integer appId ,@PathVariable("id") Integer id){
        CommonDto<Map<String,Object>> result = null;

        try {
            result = reportService.getReportById(appId,id);  
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e.fillInStackTrace());
            result = new CommonDto<>();
            result.setStatus(500);
            result.setMessage("服务器繁忙，请稍后再试。");
        }
        return result;
    }
    /**
     * report的保存和更新
     * @param appId
     * @param reqBody
     * @return
     */
    @PutMapping("/v{appid}/report")
    public CommonDto<String> reportSaveOrUpdate(@PathVariable("appid") Integer appId ,@RequestBody ReportReqBody reqBody ){
        CommonDto<String> result = null;
        
        try {
            result = reportService.saveOrUpdate(appId,reqBody);
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e.fillInStackTrace());
            result = new CommonDto<>();
            result.setStatus(500);
            result.setMessage("服务器繁忙，请售后再试。");
        }

        return  result;

    }

    @GetMapping("report/image/generate/{reportId}")
    @Cacheable(value = "generateReportShareImage",keyGenerator = "wiselyKeyGenerator")
    public HttpEntity<byte[]> generateReportShareImage( @PathVariable Integer reportId) throws IOException {

        CommonDto<String> result = null;
        InputStream in = null;
        try {
            in = reportService.generateReportShareImage(reportId);
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e.fillInStackTrace());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int l = in.read(buffer);
        while(l >= 0) {
            outputStream.write(buffer, 0, l);
            l = in.read(buffer);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(outputStream.toByteArray().length);
        headers.set("Content-Disposition", "attachment; filename="+ DateTime.now().millisOfDay()+".jpg");
        return new HttpEntity<byte[]>(outputStream.toByteArray(), headers);
    }

}
