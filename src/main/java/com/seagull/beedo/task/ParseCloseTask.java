/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.task;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.common.query.TaskParseQuery;
import com.seagull.beedo.model.BeedoTaskParseModel;
import com.seagull.beedo.service.TaskParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import team.seagull.common.base.common.page.PageList;
import team.seagull.common.base.utils.CollectionUtils;

import java.util.Map;

/**
 * 任务总执行器，每30s触发一次，redis记录各个ParseTask执行状态
 *
 * @author guosheng.huang
 * @version $id:TaskExecuteComponent.java, v 0.1 2018年08月11日 21:23 guosheng.huang Exp $
 */
@Component
public class ParseCloseTask extends BaseTask {
    Logger logger = LoggerFactory.getLogger(ParseCloseTask.class);

    @Autowired
    private TaskParseService taskParseService;


    @Autowired
    private Map schedulerMap;


    @Scheduled(cron = "0/10 * * * * ?")
    @Override
    public void exec() {
        TaskParseQuery query = new TaskParseQuery();
        query.setPageNum(1);
        query.setPageSize(200);
        query.setLevel(0);
        query.setTaskStatus(TaskStatusEnum.CLOSE.getCode());
        PageList<BeedoTaskParseModel> closeTaskPage = taskParseService.getTaskPage(query);
        if (CollectionUtils.isEmpty(closeTaskPage.getDatas())) {
            return;
        }

        //关闭任务
        for (BeedoTaskParseModel taskParseModel : closeTaskPage.getDatas()) {
            ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) schedulerMap.get(taskParseModel.getUid());
            if (scheduler == null) {
                //更新状态为INIT
                taskParseService.updateTaskStatus(taskParseModel.getUid(), TaskStatusEnum.INIT);
                continue;
            }
            scheduler.shutdown();
            schedulerMap.remove(taskParseModel.getUid());
            logger.info("关闭定时任务，taskParseModel：{}", taskParseModel);

            //更新状态为INIT
            taskParseService.updateTaskStatus(taskParseModel.getUid(), TaskStatusEnum.INIT);
            return;
        }

    }
}
