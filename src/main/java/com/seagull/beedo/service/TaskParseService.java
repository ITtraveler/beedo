package com.seagull.beedo.service;

import org.springframework.transaction.annotation.Transactional;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.common.query.TaskParseQuery;
import com.seagull.beedo.model.BeedoTaskParseModel;

import team.seagull.common.base.common.page.PageList;

/**
 * @author guosheng.huang
 * @version $Id: TaskParseService, v1.0 2018年12月07日 15:13 guosheng.huang Exp $
 */
public interface TaskParseService {
    /**
     * 保存Task 及其 element
     * @param taskParseModel
     */
    @Transactional(rollbackFor = Exception.class)
    void saveTask(BeedoTaskParseModel taskParseModel);

    BeedoTaskParseModel getTaskByUid(String uid);

    PageList<BeedoTaskParseModel> getTaskPage(TaskParseQuery query);

    /**
     * 更新操作任务
     * @param taskParseInfo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateTask(BeedoTaskParseModel taskParseInfo);

    @Transactional(rollbackFor = Exception.class)
    void updateTaskStatus(String uid, TaskStatusEnum statusEnum);

    @Transactional(rollbackFor = Exception.class)
    void deleteTaskByUid(String uid);

    @Transactional(rollbackFor = Exception.class)
    void deleteTaskNodeById(Integer nodeId);
}
