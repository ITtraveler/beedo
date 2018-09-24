/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao;

import com.seagull.beedo.dao.dataobject.DocumentParseDO;
import com.seagull.beedo.dao.dataobject.ElementParseDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import team.seagull.common.base.query.QueryBase;

import java.util.List;

/**
 * @author guosheng.huang
 * @version $id:DocumentParseDao.java, v 0.1 2018年08月12日 12:35 guosheng.huang Exp $
 */
public interface ElementParseDao extends JpaRepository<ElementParseDO, Integer> {

    /**
     * 删除Document相关的Element
     * @param documentId
     */
    void deleteByDocumentId(Integer documentId);

    /**
     * documentUid查选所以相关elementParse
     *
     * @param documentId
     * @return
     */
    List<ElementParseDO> findByDocumentIdOrderByGmtCreateDesc(Integer documentId);
}
