/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component;

import com.seagull.beedo.dao.mongodb.OptMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 解析数据组件
 *
 * @author guosheng.huang
 * @version ParseDataComponent.java, v 0.1 2018年09月26日 22:28 guosheng.huang Exp $
 */
@Component
public class ParseDataComponent {

    @Autowired
    private OptMongo optMongo;

    /**
     * 保存数据
     * @param object
     * @param collectionName
     * @param indexFields
     */
    public void saveData(Object object, String collectionName, List<String> indexFields) {
        for (String indexField : indexFields) {
            Index index = new Index();
            index.named("INDEX_" + collectionName + "_" + indexField);
            index.on(indexField, Sort.Direction.DESC);
            index.unique();
            optMongo.addIndex(collectionName, index);
        }
        optMongo.insert(object, collectionName);
    }
}
