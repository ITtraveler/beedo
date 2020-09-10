/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.core;

import cn.hutool.core.util.NumberUtil;
import com.github.pagehelper.util.StringUtil;
import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.common.enums.TaskTypeEnum;
import com.seagull.beedo.component.mongo.ParseDataComponent;
import com.seagull.beedo.model.BeedoTaskNodeModel;
import com.seagull.beedo.model.BeedoTaskParseModel;
import com.seagull.beedo.model.TaskElementInfo;
import com.seagull.beedo.service.TaskParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author guosheng.huang
 * @version $id:ParseCoolExecute.java, v 0.1 2018年08月12日 11:12 guosheng.huang Exp $
 */
@Component
public class ParseCoolExecute {
    public static final String URL_EXPRESSION_ELEMENT_KEY = "-1";
    /**
     * logger
     */
    Logger logger = LoggerFactory.getLogger(ParseCoolExecute.class);
    @Autowired
    private Map schedulerMap;

    @Autowired
    private ParseDataComponent dataComponent;

    @Autowired
    private TaskParseService taskParseService;

    @Autowired
    private ParseCore parseCore;

    public void parseCool(List<BeedoTaskParseModel> parseInfos) {
        //任务执行
        parseInfos.forEach(taskParseInfo -> {
            if (TaskStatusEnum.VALID != taskParseInfo.getTaskStatus()
                    && TaskStatusEnum.MODIFIED != taskParseInfo.getTaskStatus()) {
                return;
            }

            ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) schedulerMap.get(taskParseInfo.getUid());
            //创建一个scheduler
            if (scheduler == null) {
                scheduler = initThreadPoolTaskScheduler(taskParseInfo);
            } else {
                //修改过，shutdown，重新初始化scheduler
                if (TaskStatusEnum.MODIFIED == taskParseInfo.getTaskStatus()) {
                    scheduler.shutdown();
                    scheduler = initThreadPoolTaskScheduler(taskParseInfo);
                } else {
                    return;
                }
            }


            scheduler.setPoolSize(taskParseInfo.getThreadCoolSize());

            scheduler.schedule(() -> {
                parseExecute(taskParseInfo);

            }, triggerContext -> {
                String cron = taskParseInfo.getCron().trim();

                if (StringUtil.isEmpty(cron)) {
                    return null;
                }

                // cron解析器
                CronTrigger trigger = new CronTrigger(cron);

                // 定时任务触发的下一个时间
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            });
            taskParseService.updateTaskStatus(taskParseInfo.getUid(), TaskStatusEnum.VALID);
        });
    }

    private ThreadPoolTaskScheduler initThreadPoolTaskScheduler(BeedoTaskParseModel taskParseInfo) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        scheduler.setThreadGroupName(taskParseInfo.getUid());
        schedulerMap.put(taskParseInfo.getUid(), scheduler);
        return scheduler;
    }

    /**
     * 进行解析
     *
     * @param taskParseInfo
     */
    private void parseExecute(BeedoTaskParseModel taskParseInfo) {

        //url表达式任务
        if (TaskTypeEnum.URL_EXPRESSION == taskParseInfo.getType()) {
            if (CollectionUtils.isEmpty(taskParseInfo.getParseNodes())) {
                logger.error("数据异常，任务节点为空 taskParseInfo:{}", taskParseInfo);
                return;
            }

            TaskElementInfo taskElementInfo = taskParseInfo.getParseNodes().get(0).getElementInfoMap().get(URL_EXPRESSION_ELEMENT_KEY);

            if (taskElementInfo == null || StringUtil.isEmpty(taskElementInfo.getUrlExpression())) {
                logger.error("数据异常，任务节点配置信息存在空配置 taskParseInfo:{}", taskParseInfo);
                return;
            }

            List<String> parseUrls = UrlExpression.getResultUrl(taskElementInfo.getUrlExpression());
            for (String url : parseUrls) {
                BeedoTaskParseModel subTaskParseInfo = taskParseService
                        .getTaskByUid(taskElementInfo.getSubTaskUid());
                List<Map<Object, Object>> parseResult = parseCore.parse(subTaskParseInfo, url);
                saveDataToMongo(taskParseInfo.getCollectionName(), getTaskParseIndexs(subTaskParseInfo), parseResult);
                /*logger.info("任务{}({})解析完成，解析结果parseResult：{}", taskParseInfo.getName(), taskParseInfo.getUid(),
                        parseResult);*/
                logger.info("任务{}({})解析完成", taskParseInfo.getName(), taskParseInfo.getUid());
            }
            //元素组合任务
        } else {
            List<Map<Object, Object>> parseResult = parseCore.parse(taskParseInfo);
            saveDataToMongo(taskParseInfo.getCollectionName(), getTaskParseIndexs(taskParseInfo), parseResult);
            /*logger.info("任务{}({})解析完成，解析结果parseResult：{}", taskParseInfo.getName(), taskParseInfo.getUid(), parseResult);*/

            logger.info("任务{}({})解析完成", taskParseInfo.getName(), taskParseInfo.getUid());
        }
    }

    /**
     * 将解析到的数据保存到mongodb及设置索引
     *
     * @param collectionName
     * @param indexs
     * @param data
     */
    private void saveDataToMongo(String collectionName, List<String> indexs, List<Map<Object, Object>> data) {
        for (Map lineData : data) {
            dataComponent.saveData(lineData, collectionName, indexs);
        }
    }

    /**
     * 获取解析任务的线程池
     *
     * @param taskParseInfo
     * @return
     */
    private ThreadPoolTaskScheduler getThreadPoolTaskScheduler(BeedoTaskParseModel taskParseInfo) {
        ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) schedulerMap.get(taskParseInfo.getUid());
        //创建一个scheduler
        if (scheduler == null) {
            scheduler = initThreadPoolTaskScheduler(taskParseInfo);
        }

        scheduler.setPoolSize(taskParseInfo.getThreadCoolSize());
        return scheduler;
    }

    /**
     * 数据唯一性索引
     * todo 考虑添加一字段存放索引
     *
     * @param taskParseInfo
     * @return
     */
    @Cacheable(key = "'PARSE_INDEX_'+#taskParseInfo.uid")
    public List<String> getTaskParseIndexs(BeedoTaskParseModel taskParseInfo) {
        List<String> indexs = new ArrayList<>();
        List<BeedoTaskNodeModel> parseNodes = taskParseInfo.getParseNodes();
        for (BeedoTaskNodeModel taskNodeModel : parseNodes) {
            Map<Object, TaskElementInfo> elementInfoMap = taskNodeModel.getElementInfoMap();
            for (TaskElementInfo taskElementInfo : elementInfoMap.values()) {

                if (taskElementInfo.getIsIndex()) {
                    indexs.add(taskElementInfo.getField());
                }
            }
        }
        return indexs;
    }
}
