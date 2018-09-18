/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.common.utils.OptimizeUtils;
import com.seagull.beedo.dao.mongodb.OptMongo;
import com.seagull.beedo.model.DocumentParseInfo;
import com.seagull.beedo.model.ElementParseInfo;
import com.seagull.beedo.model.TaskNodeInfo;
import com.seagull.beedo.model.TaskParseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import team.seagull.common.base.utils.CollectionUtils;
import team.seagull.common.base.utils.JsoupUtilSingleton;
import team.seagull.common.base.utils.RandomUtils;
import team.seagull.common.base.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guosheng.huang
 * @version $id:ParseCoolExecute.java, v 0.1 2018年08月12日 11:12 tao.hu Exp $
 */
@Component
public class ParseCoolExecute {
    /**
     * logger
     */
    Logger logger = LoggerFactory.getLogger(ParseCoolExecute.class);

    private Map<String, Object> schedulerMap = new HashMap<>();

    @Autowired
    private DocumentParseComponent documentParseComponent;

    @Autowired
    private OptMongo optMongo;

    public void parseCool(List<TaskParseInfo> taskInfoList) {

        //任务执行
        taskInfoList.forEach(taskParseInfo -> {
                    //不为有效状态->下一个
                    if (TaskStatusEnum.INIT == taskParseInfo.getTaskStatus()) {
                        return;
                    }

                    // TODO: 2018/8/11 将改成任务定时保存再redis中防止，防止多次创建，key：taskParseInfo.id
                    ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) schedulerMap.get(taskParseInfo
                            .getUid());

                    //创建一个scheduler
                    if (scheduler == null) {
                        if (taskParseInfo.getTaskStatus() == TaskStatusEnum.VALID) {
                            scheduler = new ThreadPoolTaskScheduler();
                            scheduler.initialize();
                            scheduler.setThreadGroupName(taskParseInfo.getUid());
                            schedulerMap.put(taskParseInfo.getUid(), scheduler);
                        } else {
                            return;
                        }
                    }

                    //关闭任务
                    if (taskParseInfo.getTaskStatus() == TaskStatusEnum.CLOSE) {
                        scheduler.shutdown();
                        schedulerMap.remove(taskParseInfo.getId());
                        logger.info("关闭定时任务，taskParseInfo：{}", taskParseInfo);
                        return;
                    }

                    scheduler.setPoolSize(taskParseInfo.getThreadCoolSize());
                    scheduler.schedule(() -> {
                        Map<Object, Object> data = new LinkedHashMap<>();
                        List<TaskNodeInfo> parseNodes = taskParseInfo.getParseNodes();

                        if (CollectionUtils.isEmpty(parseNodes)) {
                            return;
                        }

                        parseNodes.forEach(taskNodeInfo -> {
                            DocumentParseInfo documentParseInfo =
                                    documentParseComponent.getDocumentById(taskNodeInfo.getDocumentId());
                            if (documentParseInfo == null) {
                                logger.error("未找到解析信息，数据异常，请处理documentId:{}", taskNodeInfo.getDocumentId());
                            }

                            String url = documentParseInfo.getProtocol() + documentParseInfo.getUrl();
                            Map<Object, String> elementIdMap = taskNodeInfo.getElementIds();
                            for (Map.Entry entry : elementIdMap.entrySet()) {

                                if (entry.getValue() == null) {
                                    continue;
                                }

                                ElementParseInfo elementParseInfo =
                                        documentParseComponent.getElementById(Integer.valueOf(entry.getKey().toString()));
                                Object parseResult = getParseResult(url, elementParseInfo);

                                //进行解析结果优化
                                //if (taskParseInfo.isOptimize()) {
                                parseResult = optimize(parseResult, documentParseInfo, taskParseInfo, elementParseInfo);
                                // }

                                data.put(entry.getValue(), parseResult);
                            }
                        });
                        // TODO: 2018/8/11 将获取到的data保存到mongodb
                        optMongo.insert(data, taskParseInfo.getCollectionName());
                        System.out.println(data);
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
                    if ("href".equals(elementParseInfo.getAttr())) {
                        str = OptimizeUtils.getVaildUrl(baseUrl, str);
                    }
                    list.add(str);
                }
                obj = list;
                break;
            case STRING:
                if ("href".equals(elementParseInfo.getAttr())) {
                    obj = OptimizeUtils.getVaildUrl(baseUrl, (String) obj);
                }
                break;
        }
        return obj;

    }
}
