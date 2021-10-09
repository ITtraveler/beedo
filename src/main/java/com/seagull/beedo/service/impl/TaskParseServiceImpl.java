package com.seagull.beedo.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.common.enums.TaskTypeEnum;
import com.seagull.beedo.common.page.Page;
import com.seagull.beedo.common.page.PageAttribute;
import com.seagull.beedo.common.page.PageList;
import com.seagull.beedo.common.query.TaskParseQuery;
import com.seagull.beedo.common.utils.RandomUtils;
import com.seagull.beedo.component.mysql.TaskNodeComponent;
import com.seagull.beedo.component.mysql.TaskParseComponent;
import com.seagull.beedo.dao.domain.BeedoTaskNode;
import com.seagull.beedo.dao.domain.BeedoTaskParse;
import com.seagull.beedo.model.BeedoTaskNodeModel;
import com.seagull.beedo.model.BeedoTaskParseModel;
import com.seagull.beedo.model.TaskElementInfo;
import com.seagull.beedo.service.TaskParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guosheng.huang
 * @version $Id: TaskParseServiceImpl, v1.0 2018年12月07日 15:14 guosheng.huang Exp $
 */
@Slf4j
@Service
public class TaskParseServiceImpl implements TaskParseService {

    @Autowired
    private TaskNodeComponent taskNodeComponent;

    @Autowired
    private TaskParseComponent taskParseComponent;

    @Override
    public void saveTask(BeedoTaskParseModel taskParseModel) {
        //设置uid
        taskParseModel.setUid(RandomUtils.getUUID());

        BeedoTaskParse beedoTaskParse = new BeedoTaskParse();
        BeanUtils.copyProperties(taskParseModel, beedoTaskParse);
        beedoTaskParse.setType(taskParseModel.getType().getCode());

        if (StringUtils.isBlank(beedoTaskParse.getCollectionName())) {
            int randomInt = RandomUtils.getRandomInt(6);
            beedoTaskParse.setCollectionName(""+randomInt);
            beedoTaskParse.setDbname(""+randomInt);
        }else{
            beedoTaskParse.setCollectionName(taskParseModel.getCollectionName());
            beedoTaskParse.setDbname(taskParseModel.getCollectionName());
        }

        beedoTaskParse.setTaskStatus(TaskStatusEnum.INIT.getCode());
        BeedoTaskParse taskParse = taskParseComponent.insert(beedoTaskParse);

        List<BeedoTaskNodeModel> nodes = taskParseModel.getParseNodes();
        insertTaskNode(nodes, taskParse.getUid());
    }

    /**
     * id获取Task
     *
     * @param uid
     * @return
     */
    @Override
    public BeedoTaskParseModel getTaskByUid(String uid) {
        BeedoTaskParseModel taskParseInfo = new BeedoTaskParseModel();
        //Task
        BeedoTaskParse beedoTaskParse = taskParseComponent.queryByUid(uid);
        BeanUtils.copyProperties(beedoTaskParse, taskParseInfo);
        taskParseInfo.setTaskStatus(TaskStatusEnum.codeOf(beedoTaskParse.getTaskStatus()));
        taskParseInfo.setType(TaskTypeEnum.codeOf(beedoTaskParse.getType()));
        //node
        List<BeedoTaskNode> taskNodes = taskNodeComponent
                .queryListByTaskParseUid(taskParseInfo.getUid());
        if (!CollectionUtils.isEmpty(taskNodes)) {
            taskParseInfo.setParseNodes(taskNodeDoToInfo(taskNodes));
        }

        return taskParseInfo;
    }

    /**
     * 分页获取Task
     *
     * @param query
     * @return
     */
    @Override
    public PageList<BeedoTaskParseModel> getTaskPage(TaskParseQuery query) {

        PageInfo<BeedoTaskParse> taskParsePageInfo = taskParseComponent.queryForPage(query);

        List<BeedoTaskParseModel> taskParseInfoList = new ArrayList<>();
        //转换处理
        for (BeedoTaskParse beedoTaskParse : taskParsePageInfo.getList()) {
            BeedoTaskParseModel taskParseInfo = new BeedoTaskParseModel();
            BeanUtils.copyProperties(beedoTaskParse, taskParseInfo);
            taskParseInfo.setTaskStatus(TaskStatusEnum.codeOf(beedoTaskParse.getTaskStatus()));
            taskParseInfo.setType(TaskTypeEnum.codeOf(beedoTaskParse.getType()));

            //Task对应的所有node
            List<BeedoTaskNode> taskNodes = taskNodeComponent
                    .queryListByTaskParseUid(taskParseInfo.getUid());
            if (!CollectionUtils.isEmpty(taskNodes)) {
                taskParseInfo.setParseNodes(taskNodeDoToInfo(taskNodes));
            }
            taskParseInfoList.add(taskParseInfo);
        }

        Page page = Page.getInstance(new PageAttribute(query.getPageNum(), query.getPageSize()),
                (int) taskParsePageInfo.getTotal());
        PageList<BeedoTaskParseModel> pageList = PageList.getInstance(taskParseInfoList, page);
        return pageList;
    }

