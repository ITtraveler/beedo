package com.seagull.beedo.component.mysql;

import com.github.pagehelper.PageInfo;
import com.seagull.beedo.common.query.ElementQuery;
import com.seagull.beedo.dao.domain.BeedoElement;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author guosheng.huang
 * @version $Id: ElementComponent, v1.0 2018年12月07日 12:09 guosheng.huang Exp $
 */
@CacheConfig(cacheNames = "elementInfo")
public interface ElementComponent {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Cacheable(key = "#id")
    BeedoElement queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    PageInfo<BeedoElement> queryForPage(ElementQuery query);


    /**
     * 查询多条数据,通过documentId
     *
     * @return 对象列表
     */
    @Cacheable(key = "'documentId_'+#documentId")
    List<BeedoElement> queryListByDocumentId(Integer documentId);

    /**
     * 新增数据
     *
     * @param beedoElement 实例对象
     * @return 实例对象
     */
    void insert(BeedoElement beedoElement);

    /**
     * 修改数据
     *
     * @param beedoElement 实例对象
     * @return 实例对象
     */

    @CachePut(key = "#beedoElement.id")
    BeedoElement update(BeedoElement beedoElement);


    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @CacheEvict(key = "#id", condition = "#result == true ")
    void deleteById(Integer id);
}
