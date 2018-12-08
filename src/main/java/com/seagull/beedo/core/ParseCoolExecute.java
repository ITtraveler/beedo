/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.core;

import com.seagull.beedo.common.enums.ElementDataTypeEnum;
import com.seagull.beedo.common.enums.ElementStructureTypeEnum;
import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.common.utils.OptimizeUtils;
import com.seagull.beedo.component.mongo.ParseDataComponent;
import com.seagull.beedo.model.BeedoDocumentModel;
import com.seagull.beedo.model.BeedoElementModel;
import com.seagull.beedo.model.BeedoTaskNodeModel;
import com.seagull.beedo.model.BeedoTaskParseModel;
import com.seagull.beedo.model.TaskElementInfo;
import com.seagull.beedo.service.DocumentService;
import com.seagull.beedo.service.TaskParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import team.seagull.common.base.utils.CollectionUtils;
import team.seagull.common.base.utils.JsoupUtilSingleton;
import team.seagull.common.base.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private Map schedulerMap;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private TaskParseService taskParseService;

    @Autowired
    private ParseDataComponent dataComponent;

    public void parseCool(List<BeedoTaskParseModel> parseInfos) {
        //任务执行
        parseInfos.forEach(taskParseInfo -> {
            if (TaskStatusEnum.VALID != taskParseInfo.getTaskStatus()) {
                return;
            }

            ThreadPoolTaskScheduler scheduler = getThreadPoolTaskScheduler(taskParseInfo);
            scheduler.schedule(() -> {

                //进行解析
                List<Map<Object, Object>> parseResult = parse(taskParseInfo);

                // 将解析到的数据保存到mongodb及设置索引
                for (Map data : parseResult) {
                    dataComponent.saveData(data, taskParseInfo.getCollectionName(),
                            getTaskParseIndexs(taskParseInfo));
                    logger.info("任务{}({})解析完成，数据结果data：{}", taskParseInfo.getName(), taskParseInfo.getUid(), data);
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
        });
    }


    private List<Map<Object, Object>> parse(BeedoTaskParseModel taskParseInfo) {
        return this.parse(taskParseInfo, null);
    }

    private List<Map<Object, Object>> parse(BeedoTaskParseModel taskParseInfo, String url) {
        List<Map<Object, Object>> resultData = new ArrayList<>();
        List<BeedoTaskNodeModel> parseNodes = taskParseInfo.getParseNodes();
        if (CollectionUtils.isEmpty(parseNodes)) {
            return resultData;
        }

        //需要进行展开的数组数据最长长度
        Integer maxLength = 0;

        //需要展开的数据
        Map<Object, Map<String, Object>> expandDataMap = new LinkedHashMap<>();

        Map<Object, Object> data = new LinkedHashMap<>();
        resultData.add(data);

        for (BeedoTaskNodeModel taskNodeInfo : parseNodes) {

            //获取该节点的Document信息
            BeedoDocumentModel documentParseInfo = documentService
                    .getDocumentById(taskNodeInfo.getDocumentId());
            if (documentParseInfo == null) {
                logger.error("未找到解析信息，数据异常，请处理documentId:{}", taskNodeInfo.getDocumentId());
                continue;
            }

            //该节点所解析的url,为父类直接用自身的url
            if (taskParseInfo.getLevel() == 0) {
                url = documentParseInfo.getProtocol() + documentParseInfo.getUrl();
             /*   List<String> resultUrl = UrlExpression.getResultUrl(url);
                if(CollectionUtils.isEmpty(resultUrl)){
                    continue;
                }
                if(resultUrl.size() == 1){
                    url = resultUrl.get(0);
                }else{
                }*/
            }

            //该节点要解析的目标内容（一个Document可能配置了多个Element，即可能配置多个）
            Map<Object, TaskElementInfo> elementIdMap = taskNodeInfo.getElementInfoMap();
            for (Map.Entry entry : elementIdMap.entrySet()) {
                Integer elementId = Integer.valueOf(entry.getKey().toString());
                //具体解析的元素
                BeedoElementModel elementParseInfo = documentService.getElementById(elementId);
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
                parseResult = optimize(parseResult, documentParseInfo, taskParseInfo,
                        elementParseInfo);

                if (ElementStructureTypeEnum.ARRAY == elementParseInfo.getStructureType()
                        && taskElementInfo.getExpand()) {
                    int size = ((List) parseResult).size();
                    if (size > maxLength) {
                        maxLength = size;
                    }
                }

                if (ElementStructureTypeEnum.ARRAY == elementParseInfo.getStructureType()
                        && taskElementInfo.getExpand()) {
                    Map<String, Object> valueMap = new HashMap<>();
                    valueMap.put("subTaskUid", taskElementInfo.getSubTaskUid());
                    valueMap.put("expandData", parseResult);
                    expandDataMap.put(taskElementInfo.getField(), valueMap);
                } else {
                    //有子任务情况，进行递归获取子任务数据
                    if (ElementDataTypeEnum.URL == elementParseInfo.getDataType()
                            && StringUtils.isNotBlank(taskElementInfo.getSubTaskUid())) {
                        BeedoTaskParseModel subTaskParseInfo = taskParseService
                                .getTaskByUid(taskElementInfo.getSubTaskUid());
                        //设置子项的url
                        data.put(taskElementInfo.getField(),
                                parse(subTaskParseInfo, parseResult.toString()));
                    } else {
                        data.put(taskElementInfo.getField(),
                                (parseResult == null || StringUtils.isBlank(parseResult.toString()))
                                        ? taskElementInfo.getDefValue()
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
     * 处理解析的数据，数组数据进行展开处理
     *
     * @param data          单条数据
     * @param expandDataMap 需要展开的数据
     * @param maxLength     需要展开的数组最长长度
     * @return
     */
    private List<Map<Object, Object>> dealParseData(Map<Object, Object> data,
                                                    Map<Object, Map<String, Object>> expandDataMap,
                                                    Integer maxLength) {
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
                    BeedoTaskParseModel subTaskParseInfo = taskParseService
                            .getTaskByUid(subTaskUid.toString());
                    copyData.put("subData",
                            parse(subTaskParseInfo, expandData.get(index).toString()));
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
    private Object getParseResult(String url, BeedoElementModel elementParseInfo) {
        Object result = "";
        JsoupUtilSingleton jsoupUtilSingleton = JsoupUtilSingleton.getJsoupUtilSingleton();
        switch (elementParseInfo.getStructureType()) {
            case STRING:
                switch (elementParseInfo.getDataType()) {
                    case TEXT:
                        result = jsoupUtilSingleton.getText(url, elementParseInfo.getCssQuery());
                        break;
                    case ATTR:
                        result = jsoupUtilSingleton.getAttr(url, elementParseInfo.getCssQuery(),
                                elementParseInfo.getAttr());
                        break;
                    case URL:
                        if (StringUtils.isNotBlank(elementParseInfo.getAttr())) {
                            result = jsoupUtilSingleton.getAttr(url, elementParseInfo.getCssQuery(),
                                    elementParseInfo.getAttr());
                        } else {
                            result = jsoupUtilSingleton.getText(url,
                                    elementParseInfo.getCssQuery());
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
                        result = jsoupUtilSingleton.getTextList(url,
                                elementParseInfo.getCssQuery());
                        break;
                    case ATTR:
                        result = jsoupUtilSingleton.getAttrList(url, elementParseInfo.getCssQuery(),
                                elementParseInfo.getAttr());
                        break;
                    case URL:
                        if (StringUtils.isNotBlank(elementParseInfo.getAttr())) {
                            result = jsoupUtilSingleton.getAttrList(url,
                                    elementParseInfo.getCssQuery(), elementParseInfo.getAttr());
                        } else {
                            result = jsoupUtilSingleton.getTextList(url,
                                    elementParseInfo.getCssQuery());
                        }
                        break;
                    case HTML:
                        result = jsoupUtilSingleton.getHtmlList(url,
                                elementParseInfo.getCssQuery());
                        break;
                }
                break;
        }
        return result;
    }

    private <T> T optimize(T obj, BeedoDocumentModel documentParseInfo,
                           BeedoTaskParseModel taskParseInfo, BeedoElementModel elementParseInfo) {
        String baseUrl = documentParseInfo.getProtocol() + documentParseInfo.getUrl();
        switch (elementParseInfo.getStructureType()) {
            case ARRAY:
                if (!(obj instanceof List)) {
                    break;
                }

                List<String> list = new ArrayList<>();
                for (String str : (List<String>) obj) {
                    // str = str.replace();
                    if (ElementDataTypeEnum.URL == elementParseInfo.getDataType()) {
                        str = OptimizeUtils.getVaildUrl(baseUrl, str);
                    }
                    list.add(str);
                }
                obj = (T) list;
                break;
            case STRING:
                if (!(obj instanceof String)) {
                    break;
                }

                if (ElementDataTypeEnum.URL == elementParseInfo.getDataType()) {
                    obj = (T) OptimizeUtils.getVaildUrl(baseUrl, (String) obj);
                }
                break;
        }
        return obj;

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
            scheduler = new ThreadPoolTaskScheduler();
            scheduler.initialize();
            scheduler.setThreadGroupName(taskParseInfo.getUid());
            schedulerMap.put(taskParseInfo.getUid(), scheduler);
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
