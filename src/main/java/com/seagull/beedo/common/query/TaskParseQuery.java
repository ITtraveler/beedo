/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.common.query;

import com.seagull.beedo.common.query.base.QueryDate;

import lombok.Data;

/**
 * @author guosheng.huang
 * @version TaskParseQuery.java, v 0.1 2018年09月30日 23:25 guosheng.huang Exp $
 */
@Data
public class TaskParseQuery extends QueryDate {
    private static final long serialVersionUID = 287277100858476175L;

    private Integer id;

    private String uid;


    /**
     * TaskStatusEnum
     */
    private String taskStatus;

    /**
     * 集合名
     */
    private String collectionName;

    /**
     * 任务等级
     */
    private Integer level;

    /**
     * 状态
     * CommonStatusEnum
     */
    private String status;
}
