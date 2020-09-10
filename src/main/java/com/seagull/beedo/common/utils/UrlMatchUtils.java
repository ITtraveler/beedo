package com.seagull.beedo.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hgs
 * @version Utils.java, v 0.1 2017/12/07 21:05 hgs Exp $
 */
public class UrlMatchUtils {
    //url正则表达式（包含中文）
    private static final String urlRegex = "(file|ftp|http|https?)://[-A-Za-z0-9+&@#/%?=~_|!:,.;\\u4e00-\\u9fa5]+";

    /**
     * url 合法性
     * @param url
     * @return
     */
    public static boolean urlValid(String url) {
        return url.matches(urlRegex);
    }


    public static List<String> matchUrls(String content){
        List<String> urlList = new ArrayList<String>();
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            urlList.add(matcher.group());
        }
        return urlList;
    }
}
