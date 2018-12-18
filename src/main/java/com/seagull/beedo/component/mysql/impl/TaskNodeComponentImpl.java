package com.seagull.beedo.component.mysql.impl;

import java.text.MessageFormat;
import java.util.List;

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
import com.seagull.beedo.common.query.TaskNodeQuery;
import com.seagull.beedo.component.mysql.TaskNodeComponent;
import com.seagull.beedo.dao.BeedoTaskNodeDao;
import com.seagull.beedo.dao.domain.BeedoTaskNode;

/**
 * @author guosheng.huang
 * @version $Id: ElementComponent, v1.0 2018年12月07日 12:09 guosheng.huang Exp $
 */
@Component
public class TaskNodeComponentImpl implements TaskNodeComponent {
    Logger                   logger = LoggerFactory.getLogger(TaskNodeComponentImpl.class);

    @Resource
    private BeedoTaskNodeDao beedoTaskNodeDao;

    @Override
    public BeedoTaskNode queryById(Integer id) {
        return beedoTaskNodeDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<BeedoTaskNode> queryForPage(TaskNodeQuery query) {
        Page<BeedoTaskNode> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        beedoTaskNodeDao.selectByQuery(query);
        return new PageInfo<>(page);
    }

    @Override
    public List<BeedoTaskNode> queryListByTaskParseUid(String taskParseUid) {
        return beedoTaskNodeDao.selectByTaskParseUid(taskParseUid);
    }

    @Override
    public void insert(BeedoTaskNode beedoTaskNode) {
        int id = beedoTaskNodeDao.insertSelective(beedoTaskNode);
        if (id <= 0) {
            logger.error(
                MessageFormat.format("添加BeedoTaskNode数据失败，" + "BeedoTaskNode:{0}", beedoTaskNode));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
    }

    @Override
    public BeedoTaskNode update(BeedoTaskNode beedoTaskNode) {
        int id = beedoTaskNodeDao.updateByPrimaryKeySelective(beedoTaskNode);
        if (id <= 0) {
            logger.error(
                MessageFormat.format("更新BeedoTaskNode数据失败，" + "BeedoTaskNode:{0}", beedoTaskNode));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
        return beedoTaskNodeDao.selectByPrimaryKey(beedoTaskNode.getId());
    }

    @Override
    public void deleteById(Integer id) {
        BeedoTaskNode beedoTaskNode = new BeedoTaskNode();
        beedoTaskNode.setId(id);
        beedoTaskNode.setStatus(CommonStatusEnum.DISABLED.getCode());
        int result = beedoTaskNodeDao.updateByPrimaryKeySelective(beedoTaskNode);
        if (result <= 0) {
            logger.error(
                MessageFormat.format("软删除BeedoTaskNode数据失败，" + "BeedoTaskNode:{0}", beedoTaskNode));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
    }
}
