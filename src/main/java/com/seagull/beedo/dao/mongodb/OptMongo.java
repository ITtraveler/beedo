package com.seagull.beedo.dao.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;

/**
 * @author guosheng.huang
 * @version $Id: OptMongo, v1.0 2018年09月17日 18:33 guosheng.huang Exp $
 */
public class OptMongo extends MongoTemplate {
    private static final Logger logger = LoggerFactory.getLogger(OptMongo.class);

    public OptMongo(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName);
    }

    public OptMongo(MongoDbFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    public OptMongo(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter) {
        super(mongoDbFactory, mongoConverter);
    }

    /**
     * 初始化collection,存在则返回collection，不存在则创建一个collection
     *
     * @param collectionName
     * @return
     */
    public MongoCollection<Document> initCollection(String collectionName) {
        MongoCollection<Document> collection;
        if (collectionExists(collectionName)) {
            collection = createCollection(collectionName);
        } else {
            collection = getCollection(collectionName);
        }
        return collection;
    }

    public void insert(Object object) {
        try {
            super.insert(object);
        } catch (Exception e) {
            logger.error("mongodb 插入异常object:{0}", object, e);
        }
    }

    public void insert(Object object, String collectionName) {
        try {
            super.insert(object, collectionName);
        } catch (Exception e) {
            logger.error("mongodb 插入异常object:{0}", object, e);
        }
    }

    /**
     * 分页查询
     *
     * @param skip
     * @param limit
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findForPage(int skip, int limit, Class<T> clazz) {
        Query query = new Query();
        query.limit(limit);
        query.skip(skip);
        List<T> result = super.find(query, clazz);
        return result;
    }

    public DeleteResult remove(Object object) {
        try {
            return super.remove(object);
        } catch (Exception e) {
            logger.error("mongodb 删除异常 object:{0}", object, e);
        }
        return null;
    }

    public <T> List<T> findAllAndRemove(Query query, Class<T> object) {
        try {
            return super.findAllAndRemove(query, object);
        } catch (Exception e) {
            logger.error("mongodb 条件删除异常Query:{0},Object:{1}", query, object, e);
        }
        return null;
    }

    /**
     * 添加索引
     *
     * @param collectionName
     * @param index
     */
    public void addIndex(String collectionName, Index index) {
        IndexOperations indexOperations = super.indexOps(collectionName);
        indexOperations.ensureIndex(index);
    }

    public List<IndexInfo> getAllIndexs(String collectionName) {
        IndexOperations indexOperations = super.indexOps(collectionName);
        return indexOperations.getIndexInfo();
    }

    /**
     * 判断是否存在索引
     * @param field 字段
     * @param clazz
     * @return
     */
    public boolean isExistIndex(String field, Class clazz) {
        IndexOperations indexOperations = super.indexOps(clazz);
        for (IndexInfo indexInfo : indexOperations.getIndexInfo()) {
            if (indexInfo.isIndexForFields(Collections.singletonList(field))) {
                return true;
            }
        }
        return false;
    }

    public boolean isExistIndex(String field, String collectionName) {
        IndexOperations indexOperations = super.indexOps(collectionName);
        for (IndexInfo indexInfo : indexOperations.getIndexInfo()) {
            if (indexInfo.isIndexForFields(Collections.singletonList(field))) {
                return true;
            }
        }
        return false;
    }
}

