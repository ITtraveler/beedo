/**
 * yingyinglicai.com Inc.
 * Copyright (c) 2013-2018 All Rights Reserved.
 */
package com.seagull.beedo.common.query;

import com.seagull.beedo.common.query.base.QueryDate;
import lombok.Data;

/**
 * 元素查询
 * @author guosheng.huang
 * @version $Id: ElementQuery, v1.0 2018年12月07日 14:04 guosheng.huang Exp $
 */
@Data
public class ElementQuery extends QueryDate {
    private static final long serialVersionUID = 987832873321373033L;
    private Integer id;

    /**
     * 文档Id
     */
    private Integer documentId;

    /**
     * 属性
     */
    private String attr;

    /**
     * 数据类型
     * ElementDataTypeEnum
     */
    private String dataType;

    /**
     * 数据结构类型
     * ElementStructureTypeEnum
     */
    private String structureType;

    /**
     * 状态
     * CommonStatusEnum
     */
    private String status;

}
