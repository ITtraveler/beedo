/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao;

import com.seagull.beedo.dao.dataobject.DocumentParseDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author guosheng.huang
 * @version $id:DocumentParseDao.java, v 0.1 2018年08月12日 12:35 tao.hu Exp $
 */
public interface DocumentParseDao extends JpaRepository<DocumentParseDO, Integer> {

    @Modifying(clearAutomatically = true)
    @Query(value = "update beedo_document as s set s.name = ?1 where id = ?2",nativeQuery = true)
    int updateNameById(String name, Integer id);

}
