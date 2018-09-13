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
 * @version $id:TaskNodeDO.java, v 0.1 2018年08月11日 20:51 tao.hu Exp $
 */
@Data
public class TaskNodeInfo {

    /**
     * DocumentParseDO id
     */
    private Integer documentId;

    /**
     * k:ElementParseDO id    v:保存在数据库中的属性名
     */
    private Map<Object, String> elementIds;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;
}
