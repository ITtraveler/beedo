/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.common.page;


import com.seagull.beedo.common.result.PageListResult;

/**
 * @author guosheng.huang
 * @version $id:PageQueryResultConvert.java, v 0.1 2018年08月12日 14:48 tao.hu Exp $
 */
public class PageQueryResultConvert {
    public static void converToResult(PageListResult result, PageList pageList) {
        result.setCurrentPage(pageList.getPage().getCurrentPage());
        result.setTotalPage(pageList.getPage().getTotalPage());
        result.setItemSize(pageList.getPage().getPageSize());
        result.setTotalSize(pageList.getPage().getRowCount());
        result.setDataList(pageList.getDatas());
    }
}
