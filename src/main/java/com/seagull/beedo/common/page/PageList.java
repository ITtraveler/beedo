/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.common.page;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author guosheng.huang
 * @version $id:PageList.java, v 0.1 2018年08月12日 14:50 tao.hu Exp $
 */
public final class PageList<T> implements Serializable {
    private static final long serialVersionUID = 3066885677031551650L;
    private final List<T> datas;
    private final Page page;

    private PageList(List<T> datas, Page page) {
        if (null == datas) {
            datas = new LinkedList();
        }

        if (null == page) {
            page = Page.getZeroRecordPage();
        }

        this.datas = (List)datas;
        this.page = page;
    }

    public static <T> PageList getEmptyInstance() {
        return new PageList((List)null, (Page)null);
    }

    public static <T> PageList<T> getInstance(List<T> datas, Page page) {
        if (null == datas) {
            throw new RuntimeException("业务数据列表不能为空！");
        } else if (null == page) {
            throw new RuntimeException("分页对象page不能为空！");
        } else {
            return new PageList(datas, page);
        }
    }

    public List<T> getDatas() {
        return this.datas;
    }

    public Page getPage() {
        return this.page;
    }
}
