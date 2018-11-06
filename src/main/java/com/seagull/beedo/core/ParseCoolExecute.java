/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.core;

import com.seagull.beedo.common.enums.ParseDataTypeEnum;
import com.seagull.beedo.common.enums.ParseStructureTypeEnum;
import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.common.utils.OptimizeUtils;
import com.seagull.beedo.component.DocumentParseComponent;
import com.seagull.beedo.component.ParseDataComponent;
import com.seagull.beedo.component.TaskParseComponent;
import com.seagull.beedo.model.DocumentParseInfo;
import com.seagull.beedo.model.ElementParseInfo;
import com.seagull.beedo.model.TaskElementInfo;
import com.seagull.beedo.model.TaskNodeInfo;
import com.seagull.beedo.model.TaskParseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import team.seagull.common.base.utils.CollectionUtils;
import team.seagull.common.base.utils.JsoupUtilSingleton;
import team.seagull.common.base.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guosheng.huang
 * @version $id:ParseCoolExecute.java, v 0.1 2018年08月12日 11:12 guosheng.huang Exp $
 */
@Component
public class ParseCoolExecute {
    /**
     * logger
     */
    Logger logger = LoggerFactory.getLogger(ParseCoolExecute.class);

    /**
     * 暂存区
     */
    private Map<String, Object> schedulerMap = new HashMap<>();

    @Autowired
    private DocumentParseComponent documentParseComponent;

    @Autowired
    private TaskParseComponent taskParseComponent;

    @Autowired
    private ParseDataComponent dataComponent;

