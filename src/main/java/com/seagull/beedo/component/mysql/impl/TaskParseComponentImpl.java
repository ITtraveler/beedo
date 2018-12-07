/**
 * yingyinglicai.com Inc.
 * Copyright (c) 2013-2018 All Rights Reserved.
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
import com.seagull.beedo.common.query.TaskParseQuery;
import com.seagull.beedo.component.mysql.TaskParseComponent;
import com.seagull.beedo.dao.BeedoTaskParseDao;
import com.seagull.beedo.dao.domain.BeedoTaskParse;

/**
 * @author guosheng.huang
 * @version $Id: ElementComponent, v1.0 2018年12月07日 12:09 guosheng.huang Exp $
 */
@Component
public class TaskParseComponentImpl implements TaskParseComponent {
    Logger                    logger = LoggerFactory.getLogger(TaskParseComponentImpl.class);

    @Resource
    private BeedoTaskParseDao beedoTaskParseDao;

    @Override
    public BeedoTaskParse queryById(Integer id) {
        return beedoTaskParseDao.selectByPrimaryKey(id);
    }

    @Override
    public BeedoTaskParse queryByUid(String uid) {
        return beedoTaskParseDao.selectByUid(uid);
    }

    @Override
    public PageInfo<BeedoTaskParse> queryForPage(TaskParseQuery query) {
        Page<BeedoTaskParse> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        beedoTaskParseDao.selectByQuery(query);
        return new PageInfo<>(page);
    }

    @Override
    public BeedoTaskParse insert(BeedoTaskParse beedoTaskParse) {
        int id = beedoTaskParseDao.insertSelective(beedoTaskParse);
        if (id <= 0) {
            logger.error(MessageFormat.format("添加BeedoTaskParse数据失败，" + "BeedoTaskParse:{0}",
                beedoTaskParse));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
        return beedoTaskParse;
    }

    @Override
    public BeedoTaskParse update(BeedoTaskParse beedoTaskParse) {
        int id = beedoTaskParseDao.updateByPrimaryKeySelective(beedoTaskParse);
        if (id <= 0) {
            logger.error(MessageFormat.format("更新BeedoTaskParse数据失败，" + "BeedoTaskParse:{0}",
                beedoTaskParse));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
        return beedoTaskParseDao.selectByPrimaryKey(beedoTaskParse.getId());
    }

    @Override
    public void deleteById(Integer id) {
        BeedoTaskParse beedoTaskParse = new BeedoTaskParse();
        beedoTaskParse.setId(id);
        beedoTaskParse.setStatus(CommonStatusEnum.DISABLED.getCode());
        int result = beedoTaskParseDao.updateByPrimaryKeySelective(beedoTaskParse);
        if (result <= 0) {
            logger.error(MessageFormat.format("软删除BeedoTaskParse数据失败，" + "BeedoTaskParse:{0}",
                beedoTaskParse));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
    }

    @Override
    public void deleteByUid(String uid) {
        BeedoTaskParse beedoTaskParse = new BeedoTaskParse();
        beedoTaskParse.setUid(uid);
        beedoTaskParse.setStatus(CommonStatusEnum.DISABLED.getCode());
        int result = beedoTaskParseDao.updateByPrimaryKeySelective(beedoTaskParse);
        if (result <= 0) {
            logger.error(MessageFormat.format("软删除BeedoTaskParse数据失败，" + "BeedoTaskParse:{0}",
                beedoTaskParse));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
    }
}
