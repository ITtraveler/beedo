/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.web.controller;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.component.TaskParseComponent;
import com.seagull.beedo.model.TaskParseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.seagull.common.base.common.page.PageList;
import team.seagull.common.base.common.page.PageQueryResultConvert;
import team.seagull.common.base.query.QueryBase;
import team.seagull.common.base.result.CommonResult;
import team.seagull.common.base.result.PageListResult;
import team.seagull.common.base.utils.StringUtils;

/**
 * @author guosheng.huang
 * @version $id:BeedoTaskController.java, v 0.1 2018年08月13日 20:56 guosheng.huang Exp $
 */
@RestController
@RequestMapping("/task")
public class BeedoTaskController extends BaseController {

    @Autowired
    private TaskParseComponent taskParseComponent;

    /**
     * 保存Task
     *
     * @param taskParseInfo
     * @return
     */
    @PostMapping
    public Object saveParseTask(@RequestBody TaskParseInfo taskParseInfo) {
        CommonResult<String> result = new CommonResult<>();
        taskParseComponent.saveTask(taskParseInfo);
        return result;
    }

    /**
     * 根据uid获取Task
     *
     * @param uid
     * @return
     */
    @GetMapping("{uid}")
    public Object getParseTaskById(@PathVariable String uid) {
        CommonResult<TaskParseInfo> result = new CommonResult<>();
        TaskParseInfo taskParseInfo = taskParseComponent.getTaskByUid(uid);
        result.setData(taskParseInfo);
        return result;
    }

    /**
     * 分页获取Task
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/page/{page}/size/{size}")
    public Object getParseTasks(@PathVariable int page, @PathVariable int size) {
        PageListResult<TaskParseInfo> result = new PageListResult<>();
        PageList<TaskParseInfo> pageList = taskParseComponent.getTaskPage(new QueryBase(page,
                size));
        PageQueryResultConvert.converToResult(result, pageList);
        result.setCurrentPage(page);
        return result;
    }

    /**
     * 修改Task及Element
     *
     * @param uid
     * @param taskParseInfo
     * @return
     */
    @PutMapping("{uid}")
    public Object putParseTask(@PathVariable String uid, @RequestBody TaskParseInfo taskParseInfo) {
        CommonResult<String> result = new CommonResult<>();
        if (!StringUtils.isEquals(uid, taskParseInfo.getUid())) {
            retFail(result);
            return result;
        }

        boolean update = taskParseComponent.updateTask(taskParseInfo);
        if (!update) {
            retFail(result);
        }
        return result;
    }

    /**
     * 更新任务状态
     * @param uid
     * @param status
     * @return
     */
    @PutMapping("{uid}/status/{status}")
    public Object changeStatus(@PathVariable String uid, @PathVariable String status) {
        CommonResult<String> result = new CommonResult<>();

        TaskStatusEnum taskStatusEnum = TaskStatusEnum.codeOf(status);
        if (taskStatusEnum == null) {
            retFail(result, "状态有误");
            return result;
        }

        boolean update = taskParseComponent.updateTaskStatus(uid, taskStatusEnum);
        if (!update) {
            retFail(result);
        }
        return result;

    }

    /**
     * 删除Task
     *
     * @param uid
     * @return
     */
    @DeleteMapping("{uid}")
    public Object deleteParseTask(@PathVariable String uid) {
        CommonResult<String> result = new CommonResult<>();
        taskParseComponent.deleteTaskByUid(uid);
        return result;
    }

}
