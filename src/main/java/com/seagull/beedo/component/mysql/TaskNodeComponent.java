/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component.mysql;

import com.seagull.beedo.common.query.TaskNodeQuery;
import com.seagull.beedo.dao.domain.BeedoTaskNode;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 任务解析组件
 *
 * @author guosheng.huang
 * @version $id:TaskParseComponent.java, v 0.1 2018年08月13日 21:13 guosheng.huang Exp $
 */
@CacheConfig(cacheNames = "taskNodeInfo")
public interface TaskNodeComponent {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Cacheable(key = "#id")
    BeedoTaskNode queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    @Cacheable
    PageInfo<BeedoTaskNode> queryForPage(TaskNodeQuery query);


    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    @Cacheable
    List<BeedoTaskNode> queryListByTaskParseUid(String taskParseUid);

    /**
     * 新增数据
     *
     * @param beedoTaskNode 实例对象
     * @return 实例对象
     */
    void insert(BeedoTaskNode beedoTaskNode);

    /**
     * 修改数据
     *
     * @param beedoTaskNode 实例对象
     * @return 实例对象
     */

    @CachePut(key = "#beedoTaskNode.id")
    BeedoTaskNode update(BeedoTaskNode beedoTaskNode);


    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @CacheEvict(key = "#id", condition = "#result == true ")
    void deleteById(Integer id);
}
