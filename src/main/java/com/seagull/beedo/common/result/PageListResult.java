package com.seagull.beedo.common.result;

/**
 * Created by hgs on 2017/7/5.
 */
public class PageListResult<T> extends ListResult {
    /**
     * 当前第几页
     */
    private int currentPage = 1;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页条数
     */
    private int itemSize;
    /**
     * 总数
     */
    private int totalSize;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}
