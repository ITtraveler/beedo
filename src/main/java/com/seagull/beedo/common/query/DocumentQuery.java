/**
 * yingyinglicai.com Inc.
 * Copyright (c) 2013-2018 All Rights Reserved.
 */
package com.seagull.beedo.common.query;

import com.seagull.beedo.common.query.base.QueryDate;

import lombok.Data;

/**
 * 文档查询
 * @author guosheng.huang
 * @version $Id: DocumentQuery, v1.0 2018年12月07日 14:03 guosheng.huang Exp $
 */
@Data
public class DocumentQuery extends QueryDate {
    private static final long serialVersionUID = 3369756536309516269L;

    private String            name;

    private String            url;

    /**
     * CommonStatusEnum
     */
    private String            status;
}
