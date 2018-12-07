/**
 * yingyinglicai.com Inc.
 * Copyright (c) 2013-2018 All Rights Reserved.
 */
package com.seagull.beedo.common.query;

import com.seagull.beedo.common.query.base.QueryDate;

import lombok.Data;

/**
 * @author guosheng.huang
 * @version $Id: TaskNodeQuery, v1.0 2018年12月07日 14:04 guosheng.huang Exp $
 */
@Data
public class TaskNodeQuery extends QueryDate {
    private static final long serialVersionUID = 8854165981625477763L;
    private Integer           id;

    /**
     * 文档Id
     */
    private Integer           documentId;

    /**
     * 解析任务Id
     */
    private String            taskParseUid;

    /**
     * 状态
     * CommonStatusEnum
     */
    private String            status;
}
