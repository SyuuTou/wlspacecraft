package com.lhjl.tzzs.proxy.dto.ElegantServiceDto;

import java.util.List;

public class ElegantServiceSearchInputDto {

    /**是否推荐*/
    private Integer recommendYn;

    /**是否按最近时间排序*/
    private Integer createTimeOrder;

    /**搜索词*/
    private String searchWord;

    /**身份类型*/
    private List<Integer> identityType;

    /**服务类型*/
    private List<Integer> serviceType;

    /**页码*/
    private Integer pageNum;
    /** 认证类型 */
    private List<Integer> approveType;
    /** 会员类型 */
    private Integer memberType;
    /**每页显示数量*/
    private Integer pageSize;
    /** 是否悬赏 */
    private Integer isReward;
    /** 是否领投 */
    private Integer isLeadInvestor;
    /** 当前访问用户的Token */
    private String token;
    /** 业务类型：Filter：筛选 */
    private String businessType;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Integer> getApproveType() {
        return approveType;
    }

    public void setApproveType(List<Integer> approveType) {
        this.approveType = approveType;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public Integer getIsReward() {
        return isReward;
    }

    public void setIsReward(Integer isReward) {
        this.isReward = isReward;
    }

    public Integer getIsLeadInvestor() {
        return isLeadInvestor;
    }

    public void setIsLeadInvestor(Integer isLeadInvestor) {
        this.isLeadInvestor = isLeadInvestor;
    }

    public Integer getRecommendYn() {
        return recommendYn;
    }

    public void setRecommendYn(Integer recommendYn) {
        this.recommendYn = recommendYn;
    }

    public Integer getCreateTimeOrder() {
        return createTimeOrder;
    }

    public void setCreateTimeOrder(Integer createTimeOrder) {
        this.createTimeOrder = createTimeOrder;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public List<Integer> getIdentityType() {
        return identityType;
    }

    public void setIdentityType(List<Integer> identityType) {
        this.identityType = identityType;
    }

    public List<Integer> getServiceType() {
        return serviceType;
    }

    public void setServiceType(List<Integer> serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
