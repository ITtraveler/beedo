package com.seagull.beedo.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum ElementStructureTypeEnum {
    STRING("STRING", "字符串"),

    ARRAY("ARRAY", "数组");

    String code;
    String message;

    ElementStructureTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ElementStructureTypeEnum codeOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (ElementStructureTypeEnum enumObj : ElementStructureTypeEnum.values()) {
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
