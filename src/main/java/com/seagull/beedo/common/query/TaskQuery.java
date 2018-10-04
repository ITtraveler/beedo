/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.common.query;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import lombok.Data;
import team.seagull.common.base.query.QueryBase;

/**
 * @author guosheng.huang
 * @version TaskQuery.java, v 0.1 2018年09月30日 23:25 guosheng.huang Exp $
 */
@Data
public class TaskQuery extends QueryBase {
    private TaskStatusEnum taskStatus;

    public TaskQuery() {
    }

    public TaskQuery(int pageNum, int pageSize) {
        super(pageNum, pageSize);
    }
}
