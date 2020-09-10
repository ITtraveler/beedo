/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.core;



import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author guosheng.huang
 * @version UrlExpression.java, v 0.1 2018年12月08日 12:48 guosheng.huang Exp $
 */
public class UrlExpression {

    /**
     * 匹配花括号的表达式
     *
     * @param url
     * @return
     */
    private static List<String> patternExpression(String url) {
        List<String> expressions = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{[\\w-]+\\}");
        Matcher matcher = pattern.matcher(url);

        while (matcher.find()) {
            expressions.add(matcher.group());
        }

        return expressions;
    }

    public static void main(String[] args) {
        String url = "https://zhidao.baidu.com/question/{0-19}";
        List<String> strings = getResultUrl(url);
    }

    /**
     * 获取经过处理的urls
     *
     * @param originalUrl
     * @return
     */
    public static List<String> getResultUrl(String originalUrl) {

        List<String> expressions = patternExpression(originalUrl);

        return getUrls(originalUrl, expressions);

    }

    /**
     * 暂只支持对一个表达式进行替换
     *
     * @param originalUrl
     * @param expressions
     * @return
     */
    private static List<String> getUrls(String originalUrl, List<String> expressions) {

        List<String> urls = new ArrayList<>();

        if (CollectionUtils.isEmpty(expressions)) {
            urls.add(originalUrl);
            return urls;
        }

        String expression = expressions.get(0);
        String expValue = expression.replace("{", "").replace("}", "");

        if (expValue.matches("[0-9]+-[0-9]+")) {

            String[] split = expValue.split("-");
            int start = Integer.valueOf(split[0]);
            int end = Integer.valueOf(split[1]);
            for (int i = start; i <= end; i++) {
                urls.add(originalUrl.replace(expression, "" + i));
            }
        }

        // TODO: 2018/12/8 更丰富的匹配

        return urls;
    }

}
