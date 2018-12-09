/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component.mongo.impl;

import com.seagull.beedo.component.mongo.ParseDataComponent;
import com.seagull.beedo.dao.mongodb.OptMongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * 解析数据组件
 *
 * @author guosheng.huang
 * @version ParseDataComponent.java, v 0.1 2018年09月26日 22:28 guosheng.huang Exp $
 */
@Component
public class ParseDataComponentImpl implements ParseDataComponent {

    Logger logger = LoggerFactory.getLogger(ParseDataComponentImpl.class);

    @Autowired
    private OptMongo optMongo;

    /**
     * 保存数据
     *
     * @param object
     * @param collectionName
     * @param indexFields
     */
    @Override
    public void saveData(Object object, String collectionName, List<String> indexFields) {
        for (String indexField : indexFields) {
            if (!optMongo.isExistIndex(indexField, collectionName)) {
                Index index = new Index();
                index.named("INDEX_" + collectionName + "_" + indexField);
                index.on(indexField, Sort.Direction.DESC);
                index.unique();
                optMongo.addIndex(collectionName, index);
            }
        }

        if (object == null) {
            return;
        }

        if (object instanceof HashMap && ((HashMap) object).isEmpty()) {
            return;
        }

        optMongo.insert(object, collectionName);
        logger.info("数据保存到Mongo成功collectionName：{}，data：{}", collectionName, object);
    }
}