    public void parseCool(List<TaskParseInfo> parseInfos) {
        //任务执行
        parseInfos.forEach(taskParseInfo -> {
                    //不为有效状态->下一个
                    if (TaskStatusEnum.INIT == taskParseInfo.getTaskStatus()) {
                        return;
                    }

                    ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) schedulerMap.get(taskParseInfo
                            .getUid());

                    //创建一个scheduler
                    if (scheduler == null) {
                        if (taskParseInfo.getTaskStatus() == TaskStatusEnum.VALID) {
                            scheduler = new ThreadPoolTaskScheduler();
                            scheduler.initialize();
                            scheduler.setThreadGroupName(taskParseInfo.getUid());
                            schedulerMap.put(taskParseInfo.getUid(), scheduler);
                        } else if (taskParseInfo.getTaskStatus() == TaskStatusEnum.CLOSE) {
                            //更新状态为INIT
                            taskParseComponent.updateTaskStatus(taskParseInfo.getUid(), TaskStatusEnum.INIT);
                            return;
                        }
                    }

                    //关闭任务
                    if (taskParseInfo.getTaskStatus() == TaskStatusEnum.CLOSE) {
                        scheduler.shutdown();
                        schedulerMap.remove(taskParseInfo.getUid());
                        logger.info("关闭定时任务，taskParseInfo：{}", taskParseInfo);
                        //更新状态为INIT
                        taskParseComponent.updateTaskStatus(taskParseInfo.getUid(), TaskStatusEnum.INIT);
                        return;
                    }

                    scheduler.setPoolSize(taskParseInfo.getThreadCoolSize());
                    scheduler.schedule(() -> {

                        //进行解析
                        List<Map<Object, Object>> parseResult = parse(taskParseInfo, null);

                        // TODO: 2018/9/26 动态索引设置
                        // 将解析到的数据保存到mongodb及设置索引
                        for (Map data : parseResult) {
                            dataComponent.saveData(data, taskParseInfo.getCollectionName(),
                                    Arrays.asList("title"));
                            System.out.println(data);
                        }
                    }, triggerContext -> {
                        String cron = taskParseInfo.getCron();

                        if (StringUtils.isBlank(cron)) {
                            return null;
                        }

                        // cron解析器
                        CronTrigger trigger = new CronTrigger(cron);

                        // 定时任务触发的下一个时间
                        Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                        return nextExecDate;
                    });
                }
        );
    }

    private List<Map<Object, Object>> parse(TaskParseInfo taskParseInfo, String url) {
        List<Map<Object, Object>> resultData = new ArrayList<>();
        List<TaskNodeInfo> parseNodes = taskParseInfo.getParseNodes();
        if (CollectionUtils.isEmpty(parseNodes)) {
            return resultData;
        }


        //需要进行展开的数组数据最长长度
        Integer maxLength = 0;

        //需要展开的数据
        Map<Object, Map<String, Object>> expandDataMap = new LinkedHashMap<>();

        Map<Object, Object> data = new LinkedHashMap<>();
        resultData.add(data);

        for (TaskNodeInfo taskNodeInfo : parseNodes) {

            //获取该节点的Document信息
            DocumentParseInfo documentParseInfo = documentParseComponent.getDocumentById(taskNodeInfo.getDocumentId());
            if (documentParseInfo == null) {
                logger.error("未找到解析信息，数据异常，请处理documentId:{}", taskNodeInfo.getDocumentId());
                continue;
            }

            //该节点所解析的url,为父类直接用自身的url
            if (taskParseInfo.getLevel() == 0) {
                url = documentParseInfo.getProtocol() + documentParseInfo.getUrl();
            }

            //该节点要解析的目标内容（一个Document可能配置了多个Element，即可能配置多个）
            Map<Object, TaskElementInfo> elementIdMap = taskNodeInfo.getElementInfoMap();
            for (Map.Entry entry : elementIdMap.entrySet()) {
                Integer elementId = Integer.valueOf(entry.getKey().toString());
                //具体解析的元素
                ElementParseInfo elementParseInfo =
                        documentParseComponent.getElementById(elementId);
                if (elementParseInfo == null) {
                    logger.error("未找到解析信息，数据异常，请处理element:{}", elementId);
                    continue;
                }

                //元素解析结果
                Object parseResult = getParseResult(url, elementParseInfo);

                TaskElementInfo taskElementInfo = (TaskElementInfo) entry.getValue();
                if (taskElementInfo == null) {
                    logger.error("任务元素信息不存在，数据异常，请处理element:{}", elementId);
                    continue;
                }

                //优化结果
                parseResult = optimize(parseResult, documentParseInfo, taskParseInfo, elementParseInfo);

                if (ParseStructureTypeEnum.ARRAY == elementParseInfo.getStructureType()
                        && taskElementInfo.getExpand()) {
                    int size = ((List) parseResult).size();
                    if (size > maxLength) {
                        maxLength = size;
                    }
                }

                if (ParseStructureTypeEnum.ARRAY == elementParseInfo.getStructureType()
                        && taskElementInfo.getExpand()) {
                    Map<String, Object> valueMap = new HashMap<>();
                    valueMap.put("subTaskUid", taskElementInfo.getSubTaskUid());
                    valueMap.put("expandData", parseResult);
                    expandDataMap.put(taskElementInfo.getField(), valueMap);
                } else {
                    //有子任务情况，进行递归获取子任务数据
                    if (ParseDataTypeEnum.URL == elementParseInfo.getDataType()
                            && StringUtils.isNotBlank(taskElementInfo.getSubTaskUid())) {
                        TaskParseInfo subTaskParseInfo = taskParseComponent.getTaskByUid(taskElementInfo.getSubTaskUid());
                        //设置子项的url
                        data.put(taskElementInfo.getField(), parse(subTaskParseInfo, parseResult.toString()));
                    } else {
                        data.put(taskElementInfo.getField(),
                                (parseResult == null || StringUtils.isBlank(parseResult.toString())) ?
                                        taskElementInfo.getDefValue()
                                        : parseResult);
                    }
                }
            }
        }


        if (maxLength > 0) {
            resultData = dealParseData(data, expandDataMap, maxLength);
        }

        //处理子项

        return resultData;
    }

    /**
     * 处理解析的数据，进行展开处理
     *
     * @param data          单条数据
     * @param expandDataMap 需要展开的数据
     * @param maxLength     需要展开的数组最长长度
     * @return
     */
    private List<Map<Object, Object>> dealParseData(Map<Object, Object> data,
                                                    Map<Object, Map<String, Object>> expandDataMap, Integer maxLength) {
        List<Map<Object, Object>> resultData = new ArrayList<>();

        for (int i = 0; i < maxLength; i++) {
            Map<Object, Object> copyData = new LinkedHashMap<>(data);

            //需要展开的数据进行展开处理
            for (Map.Entry entry : expandDataMap.entrySet()) {
                Map valueMap = (Map) entry.getValue();

                //拓展的数据
                List expandData = (List) valueMap.get("expandData");
                int index = expandData.size() - i % expandData.size() - 1;
                copyData.put(entry.getKey(), expandData.get(index));

                //子项数据
                Object subTaskUid = valueMap.get("subTaskUid");
                if (subTaskUid != null) {
                    //有缓存处理
                    TaskParseInfo subTaskParseInfo = taskParseComponent.getTaskByUid(subTaskUid.toString());
                    copyData.put("subData", parse(subTaskParseInfo, expandData.get(index).toString()));
                }

            }
            resultData.add(copyData);
        }
        return resultData;

    }

    /**
     * 获取每一个element解析的结果
     *
     * @param url
     * @param elementParseInfo
     * @return
     */
    private Object getParseResult(String url, ElementParseInfo elementParseInfo) {
        Object result = "";
        JsoupUtilSingleton jsoupUtilSingleton = JsoupUtilSingleton.getJsoupUtilSingleton();
        switch (elementParseInfo.getStructureType()) {
            case STRING:
                switch (elementParseInfo.getDataType()) {
                    case TEXT:
                        result = jsoupUtilSingleton.getText(url, elementParseInfo.getCssQuery());
                        break;
                    case ATTR:
                        result = jsoupUtilSingleton.getAttr(url, elementParseInfo.getCssQuery(), elementParseInfo
                                .getAttr());
                        break;
                    case URL:
                        if (StringUtils.isNotBlank(elementParseInfo.getAttr())) {
                            result = jsoupUtilSingleton.getAttr(url, elementParseInfo.getCssQuery(), elementParseInfo
                                    .getAttr());
                        } else {
                            result = jsoupUtilSingleton.getText(url, elementParseInfo.getCssQuery());
                        }
                        break;
                    case HTML:
                        result = jsoupUtilSingleton.getHtml(url, elementParseInfo.getCssQuery());
                        break;
                }
                break;
            case ARRAY:
                switch (elementParseInfo.getDataType()) {
                    case TEXT:
                        result = jsoupUtilSingleton.getTextList(url, elementParseInfo.getCssQuery());
                        break;
                    case ATTR:
                        result = jsoupUtilSingleton.getAttrList(url, elementParseInfo.getCssQuery(), elementParseInfo
                                .getAttr());
                        break;
                    case URL:
                        if (StringUtils.isNotBlank(elementParseInfo.getAttr())) {
                            result = jsoupUtilSingleton.getAttrList(url, elementParseInfo.getCssQuery(), elementParseInfo
                                    .getAttr());
                        } else {
                            result = jsoupUtilSingleton.getTextList(url, elementParseInfo.getCssQuery());
                        }
                        break;
                    case HTML:
                        result = jsoupUtilSingleton.getHtmlList(url, elementParseInfo.getCssQuery());
                        break;
                }
                break;
        }
        return result;
    }

    private Object optimize(Object obj, DocumentParseInfo documentParseInfo,
                            TaskParseInfo taskParseInfo, ElementParseInfo elementParseInfo) {
        String baseUrl = documentParseInfo.getProtocol() + documentParseInfo.getUrl();
        switch (elementParseInfo.getStructureType()) {
            case ARRAY:
                List<String> list = new ArrayList<>();
                for (String str : (List<String>) obj) {
                    // str = str.replace();
                    if (ParseDataTypeEnum.URL == elementParseInfo.getDataType()) {
                        str = OptimizeUtils.getVaildUrl(baseUrl, str);
                    }
                    list.add(str);
                }
                obj = list;
                break;
            case STRING:
                if (ParseDataTypeEnum.URL == elementParseInfo.getDataType()) {
                    obj = OptimizeUtils.getVaildUrl(baseUrl, (String) obj);
                }
                break;
        }
        return obj;

    }
}
