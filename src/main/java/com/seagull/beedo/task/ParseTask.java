/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.task;

import com.seagull.beedo.component.ParseCoolExecute;
import com.seagull.beedo.component.TaskParseComponent;
import com.seagull.beedo.model.TaskParseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.seagull.common.base.common.page.PageList;
import team.seagull.common.base.query.QueryBase;

/**
 * 任务总执行器，每30s触发一次，redis记录各个ParseTask执行状态
 *
 * @author guosheng.huang
 * @version $id:TaskExecuteComponent.java, v 0.1 2018年08月11日 21:23 tao.hu Exp $
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
        QueryBase query = new QueryBase();
        query.setPageNum(1);
        query.setPageSize(20);

        PageList<TaskParseInfo> taskPage = taskParseComponent.getTaskPage(query);
        if(taskPage != null && taskPage.getDatas().size() > 0){
            parseToolExecute.parseCool(taskPage.getDatas());
        }
    }
}
