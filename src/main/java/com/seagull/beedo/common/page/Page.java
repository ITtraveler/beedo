/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.common.page;

import java.io.Serializable;

/**
 * @author guosheng.huang
 * @version $id:Page.java, v 0.1 2018年08月12日 14:49 tao.hu Exp $
 */
public final class Page implements Serializable {
    private static final long serialVersionUID = 3143231054182844861L;
    private int firstPage;
    private int lastPage;
    private int nextPage;
    private int prevPage;
    private int currentPage;
    private int totalPage;
    private int rowCount;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrev;
    private boolean hasFirst;
    private boolean hasLast;
    private static final int BUTTON_COUNT = 5;

    private Page() {
        this(1, 10, 0);
    }

    private Page(int currentPage, int pageSize, int rowCount) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.rowCount = rowCount;
        this.totalPage = this.rowCount % pageSize == 0 ? this.rowCount / pageSize : this.rowCount / pageSize + 1;
        if (this.totalPage > 0) {
            this.hasFirst = true;
            this.firstPage = currentPage - 2;
            if (this.firstPage > this.totalPage - 5 + 1) {
                this.firstPage = this.totalPage - 5 + 1;
            }

            if (this.firstPage < 1) {
                this.firstPage = 1;
            }
        }

        if (this.currentPage > 1) {
            this.hasPrev = true;
            this.prevPage = this.currentPage - 1;
        }

        if (this.totalPage > 0 && this.currentPage < this.totalPage) {
            this.hasNext = true;
            this.nextPage = this.currentPage + 1;
        }

        if (this.totalPage > 0) {
            this.hasLast = true;
            this.lastPage = this.firstPage + 5 - 1;
            if (this.lastPage > this.totalPage) {
                this.lastPage = this.totalPage;
            }

            if (this.currentPage > this.lastPage) {
                this.currentPage = this.lastPage;
            }
        }

    }

    public static Page getZeroRecordPage() {
        return new Page();
    }

    public static Page getInstance(PageAttribute pageAttr, int rowCount) {
        return new Page(pageAttr.getPageIndex(), pageAttr.getPageSize(), rowCount);
    }

    public int getFirstPage() {
        return this.firstPage;
    }

    public int getLastPage() {
        return this.lastPage;
    }

    public int getNextPage() {
        return this.nextPage;
    }

    public int getPrevPage() {
        return this.prevPage;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public boolean getHasNext() {
        return this.hasNext;
    }

    public boolean getHasPrev() {
        return this.hasPrev;
    }

    public boolean getHasFirst() {
        return this.hasFirst;
    }

    public boolean getHasLast() {
        return this.hasLast;
    }

    public int getButtonCount() {
        return 5;
    }

    public int offset() {
        return this.currentPage > 0 ? this.pageSize * (this.currentPage - 1) : 0;
    }

    public int getSwitchPoint() {
        int switchPoint = this.totalPage - 2;
        if (switchPoint <= 5) {
            switchPoint = 5;
        }

        return switchPoint;
    }
}