/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author guosheng.huang
 * @version $id:TaskStatusEnum.java, v 0.1 2018年08月11日 21:16 guosheng.huang Exp $
 */
public enum TaskStatusEnum {
    INIT("INIT", "初始"),

    VALID("VALID", "有效"),

    // todo 最好新增字段存放，否则容易与其它状态冲突
    MODIFIED("MODIFIED", "被修改的"),

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
