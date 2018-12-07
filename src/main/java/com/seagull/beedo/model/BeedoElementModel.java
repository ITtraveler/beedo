/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.model;

import com.seagull.beedo.common.enums.ElementDataTypeEnum;
import com.seagull.beedo.common.enums.ElementStructureTypeEnum;
import lombok.Data;

/**
 * 一个Element对应一个document表达式
 * @author guosheng.huang
 * @version $id:ElementParseDO.java, v 0.1 2018年08月11日 20:51 guosheng.huang Exp $
 */
@Data
public class BeedoElementModel {

    private Integer id;

    private Integer documentId;

    /**
     * document表达式
     */
    private String cssQuery;

    /**
     * 数据类型
     */
    private ElementDataTypeEnum dataType;

    /**
     * 结构类型
     */
    private ElementStructureTypeEnum structureType;

    /**
     * 如果解析的是属性则有值
     */
    private String attr;

    /**
     * 备注
     */
    private String memo;

}
