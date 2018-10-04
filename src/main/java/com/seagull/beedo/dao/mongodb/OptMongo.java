package com.seagull.beedo.dao.mongodb;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author guosheng.huang
 * @version $Id: OptMongo, v1.0 2018年09月17日 18:33 guosheng.huang Exp $
 */
@Repository
public class OptMongo {
    private static final Logger logger = LoggerFactory.getLogger(OptMongo.class);

    @Autowired
    protected MongoTemplate mongoTemplate;

    /**
     * 初始化collection,存在则返回collection，不存在则创建一个collection
     *
     * @param collectionName
     * @return
     */
    public MongoCollection<Document> initCollection(String collectionName) {
        MongoCollection<Document> collection;
        if (mongoTemplate.collectionExists(collectionName)) {
            collection = mongoTemplate.createCollection(collectionName);
        } else {
            collection = mongoTemplate.getCollection(collectionName);
        }
        return collection;
    }

    public <T> void insert(T object) {
        try {
            mongoTemplate.insert(object);
        } catch (Exception e) {
            logger.error("mongodb 插入异常object:{0}", object, e);
        }
    }


    public <T> void insert(T object, String collectionName) {
        try {
            mongoTemplate.insert(object, collectionName);
        } catch (Exception e) {
            logger.error("mongodb 插入异常object:{0}", object, e);
        }
    }

    public <T> List<T> find(Query query, Class<T> clazz, String collectionName) {
        List<T> result = mongoTemplate.find(query, clazz, collectionName);
        return result;
    }

    public <T> T findById(String id, Class<T> clazz) {
        T result = mongoTemplate.findById(id, clazz);
        return result;
    }

    public <T> List<T> findAll(Class<T> clazz) {
        List<T> result = mongoTemplate.findAll(clazz);
        return result;
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
        List<T> result = mongoTemplate.find(query, clazz);
        return result;
    }

    public <T> long count(Query query, Class<T> clazz, String collectionName) {
        return mongoTemplate.count(query, clazz, collectionName);
    }

    public long count(Query query, String collectionName) {
        return mongoTemplate.count(query, collectionName);
    }

    public <T> void remove(T object) {
        try {
            mongoTemplate.remove(object);
        } catch (Exception e) {
            logger.error("mongodb 删除异常 object:{0}", object, e);
        }
    }

    public <T> void findAllAndRemove(Query query, Class<T> object) {
        try {
            mongoTemplate.findAllAndRemove(query, object);
        } catch (Exception e) {
            logger.error("mongodb 条件删除异常Query:{0},Object:{1}", query, object, e);
        }
    }

    /**
     * 添加索引
     * @param collectionName
     * @param index
     */
    public void addIndex(String collectionName, Index index){
        IndexOperations indexOperations = mongoTemplate.indexOps(collectionName);
        indexOperations.ensureIndex(index);
    }

    public List<IndexInfo> getAllIndexs(String collectionName){
        IndexOperations indexOperations = mongoTemplate.indexOps(collectionName);
        return indexOperations.getIndexInfo();
    }
}

