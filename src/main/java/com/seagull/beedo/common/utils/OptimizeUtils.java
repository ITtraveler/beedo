package com.seagull.beedo.common.utils;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OptimizeUtils {
    public static String getVaildUrl(String baseUrl, String url) {
        url = url.replace(" ", "").replace("\"", "").replace("\'", "");
        if (UrlMatchUtils.urlValid(url)) {
            return url;
        }

        baseUrl = getBaseUrl(baseUrl);
        String protocol = baseUrl.substring(0, baseUrl.indexOf("/", 2));
        if(url.substring(0,2).equals("//")){
            return protocol+url;
        }

        if (baseUrl.lastIndexOf("/") == baseUrl.length()) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        if (url.indexOf("/") == 0) {
            url = url.substring(1);
        }
        return baseUrl + "/" + url;
    }

    /**
     * 进一步处理获取域名
     *
     * @param url
     * @return
     */
    private static String getBaseUrl(String url) {
        Pattern pattern = Pattern.compile("(file|ftp|http|https?)://[-A-Za-z0-9+&@#%?=~_|!:,.;\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        return url;
    }


}
