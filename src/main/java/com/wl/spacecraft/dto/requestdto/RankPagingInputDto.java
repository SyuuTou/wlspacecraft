package com.wl.spacecraft.dto.requestdto;

import java.util.List;

public class RankPagingInputDto {
    /**
     * 搜索关键字
     */
    private String keyword;
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 每页显示的数目
     */
    private Integer pageSize;

    /**
     * 分页起始索引
     */
    private Long start;
    /**
     * 社区id
     * 社区id为-1或者空 就返回默认的世界排行榜 如果是带社区id的 就返回对应的社区排行榜
     */
    private Integer communityId;
    /**
     * 社区用户手机
     */
    private List<String> communityUsersPhones;

    /**
     * 游戏类型
     *
     */
    //TODO appKey是为了获取游戏排行，目前此功能是不需要的
    private String appKey;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public List<String> getCommunityUsersPhones() {
        return communityUsersPhones;
    }

    public void setCommunityUsersPhones(List<String> communityUsersPhones) {
        this.communityUsersPhones = communityUsersPhones;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return "RankPagingInputDto{" +
                "keyword='" + keyword + '\'' +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", start=" + start +
                ", communityId=" + communityId +
                ", communityUsersPhones=" + communityUsersPhones +
                ", appKey='" + appKey + '\'' +
                '}';
    }
}
