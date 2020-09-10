package com.seagull.beedo.common.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Created by hgs on 2017/6/26.
 */
public class JsoupUtilSingleton {
    private static final int timeOut = 1000 * 60;
    private static JsoupUtilSingleton jsoupUtilSingleton;
    private Document document;
    private Map<String, String> cookieMap = new HashMap<String, String>();

    public JsoupUtilSingleton() {
    }

    public static JsoupUtilSingleton getJsoupUtilSingleton() {
        if (jsoupUtilSingleton == null) {
            jsoupUtilSingleton = new JsoupUtilSingleton();
        }
        return jsoupUtilSingleton;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookieMap = cookies;
    }

    private Connection connection(String url) {
        Connection connect = Jsoup.connect(url);
        connect.cookies(cookieMap);
        return connect;
    }

    /**
     * 进行get 得到Document
     *
     * @param url
     * @return
     */
    public Document getConnect(String url) {
        if (document == null || !document.baseUri().equals(url)) {
            try {
                document = connection(url).timeout(timeOut).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return document;
    }

    public Elements getElements(Document document, String cssQuery) {
        return document.select(cssQuery);
    }


    /**
     * 返回map: key-text   value-url
     *
     * @param url
     * @param cssQuery
     * @return
     */
    public Map<String, String> getMapResult(String url, String cssQuery) {
        Elements elements = getElements(getConnect(url), cssQuery);
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Element element : elements) {
            map.put(element.text(), element.attr("href"));
        }
        return map;
    }

    /**
     * 获取String集合
     *
     * @param url
     * @param cssQuery
     * @param type     0-href,1-text
     * @return
     */
    public List<String> getListResult(String url, String cssQuery, int type) {
        List<String> list = new ArrayList<String>();
        Elements elements = getElements(getConnect(url), cssQuery);
        for (Element e : elements) {
            switch (type) {
                case 0:
                    list.add(e.attr("href"));
                    break;
                case 1:
                    list.add(e.text());
                    break;
            }
        }
        return list;
    }


    /**
     * @param url
     * @param cssQuery
     * @return
     */
    public String getContent(String url, String cssQuery) {
        return getText(url, cssQuery);
    }

    public String getText(String url, String cssQuery) {
        Elements elements = getElements(getConnect(url), cssQuery);
        return elements.text();
    }

    public List<String> getTextList(String url, String cssQuery) {
        List<String> list = new ArrayList<>();
        Elements elements = getElements(getConnect(url), cssQuery);

        for (Element e : elements) {
            list.add(e.text());
        }
        return list;
    }

    /**
     * 获取html内容
     *
     * @param url
     * @param cssQuery
     * @return
     */
    public String getHtml(String url, String cssQuery) {
        Elements elements = getElements(getConnect(url), cssQuery);
        return elements.html();
    }

    /**
     * 获取html内容 list
     *
     * @param url
     * @param cssQuery
     * @return
     */
    public List<String> getHtmlList(String url, String cssQuery) {
        List<String> list = new ArrayList<>();
        Elements elements = getElements(getConnect(url), cssQuery);

        for (Element e : elements) {
            list.add(e.html());
        }
        return list;
    }

    /**
     * 获取属性内容
     *
     * @param url
     * @param cssQuery
     * @param attr     属性如href，value，style等属性值
     * @return
     */
    public String getAttr(String url, String cssQuery, String attr) {
        Elements elements = getElements(getConnect(url), cssQuery);
        return elements.attr(attr);
    }

    /**
     * 获取属性内容 list
     *
     * @param url
     * @param cssQuery
     * @param attr     属性如href，value，style等属性值
     * @return
     */
    public List<String> getAttrList(String url, String cssQuery, String attr) {
        List<String> list = new ArrayList<>();
        Elements elements = getElements(getConnect(url), cssQuery);

        for (Element e : elements) {
            list.add(e.attr(attr));
        }
        return list;
    }
}
