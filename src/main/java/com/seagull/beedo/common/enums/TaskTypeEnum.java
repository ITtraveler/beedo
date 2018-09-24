package com.seagull.beedo.common.enums;

import team.seagull.common.base.utils.StringUtils;

public enum TaskTypeEnum {
    INIT("INIT", "初始"),

    VALID("VALID", "有效"),

    CLOSE("CLOSE", "关闭");

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
