/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao;

import com.seagull.beedo.dao.domain.TaskNodeDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author guosheng.huang
 * @version $id:TaskNodeDao.java, v 0.1 2018年08月13日 21:10 guosheng.huang Exp $
 */
public interface TaskNodeDao  extends JpaRepository<TaskNodeDO, Integer> {
    List<TaskNodeDO> findByTaskParseUid(String taskUid);

    int deleteByTaskParseUid(String taskUid);
}
