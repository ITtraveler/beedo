package com.seagull.beedo.common.enums;

import team.seagull.common.base.utils.StringUtils;

public enum ParseStructureTypeEnum {
    STRING("STRING", "字符串"),

    ARRAY("ARRAY", "数组");

    String code;
    String message;

    ParseStructureTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ParseStructureTypeEnum codeOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (ParseStructureTypeEnum enumObj : ParseStructureTypeEnum.values()) {
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
