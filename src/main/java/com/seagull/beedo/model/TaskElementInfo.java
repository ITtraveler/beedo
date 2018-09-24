/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.model;

import lombok.Data;

/**
 * 任务元素信息
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
     * 子任务的uid  仅数据类型为URL的才可以配置子解析任务
     */
    private String subTaskUid;

    /**
     * 是否需要展开，仅数据结构为数组时才可以数据展开
     */
    private Boolean expand = true;
}
