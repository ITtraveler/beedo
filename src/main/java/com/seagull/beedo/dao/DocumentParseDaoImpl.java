/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao;

import com.seagull.beedo.dao.dataobject.DocumentParseDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import team.seagull.common.base.query.QueryBase;

import java.util.List;

/**
 * @author guosheng.huang
 * @version $id:DocumentParseDaoImpl.java, v 0.1 2018年08月12日 12:45 guosheng.huang Exp $
 */

public class DocumentParseDaoImpl  {

    public static final String collectionName = "DocumentParse";

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(DocumentParseDO documentParseDO) {
        mongoTemplate.save(documentParseDO, collectionName);
    }

    public int count() {
        return (int) mongoTemplate.count(new Query(), DocumentParseDO.class, collectionName);
    }


    public DocumentParseDO queryByUid(String id) {
        return mongoTemplate.findById(id, DocumentParseDO.class, collectionName);
    }


    public List<DocumentParseDO> queryForList(QueryBase queryBase) {
        Query query = new Query();
        long count = mongoTemplate.count(query, DocumentParseDO.class, collectionName);
        query.limit(queryBase.getPageSize());
        query.skip(queryBase.getSkip(count));
        return mongoTemplate.find(query, DocumentParseDO.class, collectionName);
    }


}
