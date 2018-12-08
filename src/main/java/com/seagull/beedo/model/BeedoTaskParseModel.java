/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.model;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 任务解析的信息
 *
 * @author guosheng.huang
 * @version $id:TaskParseDO.java, v 0.1 2018年08月11日 20:45 guosheng.huang Exp $
 */
@Data
public class BeedoTaskParseModel {

    private Integer id;

    /**
     * 任务uid
     */
    private String uid;

    /**
     * 任务名
     */
    private String name;

    /**
     * 任务级别
     */
    private int level;

    /**
     * 任务执行的cron表达式
     */
    private String cron;


    /**
     * 线程池大小
     */
    private int threadCoolSize;

    /**
     * 要进行解析执行的节点
     */
    private List<BeedoTaskNodeModel> parseNodes;


    /**
     * 该解析信息的任务状态
     */
    private TaskStatusEnum taskStatus;

    /**
     * mongodb collectionName
     */
    private String collectionName;

    /**
     * 是否需要优化爬虫结果
     */
    private boolean optimize;


    /**
     * 索引
     */
    private List<String> indexs;


    private String memo;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;
}
