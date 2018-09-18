package com.seagull.beedo.common.utils;

import team.seagull.common.base.utils.UrlMatchUtils;

public class OptimizeUtils {
    public static String getVaildUrl(String baseUrl, String url) {
        url = url.replace(" ", "").replace("\"", "").replace("\'", "");
        if (UrlMatchUtils.urlValid(url)) {
            return url;
        }

        if (baseUrl.lastIndexOf("/") == baseUrl.length()) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        if (baseUrl.indexOf("/") == 0) {
            baseUrl = baseUrl.substring(1);
        }
        return baseUrl + "/" + url;
    }


}
