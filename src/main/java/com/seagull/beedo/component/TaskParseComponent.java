/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component;

import com.alibaba.fastjson.JSON;
import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.dao.TaskNodeDao;
import com.seagull.beedo.dao.TaskParseDao;
import com.seagull.beedo.dao.dataobject.TaskNodeDO;
import com.seagull.beedo.dao.dataobject.TaskParseDO;
import com.seagull.beedo.model.TaskNodeInfo;
import com.seagull.beedo.model.TaskParseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team.seagull.common.base.common.page.Page;
import team.seagull.common.base.common.page.PageAttribute;
import team.seagull.common.base.common.page.PageList;
import team.seagull.common.base.query.QueryBase;
import team.seagull.common.base.utils.RandomUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author guosheng.huang
 * @version $id:TaskParseComponent.java, v 0.1 2018年08月13日 21:13 tao.hu Exp $
 */
@Component
public class TaskParseComponent {
    /**
     * logger
     */
    Logger logger = LoggerFactory.getLogger(TaskParseComponent.class);

    @Autowired
    private TaskNodeDao taskNodeDao;

    @Autowired
    private TaskParseDao taskParseDao;

    /**
     * 保存Task 及其 element
     *
     * @param taskParseInfo
     */
    @Transactional
    public void saveTask(TaskParseInfo taskParseInfo) {
        //设置uid
        taskParseInfo.setUid(RandomUtils.getUUID());
        TaskParseDO taskParseDO = new TaskParseDO();
        BeanUtils.copyProperties(taskParseInfo, taskParseDO);
        taskParseDO.setTaskStatus(TaskStatusEnum.INIT);
        TaskParseDO saveTask = taskParseDao.save(taskParseDO);
        List<TaskNodeInfo> nodes = taskParseInfo.getParseNodes();
        nodes.forEach(taskNodeInfo -> {
            TaskNodeDO taskNodeDO = new TaskNodeDO();
            taskNodeDO.setDocumentId(taskNodeInfo.getDocumentId());
            taskNodeDO.setTaskParseUid(saveTask.getUid());
            taskNodeDO.setElementMap(JSON.toJSONString(taskNodeInfo.getElementIds()));
            taskNodeDao.save(taskNodeDO);
        });

        logger.info(MessageFormat.format("保存解析的文档数据成功,TaskParseInfo:{0}", taskParseInfo));
    }

    /**
     * id获取Task
     *
     * @param uid
     * @return
     */
    public TaskParseInfo getTaskByUid(String uid) {
        TaskParseInfo taskParseInfo = new TaskParseInfo();
        //Task
        TaskParseDO taskParseDO = taskParseDao.findByUid(uid);
        BeanUtils.copyProperties(taskParseDO, taskParseInfo);

        //node
        List<TaskNodeInfo> taskNodeInfos = new ArrayList<>();
        taskParseInfo.setParseNodes(taskNodeInfos);
        List<TaskNodeDO> taskNodeDOS = taskNodeDao.findByTaskParseUid(taskParseInfo.getUid());
        taskNodeDOS.forEach(taskNodeDO -> {
            TaskNodeInfo taskNodeInfo = new TaskNodeInfo();
            BeanUtils.copyProperties(taskNodeDO, taskNodeInfo);
            taskNodeInfo.setElementIds(JSON.parseObject(taskNodeDO.getElementMap(), HashMap.class));
        });

        return taskParseInfo;
    }

    /**
     * 分页获取Task
     *
     * @param queryBase
     * @return
     */
    public PageList<TaskParseInfo> getTaskPage(QueryBase queryBase) {
        org.springframework.data.domain.Page<TaskParseDO> TaskParsePage = taskParseDao.findAll
                (PageRequest.of(queryBase.pageNum-1, queryBase.getPageSize()));
        Iterator<TaskParseDO> iterator = TaskParsePage.iterator();
        ArrayList<TaskParseInfo> taskParseInfoList = new ArrayList<>();

        //转换处理
        while (iterator.hasNext()) {
            TaskParseDO taskParseDO = iterator.next();
            TaskParseInfo taskParseInfo = new TaskParseInfo();
            BeanUtils.copyProperties(taskParseDO, taskParseInfo);
            taskParseInfoList.add(taskParseInfo);

            //Task对应的所有node
            List<TaskNodeInfo> taskNodeInfoList = new ArrayList<>();
            List<TaskNodeDO> taskNodeDOList = taskNodeDao.findByTaskParseUid(taskParseDO.getUid());
            taskNodeDOList.forEach(taskNodeDO -> {
                TaskNodeInfo taskNodeInfo = new TaskNodeInfo();
                BeanUtils.copyProperties(taskNodeDO, taskNodeInfo);
                taskNodeInfo.setElementIds(JSON.parseObject(taskNodeDO.getElementMap(), HashMap.class));
                taskNodeInfoList.add(taskNodeInfo);
            });

            taskParseInfo.setParseNodes(taskNodeInfoList);
        }

        int count = (int) taskParseDao.count();
        Page page = Page.getInstance(new PageAttribute(queryBase.getPageNum(), queryBase.getPageSize()), count);
        PageList<TaskParseInfo> pageList = PageList.getInstance(
                taskParseInfoList, page);
        return pageList;
    }

    @Transactional
    public boolean updateTask(TaskParseInfo taskParseInfo) {
        if (taskParseInfo.getId() == null || taskParseInfo.getId() <= 0) {
            return false;
        }

        //更新task信息
        TaskParseDO taskParseDO = new TaskParseDO();
        BeanUtils.copyProperties(taskParseInfo, taskParseDO);
        TaskParseDO result = taskParseDao.save(taskParseDO);
        if (result == null) {
            logger.error(MessageFormat.format("更新解析的文档数据失败,TaskParseInfo:{0}", taskParseInfo));
            return false;
        }

        //删除task节点信息
        taskNodeDao.deleteByTaskParseUid(taskParseInfo.getUid());

        //task新的task节点
        List<TaskNodeInfo> nodes = taskParseInfo.getParseNodes();
        nodes.forEach(taskNodeInfo -> {
            TaskNodeDO taskNodeDO = new TaskNodeDO();
            taskNodeDO.setDocumentId(taskNodeInfo.getDocumentId());
            taskNodeDO.setTaskParseUid(taskParseInfo.getUid());
            taskNodeDO.setElementMap(JSON.toJSONString(taskNodeInfo.getElementIds()));
            taskNodeDao.save(taskNodeDO);
        });
        return true;
    }

    @Transactional
    public boolean updateTaskStatus(String uid,TaskStatusEnum statusEnum){
        if(statusEnum == null){
            logger.error("更新解析的文档状态失败，状态为空");
            return false;
        }

        //更新
        TaskParseDO taskParseDO = taskParseDao.findByUidForUpdate(uid);
        taskParseDO.setTaskStatus(statusEnum);
        taskParseDao.save(taskParseDO);
        return true;
    }

    @Transactional
    public void deleteTaskByUid(String uid) {
        taskParseDao.deleteByUid(uid);
        taskNodeDao.deleteByTaskParseUid(uid);
        logger.info(MessageFormat.format("删除解析的任务数据成功,TaskUid:{0}", uid));
    }

    @Transactional
    public void deleteTaskNodeById(Integer nodeId) {
        taskNodeDao.deleteById(nodeId);
        logger.info(MessageFormat.format("删除任务的解析节点数据成功,nodeId:{0}", nodeId));
    }

}
