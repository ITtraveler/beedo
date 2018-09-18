/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component;

import com.seagull.beedo.dao.DocumentParseDao;
import com.seagull.beedo.dao.DocumentParseDaoImpl;
import com.seagull.beedo.dao.ElementParseDao;
import com.seagull.beedo.dao.dataobject.DocumentParseDO;
import com.seagull.beedo.dao.dataobject.ElementParseDO;
import com.seagull.beedo.model.DocumentParseInfo;
import com.seagull.beedo.model.ElementParseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team.seagull.common.base.common.page.Page;
import team.seagull.common.base.common.page.PageAttribute;
import team.seagull.common.base.common.page.PageList;
import team.seagull.common.base.query.QueryBase;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author guosheng.huang
 * @version $id:DocumentParseComponent.java, v 0.1 2018年08月12日 13:00 tao.hu Exp $
 */
@Component
public class DocumentParseComponent {
    /**
     * logger
     */
    Logger logger = LoggerFactory.getLogger(DocumentParseComponent.class);

    @Autowired
    private DocumentParseDao documentParseDao;

    @Autowired
    private ElementParseDao elementParseDao;

    /**
     * 保存Document 及其 element
     *
     * @param documentParseInfo
     */
    @Transactional
    public void saveDocument(DocumentParseInfo documentParseInfo) {
        DocumentParseDO documentParseDO = new DocumentParseDO();
        BeanUtils.copyProperties(documentParseInfo, documentParseDO);

        DocumentParseDO saveDocument = documentParseDao.save(documentParseDO);
        List<ElementParseInfo> elements = documentParseInfo.getElements();
        elements.forEach(elementParseInfo -> {
            ElementParseDO elementParseDO = new ElementParseDO();
            BeanUtils.copyProperties(elementParseInfo, elementParseDO);
            elementParseDO.setDocumentId(saveDocument.getId());
            elementParseDao.save(elementParseDO);
        });

        logger.info(MessageFormat.format("保存解析的文档数据成功,DocumentParseInfo:{0}", documentParseInfo));
    }

    /**
     * id获取Document
     *
     * @param documentId
     * @return
     */
    public DocumentParseInfo getDocumentById(int documentId) {
        DocumentParseInfo documentParseInfo = new DocumentParseInfo();

        //document
        Optional<DocumentParseDO> optionalDo = documentParseDao.findById(documentId);
        BeanUtils.copyProperties(optionalDo.get(), documentParseInfo);

        //elements
        List<ElementParseInfo> elementParseInfos = new ArrayList<>();
        documentParseInfo.setElements(elementParseInfos);
        List<ElementParseDO> elementParseDOS = elementParseDao.findByDocumentIdOrderByGmtCreateDesc(documentId);
        elementParseDOS.forEach(elementParseDO -> {
            ElementParseInfo elementParseInfo = new ElementParseInfo();
            BeanUtils.copyProperties(elementParseDO, elementParseInfo);
            elementParseInfos.add(elementParseInfo);
        });

        return documentParseInfo;
    }

    /**
     * 获取element
     *
     * @param elementId
     * @return
     */
    public ElementParseInfo getElementById(int elementId) {
        ElementParseInfo elementParseInfo = new ElementParseInfo();
        Optional<ElementParseDO> elementParseDO = elementParseDao.findById(elementId);
        BeanUtils.copyProperties(elementParseDO.get(), elementParseInfo);
        return elementParseInfo;
    }

    /**
     * 分页获取Document
     *
     * @param queryBase
     * @return
     */
    public PageList<DocumentParseInfo> getDocumentPage(QueryBase queryBase) {
        org.springframework.data.domain.Page<DocumentParseDO> documentParsePage = documentParseDao.findAll
                (PageRequest.of(queryBase.pageNum - 1, queryBase.getPageSize()));
        Iterator<DocumentParseDO> iterator = documentParsePage.iterator();
        ArrayList<DocumentParseInfo> documentParseInfoList = new ArrayList<>();

        //转换处理
        while (iterator.hasNext()) {
            DocumentParseDO documentParseDO = iterator.next();
            DocumentParseInfo documentParseInfo = new DocumentParseInfo();
            BeanUtils.copyProperties(documentParseDO, documentParseInfo);
            documentParseInfoList.add(documentParseInfo);

            //document对应的所有element
            List<ElementParseInfo> elementParseInfoList = new ArrayList<>();
            List<ElementParseDO> elementParseDOList = elementParseDao.findByDocumentIdOrderByGmtCreateDesc
                    (documentParseDO.getId());
            elementParseDOList.forEach(elementParseDO -> {
                ElementParseInfo elementParseInfo = new ElementParseInfo();
                BeanUtils.copyProperties(elementParseDO, elementParseInfo);
                elementParseInfoList.add(elementParseInfo);
            });

            documentParseInfo.setElements(elementParseInfoList);
        }

        int count = (int) documentParseDao.count();
        Page page = Page.getInstance(new PageAttribute(queryBase.getPageNum(), queryBase.getPageSize()), count);
        PageList<DocumentParseInfo> pageList = PageList.getInstance(
                documentParseInfoList, page);
        return pageList;
    }

    @Transactional
    public boolean updateDocument(DocumentParseInfo documentParseInfo) {
        if (documentParseInfo.getId() == null || documentParseInfo.getId() <= 0) {
            return false;
        }

        int isUpdate = documentParseDao.updateNameById(documentParseInfo.getName(), documentParseInfo.getId());
        if (isUpdate == 0) {
            logger.error(MessageFormat.format("更新解析的文档数据失败,DocumentParseInfo:{0}", documentParseInfo));
            return false;
        }

        elementParseDao.deleteByDocumentId(documentParseInfo.getId());
        List<ElementParseInfo> elements = documentParseInfo.getElements();
        elements.forEach(elementParseInfo -> {
            ElementParseDO elementParseDO = new ElementParseDO();
            BeanUtils.copyProperties(elementParseInfo, elementParseDO);
            elementParseDO.setDocumentId(documentParseInfo.getId());
            elementParseDao.save(elementParseDO);
        });
        return true;
    }

    @Transactional
    public void deleteDocumentById(Integer documentId) {
        documentParseDao.deleteById(documentId);
        elementParseDao.deleteByDocumentId(documentId);
        logger.info(MessageFormat.format("删除解析的文档数据成功,documentId:{0}", documentId));
    }

    @Transactional
    public void deleteElementById(Integer elementId) {
        elementParseDao.deleteById(elementId);
        logger.info(MessageFormat.format("删除解析的文档元素数据成功,elementId:{0}", elementId));
    }
}
