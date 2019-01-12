package com.seagull.beedo.service.impl;

import com.github.pagehelper.PageInfo;
import com.seagull.beedo.common.enums.ElementDataTypeEnum;
import com.seagull.beedo.common.enums.ElementStructureTypeEnum;
import com.seagull.beedo.common.query.DocumentQuery;
import com.seagull.beedo.component.mysql.DocumentComponent;
import com.seagull.beedo.component.mysql.ElementComponent;
import com.seagull.beedo.dao.domain.BeedoDocument;
import com.seagull.beedo.dao.domain.BeedoElement;
import com.seagull.beedo.model.BeedoDocumentModel;
import com.seagull.beedo.model.BeedoElementModel;
import com.seagull.beedo.service.DocumentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import team.seagull.common.base.common.page.Page;
import team.seagull.common.base.common.page.PageAttribute;
import team.seagull.common.base.common.page.PageList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guosheng.huang
 * @version $Id: DocumentServiceImpl, v1.0 2018年12月07日 13:59 guosheng.huang Exp $
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentComponent documentComponent;

    @Autowired
    private ElementComponent elementComponent;

    /**
     * 保存Document 及其 element
     *
     * @param beedoDocumentModel
     */
    @Override
    public void saveDocument(BeedoDocumentModel beedoDocumentModel) {
        BeedoDocument beedoDocument = new BeedoDocument();
        BeanUtils.copyProperties(beedoDocumentModel, beedoDocument);

        documentComponent.insert(beedoDocument);

        insertElements(beedoDocument.getId(), beedoDocumentModel.getElements());
    }

    /**
     * id获取Document
     *
     * @param id
     * @return
     */
    @Override
    public BeedoDocumentModel getDocumentById(int id) {
        BeedoDocumentModel documentParseInfo = new BeedoDocumentModel();

        //document
        BeedoDocument beedoDocument = documentComponent.queryById(id);
        BeanUtils.copyProperties(beedoDocument, documentParseInfo);

        //document对应的所有element
        setElements(id, documentParseInfo);

        return documentParseInfo;
    }

    /**
     * 获取element
     *
     * @param elementId
     * @return
     */

    @Override
    public BeedoElementModel getElementById(int elementId) {
        BeedoElementModel elementModel = new BeedoElementModel();
        BeedoElement beedoElement = elementComponent.queryById(elementId);
        if (beedoElement != null) {
            BeanUtils.copyProperties(beedoElement, elementModel);
            elementModel.setStructureType(ElementStructureTypeEnum.codeOf(beedoElement.getStructureType()));
            elementModel.setDataType(ElementDataTypeEnum.codeOf(beedoElement.getDataType()));
            return elementModel;
        }
        return null;
    }

    /**
     * 分页获取Document
     *
     * @param query
     * @return
     */
    @Override
    public PageList<BeedoDocumentModel> getDocumentPage(DocumentQuery query) {
        PageInfo<BeedoDocument> documentPage = documentComponent.queryForPage(query);

        ArrayList<BeedoDocumentModel> documentParseInfos = new ArrayList<>();

        //转换处理
        for (BeedoDocument beedoDocument : documentPage.getList()) {
            BeedoDocumentModel documentParseInfo = new BeedoDocumentModel();
            BeanUtils.copyProperties(beedoDocument, documentParseInfo);

            //document对应的所有element
            setElements(beedoDocument.getId(), documentParseInfo);

            documentParseInfos.add(documentParseInfo);
        }

        Page page = Page.getInstance(new PageAttribute(query.getPageNum(), query.getPageSize()),
                (int)documentPage.getTotal());
        PageList<BeedoDocumentModel> pageList = PageList.getInstance(documentParseInfos, page);
        return pageList;
    }

    @Override
    public void updateDocument(BeedoDocumentModel beedoDocumentModel) {
        Assert.isTrue(beedoDocumentModel.getId() != null || beedoDocumentModel.getId() <= 0,
                "更新beedoDocumentModel失败，id需>=0");

        BeedoDocument beedoDocument = new BeedoDocument();
        BeanUtils.copyProperties(beedoDocumentModel, beedoDocument);
        BeedoDocument updateDocument = documentComponent.update(beedoDocument);

        deleteElements(updateDocument.getId());

        List<BeedoElementModel> elements = beedoDocumentModel.getElements();
        insertElements(beedoDocumentModel.getId(), elements);
    }

    @Override
    public void deleteDocumentById(Integer documentId) {
        documentComponent.deleteById(documentId);
        deleteElements(documentId);
    }

    /**
     * 删除elements
     *
     * @param documentId
     */
    private void deleteElements(Integer documentId) {
        List<BeedoElement> beedoElements = elementComponent.queryListByDocumentId(documentId);
        if (CollectionUtils.isEmpty(beedoElements)) {
            return;
        }

        for (BeedoElement beedoElement : beedoElements) {
            elementComponent.deleteById(beedoElement.getId());
        }
    }

    /**
     * 保持Document的元素
     *
     * @param documentId
     * @param elements
     */
    private void insertElements(Integer documentId, List<BeedoElementModel> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return;
        }

        elements.forEach(elementParseInfo -> {
            BeedoElement beedoElement = new BeedoElement();
            BeanUtils.copyProperties(elementParseInfo, beedoElement);
            beedoElement.setDocumentId(documentId);
            beedoElement.setDataType(elementParseInfo.getDataType().getCode());
            beedoElement.setStructureType(elementParseInfo.getStructureType().getCode());
            elementComponent.insert(beedoElement);
        });
    }

    private void setElements(int id, BeedoDocumentModel documentParseInfo) {
        List<BeedoElementModel> elementModels = new ArrayList<>();
        List<BeedoElement> beedoElements = elementComponent.queryListByDocumentId(id);
        beedoElements.forEach(beedoElement -> {
            BeedoElementModel beedoElementModel = new BeedoElementModel();
            BeanUtils.copyProperties(beedoElement, beedoElementModel);
            beedoElementModel.setDataType(ElementDataTypeEnum.codeOf(beedoElement.getDataType()));
            beedoElementModel.setStructureType(ElementStructureTypeEnum.codeOf(beedoElement.getStructureType()));
            elementModels.add(beedoElementModel);
        });
        documentParseInfo.setElements(elementModels);
    }
}
