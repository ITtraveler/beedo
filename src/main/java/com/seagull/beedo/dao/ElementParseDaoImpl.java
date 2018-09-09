/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao;

import com.seagull.beedo.dao.dataobject.ElementParseDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author guosheng.huang
 * @version $id:ElementParseDaoImpl.java, v 0.1 2018年08月12日 15:49 tao.hu Exp $
 */

public class ElementParseDaoImpl {
    public static final String collectionName = "ElementParse";

    @Autowired
    private MongoTemplate mongoTemplate;


    public String save(ElementParseDO elementParseDO) {
        mongoTemplate.save(elementParseDO, collectionName);
        return null;
    }

    public int count() {
        return 0;
    }

    public ElementParseDO queryByUid(String id) {
        return null;
    }


    public List<ElementParseDO> queryAll(String documentUid) {
        return null;
    }
}
