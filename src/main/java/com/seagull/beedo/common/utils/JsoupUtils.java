package com.seagull.beedo.common.utils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hgs on 2017/6/26.
 */
public class JsoupUtils {

    /**
     * 进行get 得到Document
     *
     * @param url
     * @return
     */
    public static Document getConnect(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static Elements getElements(Document document, String cssQuery) {
        return document.select(cssQuery);
    }


    /**
     * 返回map: key-text   value-url
     * @param url
     * @param cssQuery
     * @return
     */
    public static Map<String,String> getMapResult(String url, String cssQuery) {
        Elements elements = getElements(getConnect(url), cssQuery);
        Map<String,String> map = new LinkedHashMap<String,String>();
        for (Element element : elements) {
            map.put(element.text(),element.attr("href"));
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
    public static List<String> getListResult(String url, String cssQuery, int type) {
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
    public static String getContent(String url, String cssQuery) {
        Elements elements = getElements(getConnect(url), cssQuery);
        return elements.text();
    }


}
