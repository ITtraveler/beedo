/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.web.controller;

import com.seagull.beedo.component.DocumentParseComponent;
import com.seagull.beedo.model.DocumentParseInfo;
import com.seagull.beedo.model.ElementParseInfo;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.seagull.common.base.common.page.PageList;
import team.seagull.common.base.common.page.PageQueryResultConvert;
import team.seagull.common.base.query.QueryBase;
import team.seagull.common.base.result.CommonResult;
import team.seagull.common.base.result.PageListResult;
import team.seagull.common.base.utils.CollectionUtils;
import team.seagull.common.base.utils.JsoupUtilSingleton;
import team.seagull.common.base.utils.JsoupUtils;
import team.seagull.common.base.utils.UrlMatchUtils;

@RestController
@RequestMapping("/document")
public class BeedoDocumentController extends BaseController {

    @Autowired
    private DocumentParseComponent documentParseComponent;

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
    public Object getUrlSource(String parseUrl, ElementParseInfo elementParseInfo) {
        CommonResult<String> result = new CommonResult<>();

        boolean urlValid = UrlMatchUtils.urlValid(parseUrl);
        if (!urlValid) {
            retFail(result, "url不合法！");
        }


        Elements elements = JsoupUtils.getElements(JsoupUtils
                .getConnect(parseUrl.replace(" ", "")), elementParseInfo.getCssQuery());

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
    public Object saveParseDocument(@RequestBody DocumentParseInfo documentParseInfo) {
        CommonResult<String> result = new CommonResult<>();
        boolean urlValid = UrlMatchUtils.urlValid(documentParseInfo.getProtocol() + documentParseInfo.getUrl());
        if (!urlValid) {
            retFail(result, "url不合法！");
            return result;
        }

        if (CollectionUtils.isEmpty(documentParseInfo.getElements())) {
            retFail(result, "没有解析的节点");
            return result;
        }

        documentParseComponent.saveDocument(documentParseInfo);
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
        CommonResult<DocumentParseInfo> result = new CommonResult<>();
        if (id <= 0) {
            retFail(result, "查询失败");
            return result;
        }
        result.setData(documentParseComponent.getDocumentById(id));
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
        PageListResult<DocumentParseInfo> result = new PageListResult<>();
        PageList<DocumentParseInfo> documentPage = documentParseComponent.getDocumentPage(new QueryBase(page,
                size));
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
    public Object putParseDocument(@RequestBody DocumentParseInfo documentParseInfo) {
        CommonResult<String> result = new CommonResult<>();
        boolean update = documentParseComponent.updateDocument(documentParseInfo);
        if (!update) {
            retFail(result);
        }
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
        documentParseComponent.deleteDocumentById(id);
        return result;
    }
}
