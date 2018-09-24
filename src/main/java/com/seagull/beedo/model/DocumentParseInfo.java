/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * 一个页面为一个Document，一个Document下有多个Element
 * @author guosheng.huang
 * @version $id:DocumentParseDO.java, v 0.1 2018年08月11日 20:51 guosheng.huang Exp $
 */
@Data
public class DocumentParseInfo {

    /**
     * 每个document都有一个uid,作为区分
     */
    private Integer id;

    private String protocol;

    /**
     * 被解析的url
     */
    private String url;

    private String name;

    /**
     * 解析的节点
     */
    private List<ElementParseInfo> elements;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;
}
