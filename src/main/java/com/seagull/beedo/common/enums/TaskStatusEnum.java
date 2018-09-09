/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.common.enums;

import team.seagull.common.base.utils.StringUtils;

/**
 * @author guosheng.huang
 * @version $id:TaskStatusEnum.java, v 0.1 2018年08月11日 21:16 tao.hu Exp $
 */
public enum TaskStatusEnum {
    INIT("INIT", "初始"),

    VALID("VALID", "有效"),

    CLOSE("CLOSE", "关闭");

    String code;
    String message;

    TaskStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static TaskStatusEnum codeOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (TaskStatusEnum enumObj : TaskStatusEnum.values()) {
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
