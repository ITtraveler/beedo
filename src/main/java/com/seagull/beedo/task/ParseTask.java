/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.task;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.common.query.TaskQuery;
import com.seagull.beedo.core.ParseCoolExecute;
import com.seagull.beedo.component.TaskParseComponent;
import com.seagull.beedo.model.TaskParseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.seagull.common.base.common.page.PageList;
import team.seagull.common.base.query.QueryBase;

import java.util.Arrays;

/**
 * 任务总执行器，每30s触发一次，redis记录各个ParseTask执行状态
 *
 * @author guosheng.huang
 * @version $id:TaskExecuteComponent.java, v 0.1 2018年08月11日 21:23 guosheng.huang Exp $
 */
@Component
public class ParseTask extends BaseTask {

    @Autowired
    private ParseCoolExecute parseToolExecute;

    @Autowired
    private TaskParseComponent taskParseComponent;

    @Scheduled(cron = "30 * * * * ?")
    @Override
    public void exec() {
        TaskQuery query = new TaskQuery();
        query.setPageNum(1);
        query.setPageSize(200);
        query.setTaskStatus(TaskStatusEnum.VALID);

        PageList<TaskParseInfo> taskPage0 = taskParseComponent.getTaskPage(query);
        query.setTaskStatus(TaskStatusEnum.CLOSE);
        PageList<TaskParseInfo> taskPage1 = taskParseComponent.getTaskPage(query);
        taskPage0.getDatas().addAll(taskPage1.getDatas());
        if (taskPage0 != null && taskPage0.getDatas().size() > 0) {
            parseToolExecute.parseCool(taskPage0.getDatas());
        }
    }
}