    @Override
    public boolean updateTask(BeedoTaskParseModel taskParseModel) {
        if (taskParseModel.getId() == null || taskParseModel.getId() <= 0) {
            return false;
        }

        //更新task信息
        BeedoTaskParse taskParse = new BeedoTaskParse();
        BeanUtils.copyProperties(taskParseModel, taskParse);
        // TODO: 2019/1/28 临时存放，后期放到单独字段中
        if (taskParseModel.getTaskStatus() == null || TaskStatusEnum.VALID == taskParseModel.getTaskStatus()) {
            taskParse.setTaskStatus(TaskStatusEnum.MODIFIED.getCode());
        }

        taskParse.setType(taskParseModel.getType().getCode());
        if (StringUtils.isBlank(taskParse.getCollectionName())) {
            taskParse.setCollectionName(RandomUtils.getUUID());
        }
        taskParseComponent.update(taskParse);

        deleteTaskNodes(taskParseModel.getUid());

        //task新的task节点
        List<BeedoTaskNodeModel> nodes = taskParseModel.getParseNodes();
        insertTaskNode(nodes, taskParseModel.getUid());
        return true;
    }

    @Override
    public void updateTaskStatus(String uid, TaskStatusEnum statusEnum) {
        Assert.isTrue(statusEnum != null, "更新任务状态失败，状态为空");
        //更新
        BeedoTaskParse beedoTaskParse = new BeedoTaskParse();
        beedoTaskParse.setUid(uid);
        beedoTaskParse.setTaskStatus(statusEnum.getCode());
        taskParseComponent.update(beedoTaskParse);
    }

    @Override
    public void deleteTaskByUid(String uid) {
        taskParseComponent.deleteByUid(uid);
        deleteTaskNodes(uid);
    }

    @Override
    public void deleteTaskNodeById(Integer nodeId) {
        taskNodeComponent.deleteById(nodeId);
    }

    /**
     * do -> info
     *
     * @param beedoTaskNodes
     * @return taskNodeInfos
     */
    private List<BeedoTaskNodeModel> taskNodeDoToInfo(List<BeedoTaskNode> beedoTaskNodes) {
        List<BeedoTaskNodeModel> taskNodeInfos = new ArrayList<>();
        for (BeedoTaskNode taskNode : beedoTaskNodes) {
            BeedoTaskNodeModel taskNodeInfo = new BeedoTaskNodeModel();
            BeanUtils.copyProperties(taskNode, taskNodeInfo);
            LinkedHashMap<Object, TaskElementInfo> elementInfoMap = JSON
                    .parseObject(taskNode.getElementInfoMap(), LinkedHashMap.class);
            //对象变成hashMap转换问题处理
            for (Map.Entry<Object, TaskElementInfo> entry : elementInfoMap.entrySet()) {
                elementInfoMap.put(entry.getKey(),
                        JSON.parseObject(JSON.toJSONString(entry.getValue()), TaskElementInfo.class));
            }
            taskNodeInfo.setElementInfoMap(elementInfoMap);
            taskNodeInfos.add(taskNodeInfo);
        }
        return taskNodeInfos;
    }

    /**
     * 保存任务节点
     *
     * @param nodes
     * @param uid
     */
    private void insertTaskNode(List<BeedoTaskNodeModel> nodes, String uid) {
        nodes.forEach(taskNodeInfo -> {
            BeedoTaskNode taskNodeDO = new BeedoTaskNode();
            taskNodeDO.setDocumentId(taskNodeInfo.getDocumentId());
            taskNodeDO.setTaskParseUid(uid);
            taskNodeDO.setElementInfoMap(JSON.toJSONString(taskNodeInfo.getElementInfoMap()));
            taskNodeComponent.insert(taskNodeDO);
        });
    }

    /**
     * 删除任务节点
     *
     * @param uid
     */
    private void deleteTaskNodes(String uid) {
        List<BeedoTaskNode> beedoTaskNodes = taskNodeComponent.queryListByTaskParseUid(uid);

        //删除task节点信息
        for (BeedoTaskNode beedoTaskNode : beedoTaskNodes) {
            taskNodeComponent.deleteById(beedoTaskNode.getId());
        }
    }

}
