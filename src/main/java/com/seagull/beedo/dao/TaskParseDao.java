/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao;

import com.seagull.beedo.dao.dataobject.TaskParseDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author guosheng.huang
 * @version $id:TaskParseDao.java, v 0.1 2018年08月12日 12:41 tao.hu Exp $
 */

public interface TaskParseDao  extends JpaRepository<TaskParseDO, Integer> {

    TaskParseDO findByUid(String uid);

    void deleteByUid(String uid);
}

