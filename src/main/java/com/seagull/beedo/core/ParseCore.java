/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.core;

import com.seagull.beedo.common.enums.ElementDataTypeEnum;
import com.seagull.beedo.common.enums.ElementStructureTypeEnum;
import com.seagull.beedo.common.enums.TaskTypeEnum;
import com.seagull.beedo.common.utils.JsoupUtilSingleton;
import com.seagull.beedo.common.utils.OptimizeUtils;
import com.seagull.beedo.common.utils.RandomUtils;
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
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guosheng.huang
 * @version ParseCore.java, v 0.1 2018年12月09日 18:13 guosheng.huang Exp $
 */
@Component
public class ParseCore {

    /**
     * logger
     */
    Logger logger = LoggerFactory.getLogger(ParseCore.class);

    @Autowired
    private DocumentService documentService;

    @Autowired
    private TaskParseService taskParseService;

    public List<Map<Object, Object>> parse(BeedoTaskParseModel taskParseInfo) {
        return this.parse(taskParseInfo, null);
    }

    public List<Map<Object, Object>> parse(BeedoTaskParseModel taskParseInfo, String url) {
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
            }


            JsoupUtilSingleton jsoupUtilSingleton = new JsoupUtilSingleton();

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
                Object parseResult = getParseResult(url, jsoupUtilSingleton, elementParseInfo);

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
                    try {
                        Thread.sleep(RandomUtils.getRandomInt(10) * 1000);
                    } catch (Exception e) {

                    }
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
    private Object getParseResult(String url, JsoupUtilSingleton jsoupUtilSingleton,
                                  BeedoElementModel elementParseInfo) {
        Object result = "";
        //JsoupUtilSingleton jsoupUtilSingleton = new JsoupUtilSingleton();//JsoupUtilSingleton.getJsoupUtilSingleton();
        try {
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
        } catch (Exception e) {
            logger.error("jsoup数据爬取失败",e);
        }

        return result;
    }

    private <T> T optimize(T obj, BeedoDocumentModel documentParseInfo,
                           BeedoTaskParseModel taskParseInfo, BeedoElementModel elementParseInfo) {
        if (TaskTypeEnum.URL_EXPRESSION == taskParseInfo.getType()) {
            return obj;
        }

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


}
