/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.task;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.common.query.TaskParseQuery;
import com.seagull.beedo.core.ParseCoolExecute;
import com.seagull.beedo.model.BeedoTaskParseModel;
import com.seagull.beedo.service.TaskParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.seagull.common.base.common.page.PageList;

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
    private TaskParseService taskParseService;

    @Scheduled(cron = "30 * * * * ?")
    @Override
    public void exec() {
        TaskParseQuery query = new TaskParseQuery();
        query.setPageNum(1);
        query.setPageSize(200);
        query.setTaskStatus(TaskStatusEnum.VALID.getCode());

        PageList<BeedoTaskParseModel> taskPage = taskParseService.getTaskPage(query);
        query.setTaskStatus(TaskStatusEnum.CLOSE.getCode());
        PageList<BeedoTaskParseModel> taskPage1 = taskParseService.getTaskPage(query);
        taskPage.getDatas().addAll(taskPage1.getDatas());
        if (taskPage.getDatas().size() > 0) {
            parseToolExecute.parseCool(taskPage.getDatas());
        }
    }
}
