package com.seagull.beedo.common.enums;

import team.seagull.common.base.utils.StringUtils;


public enum ParseDataTypeEnum {
    TEXT("TEXT", "文本"),

    HTML("HTML", "HTML"),

    ATTR("ATTR", "属性");

    String code;
    String message;


    ParseDataTypeEnum(String code, String message) {

        this.code = code;
        this.message = message;
    }

    public static ParseDataTypeEnum codeOf(String code) {
        if (StringUtils.isBlank(code)){
            return null;
        }

        for (ParseDataTypeEnum enumObj : ParseDataTypeEnum.values()) {
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
