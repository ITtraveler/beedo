/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.model;

import lombok.Data;

/**
 * 任务元素信息
 *
 * @author guosheng.huang
 * @version TaskElementInfo.java, v 0.1 2018年09月23日 22:18 guosheng.huang Exp $
 */
@Data
public class TaskElementInfo {
    /**
     * 元素id
     */
    private int elementId;

    /**
     * 属性名，用于保存数据库中的属性名
     */
    private String Field;

    /**
     * 子任务的uid  数据类型为URL的才可以配置子解析任务|task类型为URL_EXPRESSION时
     */
    private String subTaskUid;

    /**
     * 默认值
     */
    private String defValue;

    /**
     * 是否需要展开，仅数据结构为数组时才可以数据展开
     */
    private Boolean expand = false;

    /**
     * 是否为唯一性索引
     */
    private Boolean isIndex = false;


    /**
     * 是否不为空数据
     */
    private Boolean isNotBlank = false;


    /**
     * URL表达式（仅task类型为URL_EXPRESSION时才有用）
     */
    private String urlExpression;
}
