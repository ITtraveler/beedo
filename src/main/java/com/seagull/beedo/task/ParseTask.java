/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.task;

import com.seagull.beedo.component.ParseCoolExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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

    @Override
    public void exec() {

        parseToolExecute.parseCool(new ArrayList<>());
    }
}
