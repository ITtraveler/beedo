package com.seagull.beedo.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 有效与无效状态
 * @author guosheng.huang
 * @version $Id: CommonStatusEnum, v1.0 2018年11月10日 15:44 guosheng.huang Exp $
 */
public enum CommonStatusEnum {
                              ENABLED("ENABLED", "有效"),

                              DISABLED("DISABLED", "无效");

    private String code;

    private String message;

    CommonStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CommonStatusEnum codeOf(String code) {
        for (CommonStatusEnum enumObj : CommonStatusEnum.values()) {
            if (StringUtils.equals(code, enumObj.code)) {
                return enumObj;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
