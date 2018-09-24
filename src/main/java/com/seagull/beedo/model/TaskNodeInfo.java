/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 要执行的Document/Element集合
 *
 * @author guosheng.huang
 * @version $id:TaskNodeDO.java, v 0.1 2018年08月11日 20:51 guosheng.huang Exp $
 */
@Data
public class TaskNodeInfo {

    /**
     * DocumentParseDO id
     */
    private Integer documentId;

    /**
     * k:ElementParseDO id    v:任务元素信息
     */
    private Map<Object, TaskElementInfo> elementInfoMap;


    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;
}
