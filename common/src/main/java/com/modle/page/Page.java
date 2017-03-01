package com.modle.page;

import java.io.Serializable;

/**
 * Created by fn on 2017/3/1.
 */
public class Page implements Serializable {
    /**
     * 当前页
     */
    private Integer pageNo;
    /**
     * 每页大小
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPage;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
