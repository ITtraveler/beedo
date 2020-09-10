/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.common.page;

import java.io.Serializable;

/**
 * @author guosheng.huang
 * @version $id:PageAttribute.java, v 0.1 2018年08月12日 14:51 tao.hu Exp $
 */
public final class PageAttribute implements Serializable {
    private static final long serialVersionUID = 5525113704096564323L;
    public static final int DEFAULT_PAGE_INDEX = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int BOX_PAGE_SIZE = 5;
    public static final String PAGE_TAG_KEY = "page";
    private int pageIndex = 1;
    private int pageSize;
    private int begin;

    public PageAttribute(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex != null && pageIndex.intValue() > 0 ? pageIndex.intValue() : 1;
        this.pageSize = pageSize != null && pageSize.intValue() > 0 ? pageSize.intValue() : 10;
        this.begin = (this.pageIndex - 1) * this.pageSize;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getBegin() {
        return this.begin;
    }

    public int getEnd() {
        return this.begin + this.pageSize;
    }
}