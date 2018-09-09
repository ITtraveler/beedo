/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.model.DocumentParseInfo;
import com.seagull.beedo.model.ElementParseInfo;
import com.seagull.beedo.model.TaskNodeInfo;
import com.seagull.beedo.model.TaskParseInfo;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import team.seagull.common.base.utils.CollectionUtils;
import team.seagull.common.base.utils.JsoupUtilSingleton;
import team.seagull.common.base.utils.RandomUtils;
import team.seagull.common.base.utils.StringUtils;

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
    private Map<String, Object> schedulerMap = new HashMap<>();

    public void parseCool(List<TaskParseInfo> taskInfoList) {

        //任务执行
        taskInfoList.forEach(taskParseInfo -> {
                    //不为有效状态->下一个
                    if (TaskStatusEnum.INIT == taskParseInfo.getTaskStatus()) {
                        return;
                    }

                    // TODO: 2018/8/11 将改成任务定时保存再redis中防止，防止多次创建，key：taskParseInfo.id
                    ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) schedulerMap.get(taskParseInfo
                            .getId());

                    //创建一个scheduler
                    if (scheduler == null) {
                        scheduler = new ThreadPoolTaskScheduler();
                        schedulerMap.put(taskParseInfo.getUid(), scheduler);
                    }

                    //关闭任务
                    if (taskParseInfo.getTaskStatus() == TaskStatusEnum.CLOSE) {
                        scheduler.shutdown();
                        schedulerMap.remove(taskParseInfo.getId());
                        return;
                    }

                    scheduler.initialize();
                    scheduler.setThreadGroupName(taskParseInfo.getUid());
                    scheduler.setPoolSize(taskParseInfo.getThreadCoolSize());
                    scheduler.schedule(() -> {
                        Map<String, Object> data = new LinkedHashMap<>();
                        List<TaskNodeInfo> parseNodes = taskParseInfo.getParseNodes();

                        if (CollectionUtils.isEmpty(parseNodes)) {
                            return;
                        }

                        parseNodes.forEach(taskNodeInfo -> {
                            Map<String, String> elementIdMap = taskNodeInfo.getElementIds();

                            Integer documentUid = taskNodeInfo.getDocumentId();
                            // TODO: 2018/8/11  得到DocumentParseInfo
                            DocumentParseInfo documentParseInfo = new DocumentParseInfo();
                            String url = documentParseInfo.getUrl();

                            List<ElementParseInfo> elements = documentParseInfo.getElements();
                            elements.forEach(elementParseInfo -> {
                                Object parseResult = getParseResult(url, elementParseInfo);
                                //将得到的数据放入到map中
                                data.put(elementIdMap.getOrDefault(elementParseInfo.getId(), RandomUtils.getUUID()),
                                        parseResult);
                            });
                        });
                        // TODO: 2018/8/11 将获取到的data保存到mongodb
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
}
