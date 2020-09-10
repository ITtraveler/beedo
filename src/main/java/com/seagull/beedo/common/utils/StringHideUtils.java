package com.seagull.beedo.common.utils;

/**
 * 字符串隐藏工具类
 *
 * @author guosheng.huang
 * @version Id: StringHideUtils.java, v 0.1 2015年12月9日 下午3:41:52 lijuan.Kang Exp
 */
public class StringHideUtils {

    /**
     * 构建姓名,格式 黄国胜 **胜, 张三 *三
     *
     * @param realName
     * @return
     */
    public static String buildRealName(String realName) {
        int len = StringUtils.length(realName);
        return StringUtils.isBlank(realName) ? "" : (len > 2 ? "**" : "*")
                + realName.substring(StringUtils.length(realName) - 1,
                StringUtils.length(realName));
    }

    /**
     * 构建手机号,格式:183****8888
     *
     * @param cell
     * @return
     */
    public static String buildPhoneNO(String cell) {
        return StringUtils.isBlank(cell) ? "" : (cell.substring(0,
                3) + "****" + cell.substring(7,
                StringUtils.length(cell)));
    }
}
