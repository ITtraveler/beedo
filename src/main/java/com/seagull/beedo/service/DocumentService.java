package com.seagull.beedo.service;

import com.seagull.beedo.common.page.PageList;
import com.seagull.beedo.common.query.DocumentQuery;
import com.seagull.beedo.model.BeedoDocumentModel;
import com.seagull.beedo.model.BeedoElementModel;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author guosheng.huang
 * @version $Id: DocumentService, v1.0 2018年12月07日 13:58 guosheng.huang Exp $
 */
public interface DocumentService {

    @Transactional(rollbackFor = Exception.class)
    void saveDocument(BeedoDocumentModel documentParseInfo);

    BeedoDocumentModel getDocumentById(int documentId);


    BeedoElementModel getElementById(int elementId);


    PageList<BeedoDocumentModel> getDocumentPage(DocumentQuery query) ;

    @Transactional(rollbackFor = Exception.class)
    void updateDocument(BeedoDocumentModel documentParseInfo);

    @Transactional(rollbackFor = Exception.class)
    void deleteDocumentById(Integer documentId);
}
