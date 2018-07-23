package com.wl.spacecraft.dto.commondto;

public class PagingInputDto {
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

    @Override
    public String toString() {
        return "PagingInputDto{" +
                "keyword='" + keyword + '\'' +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", start=" + start +
                '}';
    }
}
