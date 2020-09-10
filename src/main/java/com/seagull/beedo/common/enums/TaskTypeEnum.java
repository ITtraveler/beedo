/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author guosheng.huang
 * @version TaskTypeEnum.java, v 0.1 2018年12月09日 12:39 guosheng.huang Exp $
 */
public enum TaskTypeEnum {
    URL_EXPRESSION("URL_EXPRESSION", "URL表达式"),

    ELEMENT("ELEMENT", "元素组合");

    String code;
    String message;

    TaskTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static TaskTypeEnum codeOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (TaskTypeEnum enumObj : TaskTypeEnum.values()) {
            if (enumObj.code.equals(code.toUpperCase())) {
                return enumObj;
            }
        }
        return null;
    }


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
