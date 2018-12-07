package com.seagull.beedo.component.mysql.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagull.beedo.common.enums.BeedoResultCodeEnum;
import com.seagull.beedo.common.enums.CommonStatusEnum;
import com.seagull.beedo.common.exception.BeedoCoreException;
import com.seagull.beedo.common.query.ElementQuery;
import com.seagull.beedo.component.mysql.ElementComponent;
import com.seagull.beedo.dao.BeedoElementDao;
import com.seagull.beedo.dao.domain.BeedoElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author guosheng.huang
 * @version $Id: ElementComponent, v1.0 2018年12月07日 12:09 guosheng.huang Exp $
 */
@Component
public class ElementComponentImpl implements ElementComponent {
    Logger                  logger = LoggerFactory.getLogger(ElementComponentImpl.class);

    @Resource
    private BeedoElementDao beedoElementDao;

    @Override
    public BeedoElement queryById(Integer id) {
        return beedoElementDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<BeedoElement> queryForPage(ElementQuery query) {
        Page<BeedoElement> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        beedoElementDao.selectByQuery(query);
        return new PageInfo<>(page);
    }

    @Override
    public List<BeedoElement> queryListByDocumentId(Integer documentId) {
        return beedoElementDao.selectByDocumentId(documentId);
    }

    @Override
    public void insert(BeedoElement beedoElement) {
        int id = beedoElementDao.insertSelective(beedoElement);
        if (id <= 0) {
            logger.error(
                MessageFormat.format("添加BeedoElement数据失败，" + "BeedoElement:{0}", beedoElement));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
    }

    @Override
    public BeedoElement update(BeedoElement beedoElement) {
        int id = beedoElementDao.updateByPrimaryKeySelective(beedoElement);
        if (id <= 0) {
            logger.error(
                MessageFormat.format("更新BeedoElement数据失败，" + "BeedoElement:{0}", beedoElement));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
        return beedoElementDao.selectByPrimaryKey(beedoElement.getId());
    }

    @Override
    public void deleteById(Integer id) {
        BeedoElement beedoElement = new BeedoElement();
        beedoElement.setId(id);
        beedoElement.setStatus(CommonStatusEnum.DISABLED.getCode());
        int result = beedoElementDao.updateByPrimaryKeySelective(beedoElement);
        if (result <= 0) {
            logger.error(
                MessageFormat.format("软删除BeedoElement数据失败，" + "BeedoElement:{0}", beedoElement));
            throw new BeedoCoreException(BeedoResultCodeEnum.DB_OPERATE_FAILED);
        }
    }
}
