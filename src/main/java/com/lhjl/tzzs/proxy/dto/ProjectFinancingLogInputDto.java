package com.lhjl.tzzs.proxy.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProjectFinancingLogInputDto {
    /**搜索词*/
    private String searchWord;

    /**
     * 搜索开始时间输入字符串
     * abandoned
     */
    private String beginTimeInputStr;
    
    /**
     * 搜索开始时间
     */
    private Date beginTime;

    /**
     * 搜索结束时间输入字符串
     * abandoned
     */
    private String endTimeInputStr;
    
    /**
     * 搜索结束时间
     */
    private Date endTime;

    /**数据来源*/
    private List<Integer> dataSource;
    
    /**
     * 领域名称的字符串List
     */
    private List<String> segmentationName;

    /**轮次*/
    private List<String> stage;

    /**币种*/
    private List<Integer> currency;
    
    /**
     * 排序字段
     */
    private String column;
    /**
     * asc;desc
     */
    private String order;
    
    /**当前页码*/
    private Integer currentPage;

    /**每页显示数量*/
    private Integer pageSize;
    
    /**数据开始索引*/
    private Integer start;
    /**
     * 审核结果
     * '审核状态，0表示审核未通过，1表示审核通过，2表示待审核，默认0'
     */
    private List<Integer> approvalStatus;
	public String getSearchWord() {
		return searchWord;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	public String getBeginTimeInputStr() {
		return beginTimeInputStr;
	}
	public void setBeginTimeInputStr(String beginTimeInputStr) {
		this.beginTimeInputStr = beginTimeInputStr;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTimeInputStr() {
		return endTimeInputStr;
	}
	public void setEndTimeInputStr(String endTimeInputStr) {
		this.endTimeInputStr = endTimeInputStr;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public List<Integer> getDataSource() {
		return dataSource;
	}
	public void setDataSource(List<Integer> dataSource) {
		this.dataSource = dataSource;
	}
	public List<String> getSegmentationName() {
		return segmentationName;
	}
	public void setSegmentationName(List<String> segmentationName) {
		this.segmentationName = segmentationName;
	}
	public List<String> getStage() {
		return stage;
	}
	public void setStage(List<String> stage) {
		this.stage = stage;
	}
	public List<Integer> getCurrency() {
		return currency;
	}
	public void setCurrency(List<Integer> currency) {
		this.currency = currency;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public List<Integer> getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(List<Integer> approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	@Override 
	public String toString() {
		return "ProjectFinancingLogInputDto [searchWord=" + searchWord + ", beginTimeInputStr=" + beginTimeInputStr
				+ ", beginTime=" + beginTime + ", endTimeInputStr=" + endTimeInputStr + ", endTime=" + endTime
				+ ", dataSource=" + dataSource + ", segmentationName=" + segmentationName + ", stage=" + stage
				+ ", currency=" + currency + ", column=" + column + ", order=" + order + ", currentPage=" + currentPage
				+ ", pageSize=" + pageSize + ", start=" + start + ", approvalStatus=" + approvalStatus + "]";
	}

}
