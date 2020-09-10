package com.seagull.beedo.common.enums;

import org.apache.commons.lang3.StringUtils;


public enum ElementDataTypeEnum {
    TEXT("TEXT", "文本"),

    HTML("HTML", "HTML"),

    ATTR("ATTR", "属性"),

    URL("URL", "链接");

    String code;
    String message;


    ElementDataTypeEnum(String code, String message) {

        this.code = code;
        this.message = message;
    }

    public static ElementDataTypeEnum codeOf(String code) {
        if (StringUtils.isBlank(code)){
            return null;
        }

        for (ElementDataTypeEnum enumObj : ElementDataTypeEnum.values()) {
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
