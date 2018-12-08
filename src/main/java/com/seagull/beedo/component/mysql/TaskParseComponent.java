/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component.mysql;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.github.pagehelper.PageInfo;
import com.seagull.beedo.common.query.TaskParseQuery;
import com.seagull.beedo.dao.domain.BeedoTaskParse;

/**
 * 任务解析组件
 *
 * @author guosheng.huang
 * @version $id:TaskParseComponent.java, v 0.1 2018年08月13日 21:13 guosheng.huang Exp $
 */
@CacheConfig(cacheNames = "taskParseInfo")
public interface TaskParseComponent {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Cacheable(key = "#id")
    BeedoTaskParse queryById(Integer id);

    /**
     * 通过UID查询单条数据
     *
     * @param uid 主键
     * @return 实例对象
     */
    @Cacheable(key = "'uid_'+#uid")
    BeedoTaskParse queryByUid(String uid);

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    PageInfo<BeedoTaskParse> queryForPage(TaskParseQuery query);

    /**
     * 新增数据
     *
     * @param beedoTaskParse 实例对象
     * @return 实例对象
     */
    BeedoTaskParse insert(BeedoTaskParse beedoTaskParse);

    /**
     * 修改数据
     *
     * @param beedoTaskParse 实例对象
     * @return 实例对象
     */

    @CachePut(key = "#result.id")
    BeedoTaskParse update(BeedoTaskParse beedoTaskParse);


    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @CacheEvict(key = "#id", condition = "#result == true ")
    void deleteById(Integer id);

    /**
     * 通过uid主键删除数据
     *
     * @param uid 主键
     * @return 是否成功
     */
    @CacheEvict(key = "'uid_'+#uid", condition = "#result == true ")
    void deleteByUid(String uid);
}
