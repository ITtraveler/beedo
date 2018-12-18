package com.seagull.beedo.common.query.base;

import team.seagull.common.base.common.ToString;

/**
 * @author guosheng.huang
 * @version $Id: QueryBase, v1.0 2018年12月07日 14:12 guosheng.huang Exp $
 */
public class QueryBase extends ToString {
    private static final long serialVersionUID = 1945863197314141133L;
    public int                pageNum          = 1;

    public int                pageSize         = 15;

    public QueryBase() {
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}