/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component.mysql.impl;

import java.text.MessageFormat;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagull.beedo.common.enums.BeedoResultCodeEnum;
import com.seagull.beedo.common.enums.CommonStatusEnum;
import com.seagull.beedo.common.exception.BeedoCoreException;
import com.seagull.beedo.common.query.DocumentQuery;
import com.seagull.beedo.component.mysql.DocumentComponent;
import com.seagull.beedo.dao.BeedoDocumentDao;
import com.seagull.beedo.dao.domain.BeedoDocument;

/**
 * @author guosheng.huang
 * @version $id:DocumentParseComponent.java, v 0.1 2018年08月12日 13:00 guosheng.huang Exp $
 */
@Component
public class DocumentComponentImpl implements DocumentComponent {
    Logger                   logger = LoggerFactory.getLogger(DocumentComponentImpl.class);

    @Resource
    private BeedoDocumentDao beedoDocumentDao;

    @Override
    public BeedoDocument queryById(Integer id) {
        return beedoDocumentDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<BeedoDocument> queryForPage(DocumentQuery query) {
        Page<BeedoDocument> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        beedoDocumentDao.selectByQuery(query);
        return new PageInfo<>(page);
    }

    @Override
    public void insert(BeedoDocument beedoDocument) {
        int id = beedoDocumentDao.insertSelective(beedoDocument);
        if (id <= 0) {
            logger.error(
                MessageFormat.format("添加BeedoDocument数据失败，" + "BeedoDocument:{0}", beedoDocument));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
    }

    @Override
    public BeedoDocument update(BeedoDocument beedoDocument) {
        int id = beedoDocumentDao.updateByPrimaryKeySelective(beedoDocument);
        if (id <= 0) {
            logger.error(
                MessageFormat.format("更新BeedoDocument数据失败，" + "BeedoDocument:{0}", beedoDocument));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
        return beedoDocumentDao.selectByPrimaryKey(beedoDocument.getId());
    }

    @Override
    public void deleteById(Integer id) {
        BeedoDocument beedoDocument = new BeedoDocument();
        beedoDocument.setId(id);
        beedoDocument.setStatus(CommonStatusEnum.DISABLED.getCode());
        int result = beedoDocumentDao.updateByPrimaryKeySelective(beedoDocument);
        if (result <= 0) {
            logger.error(
                MessageFormat.format("软删除BeedoDocument数据失败，" + "BeedoDocument:{0}", beedoDocument));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
    }
}
