/**
 * yingyinglicai.com Inc.
 * Copyright (c) 2013-2018 All Rights Reserved.
 */
package com.seagull.beedo.component.mysql;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.github.pagehelper.PageInfo;
import com.seagull.beedo.common.query.DocumentQuery;
import com.seagull.beedo.dao.domain.BeedoDocument;

/**
 * @author guosheng.huang
 * @version $Id: DocumentComponent, v1.0 2018年12月07日 12:06 guosheng.huang Exp $
 */
@CacheConfig(cacheNames = "documentInfo")
public interface DocumentComponent {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Cacheable(key = "#id")
    BeedoDocument queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    PageInfo<BeedoDocument> queryForPage(DocumentQuery query);

    /**
     * 新增数据
     *
     * @param BeedoDocument 实例对象
     * @return 实例对象
     */
    void insert(BeedoDocument BeedoDocument);

    /**
     * 修改数据
     *
     * @param BeedoDocument 实例对象
     * @return 实例对象
     */

    @CachePut(key = "#BeedoDocument.id")
    BeedoDocument update(BeedoDocument BeedoDocument);


    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @CacheEvict(key = "#id", condition = "#result == true ")
    void deleteById(Integer id);

}
