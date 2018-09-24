/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao;

import com.seagull.beedo.dao.dataobject.TaskParseDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;

/**
 * @author guosheng.huang
 * @version $id:TaskParseDao.java, v 0.1 2018年08月12日 12:41 guosheng.huang Exp $
 */

public interface TaskParseDao extends JpaRepository<TaskParseDO, Integer> {

    TaskParseDO findByUid(String uid);

    @Query(value = "select * from beedo_task_parse where uid=:uid for update ", nativeQuery = true)
    TaskParseDO findByUidForUpdate(@Param("uid") String uid);

    void deleteByUid(String uid);
}

