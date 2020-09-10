/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.web.controller;

import com.seagull.beedo.common.enums.CommonStatusEnum;
import com.seagull.beedo.common.page.PageList;
import com.seagull.beedo.common.page.PageQueryResultConvert;
import com.seagull.beedo.common.result.CommonResult;
import com.seagull.beedo.common.result.PageListResult;
import com.seagull.beedo.common.utils.JsoupUtils;
import com.seagull.beedo.common.utils.UrlMatchUtils;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seagull.beedo.common.query.DocumentQuery;
import com.seagull.beedo.model.BeedoDocumentModel;
import com.seagull.beedo.model.BeedoElementModel;
import com.seagull.beedo.service.DocumentService;


@RestController
@RequestMapping("/document")
public class BeedoDocumentController extends BaseController {

    @Autowired
    private DocumentService documentService;

    /**
     * 获取URL的HTML
     *
     * @param parseUrl
     * @return
     */
    @GetMapping("/parse/url")
    public Object getUrlSource(String parseUrl) {
        CommonResult<String> result = new CommonResult<>();

        boolean urlValid = UrlMatchUtils.urlValid(parseUrl);
        if (!urlValid) {
            retFail(result, "url不合法！");
        }

        String content = JsoupUtils.getConnect(parseUrl.replace(" ", "")).outerHtml();
        result.setData(content);
        return result;
    }

    @GetMapping("/parse/query")
    public Object getUrlSource(String parseUrl, BeedoElementModel elementParseInfo) {
        CommonResult<String> result = new CommonResult<>();

        boolean urlValid = UrlMatchUtils.urlValid(parseUrl);
        if (!urlValid) {
            retFail(result, "url不合法！");
        }

        Elements elements = JsoupUtils.getElements(JsoupUtils.getConnect(parseUrl.replace(" ", "")),
            elementParseInfo.getCssQuery());

        result.setData(elements.outerHtml());
        return result;
    }

    /**
     * 保存Document
     *
     * @param documentParseInfo
     * @return
     */
    @PostMapping
    public Object saveParseDocument(@RequestBody BeedoDocumentModel documentParseInfo) {
        CommonResult<String> result = new CommonResult<>();
        boolean urlValid = UrlMatchUtils
            .urlValid(documentParseInfo.getProtocol() + documentParseInfo.getUrl());
        if (!urlValid) {
            retFail(result, "url不合法！");
            return result;
        }

        if (CollectionUtils.isEmpty(documentParseInfo.getElements())) {
            retFail(result, "没有解析的节点");
            return result;
        }

        documentService.saveDocument(documentParseInfo);
        return result;
    }

    /**
     * 更加Id获取Document
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Object getParseDocumentById(@PathVariable int id) {
        CommonResult<BeedoDocumentModel> result = new CommonResult<>();
        if (id <= 0) {
            retFail(result, "查询失败");
            return result;
        }
        result.setData(documentService.getDocumentById(id));
        return result;
    }

    /**
     * 分页获取Document
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/page/{page}/size/{size}")
    public Object getParseDocuments(@PathVariable int page, @PathVariable int size) {
        PageListResult<BeedoDocumentModel> result = new PageListResult<>();
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setStatus(CommonStatusEnum.ENABLED.getCode());
        documentQuery.setPageNum(page);
        documentQuery.setPageSize(size);
        PageList<BeedoDocumentModel> documentPage = documentService.getDocumentPage(documentQuery);
        PageQueryResultConvert.converToResult(result, documentPage);
        result.setCurrentPage(page);
        return result;
    }

    /**
     * 修改Document及Element
     *
     * @param documentParseInfo
     * @return
     */
    @PutMapping
    public Object putParseDocument(@RequestBody BeedoDocumentModel documentParseInfo) {
        CommonResult<String> result = new CommonResult<>();
        documentService.updateDocument(documentParseInfo);
        return result;
    }

    /**
     * 删除Document
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public Object deleteParseDocument(@PathVariable int id) {
        CommonResult<String> result = new CommonResult<>();
        documentService.deleteDocumentById(id);
        return result;
    }
}
