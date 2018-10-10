/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component;

import com.alibaba.fastjson.JSON;
import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.common.query.TaskQuery;
import com.seagull.beedo.dao.TaskNodeDao;
import com.seagull.beedo.dao.TaskParseDao;
import com.seagull.beedo.dao.domain.TaskNodeDO;
import com.seagull.beedo.dao.domain.TaskParseDO;
import com.seagull.beedo.model.TaskElementInfo;
import com.seagull.beedo.model.TaskNodeInfo;
import com.seagull.beedo.model.TaskParseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team.seagull.common.base.common.page.Page;
import team.seagull.common.base.common.page.PageAttribute;
import team.seagull.common.base.common.page.PageList;
import team.seagull.common.base.utils.CollectionUtils;
import team.seagull.common.base.utils.RandomUtils;
import team.seagull.common.base.utils.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务解析组件
 *
 * @author guosheng.huang
 * @version $id:TaskParseComponent.java, v 0.1 2018年08月13日 21:13 guosheng.huang Exp $
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

        if (StringUtils.isBlank(taskParseDO.getCollectionName())) {
            taskParseDO.setCollectionName(RandomUtils.getUUID());
        }

        taskParseDO.setTaskStatus(TaskStatusEnum.INIT);
        TaskParseDO saveTask = taskParseDao.save(taskParseDO);
        List<TaskNodeInfo> nodes = taskParseInfo.getParseNodes();
        nodes.forEach(taskNodeInfo -> {
            TaskNodeDO taskNodeDO = new TaskNodeDO();
            taskNodeDO.setDocumentId(taskNodeInfo.getDocumentId());
            taskNodeDO.setTaskParseUid(saveTask.getUid());
            taskNodeDO.setElementInfoMap(JSON.toJSONString(taskNodeInfo.getElementInfoMap()));
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
    @Cacheable(value = "taskParseInfo", key = "#uid")
    public TaskParseInfo getTaskByUid(String uid) {
        TaskParseInfo taskParseInfo = new TaskParseInfo();
        //Task
        TaskParseDO taskParseDO = taskParseDao.findByUid(uid);
        BeanUtils.copyProperties(taskParseDO, taskParseInfo);

        //node
        List<TaskNodeDO> taskNodeDOS = taskNodeDao.findByTaskParseUid(taskParseInfo.getUid());
        if (!CollectionUtils.isEmpty(taskNodeDOS)) {
            taskParseInfo.setParseNodes(taskNodeDoToInfo(taskNodeDOS));
        }

        return taskParseInfo;
    }

    /**
     * 分页获取Task
     *
     * @param query
     * @return
     */
    public PageList<TaskParseInfo> getTaskPage(TaskQuery query) {
        TaskParseDO parseDO = new TaskParseDO();
        parseDO.setTaskStatus(query.getTaskStatus());
        Example<TaskParseDO> example = Example.of(parseDO);
        org.springframework.data.domain.Page<TaskParseDO> TaskParsePage = taskParseDao.findAll
                (example, PageRequest.of(query.pageNum - 1, query.getPageSize()));
        Iterator<TaskParseDO> iterator = TaskParsePage.iterator();
        ArrayList<TaskParseInfo> taskParseInfoList = new ArrayList<>();

        //转换处理
        while (iterator.hasNext()) {
            TaskParseDO taskParseDO = iterator.next();
            TaskParseInfo taskParseInfo = new TaskParseInfo();
            BeanUtils.copyProperties(taskParseDO, taskParseInfo);
            taskParseInfoList.add(taskParseInfo);

            //Task对应的所有node
            List<TaskNodeDO> taskNodeDOS = taskNodeDao.findByTaskParseUid(taskParseInfo.getUid());
            if (!CollectionUtils.isEmpty(taskNodeDOS)) {
                taskParseInfo.setParseNodes(taskNodeDoToInfo(taskNodeDOS));
            }
        }

        int count = (int) taskParseDao.count();
        Page page = Page.getInstance(new PageAttribute(query.getPageNum(), query.getPageSize()), count);
        PageList<TaskParseInfo> pageList = PageList.getInstance(
                taskParseInfoList, page);
        return pageList;
    }

    /**
     * 更新操作直接从缓存删除即可，不做@CachePut
     *
     * @param taskParseInfo
     * @return
     */
    @CacheEvict(value = "taskParseInfo", key = "#taskParseInfo.uid",
            condition = "#taskParseInfo.uid != null")
    @Transactional
    public boolean updateTask(TaskParseInfo taskParseInfo) {
        if (taskParseInfo.getId() == null || taskParseInfo.getId() <= 0) {
            return false;
        }

        //更新task信息
        TaskParseDO taskParseDO = new TaskParseDO();
        BeanUtils.copyProperties(taskParseInfo, taskParseDO);

        if (StringUtils.isBlank(taskParseDO.getCollectionName())) {
            taskParseDO.setCollectionName(RandomUtils.getUUID());
        }

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
            taskNodeDO.setElementInfoMap(JSON.toJSONString(taskNodeInfo.getElementInfoMap()));
            taskNodeDao.save(taskNodeDO);
        });
        return true;
    }


    @CacheEvict(value = "taskParseInfo", key = "#uid", condition = "#uid != null")
    @Transactional
    public boolean updateTaskStatus(String uid, TaskStatusEnum statusEnum) {
        if (statusEnum == null) {
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


    /**
     * do -> info
     *
     * @param taskNodeDOS
     * @return taskNodeInfos
     */
    private List<TaskNodeInfo> taskNodeDoToInfo(List<TaskNodeDO> taskNodeDOS) {
        List<TaskNodeInfo> taskNodeInfos = new ArrayList<>();
        for (TaskNodeDO taskNodeDO : taskNodeDOS) {
            TaskNodeInfo taskNodeInfo = new TaskNodeInfo();
            BeanUtils.copyProperties(taskNodeDO, taskNodeInfo);
            LinkedHashMap<Object, TaskElementInfo> elementInfoMap = JSON.parseObject(taskNodeDO.getElementInfoMap(),
                    LinkedHashMap.class);
            //对象变成hashMap转换问题处理
            for (Map.Entry<Object, TaskElementInfo> entry : elementInfoMap.entrySet()) {
                elementInfoMap.put(entry.getKey(), JSON.parseObject(JSON.toJSONString(entry.getValue()),
                        TaskElementInfo.class));
            }
            taskNodeInfo.setElementInfoMap(elementInfoMap);
            taskNodeInfos.add(taskNodeInfo);
        }
        return taskNodeInfos;
    }
}
