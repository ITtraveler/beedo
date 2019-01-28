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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.seagull.common.base.common.page.PageList;
import team.seagull.common.base.utils.CollectionUtils;

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

    @Scheduled(cron = "0/30 * * * * ?")
    @Override
    public void exec() {
        TaskParseQuery query = new TaskParseQuery();
        query.setPageNum(1);
        query.setPageSize(200);
        query.setLevel(0);
        query.setTaskStatus(TaskStatusEnum.VALID.getCode());
        PageList<BeedoTaskParseModel> validTaskPage = taskParseService.getTaskPage(query);
        if (!CollectionUtils.isEmpty(validTaskPage.getDatas())) {
            parseToolExecute.parseCool(validTaskPage.getDatas());
        }

        //被修改到数据
        query.setTaskStatus(TaskStatusEnum.MODIFIED.getCode());
        PageList<BeedoTaskParseModel> modifiedTaskPage = taskParseService.getTaskPage(query);
        if (!CollectionUtils.isEmpty(modifiedTaskPage.getDatas())) {
            parseToolExecute.parseCool(modifiedTaskPage.getDatas());
        }
    }
}
