package com.seagull.beedo.common.enums;


import org.apache.commons.lang3.StringUtils;

/**
 * Created by hgs on 2017/6/25.
 */
public enum MessageEnum {
    OPERATE_SUCCESS("OPERATE_SUCCESS","操作成功"),
    OPERATE_FAIL("OPERATE_FAIL","操作失败"),

    QUERY_SUCCESS("QUERY_SUCCESS","查询成功"),
    QUERY_FAIL("QUERY_FAIL","查询失败"),

    UPDATE_SUCCESS("UPDATE_SUCCESS","更新成功"),
    UPDATE_FAIL("UPDATE_FAIL","更新失败"),

    DELETE_SUCCESS("DELETE_SUCCESS","删除成功"),
    DELETE_FAIL("DELETE_FAIL","删除失败"),

    ADD_SUCCESS("ADD_SUCCESS","添加成功"),
    ADD_FAIL("ADD_FAIL","添加失败"),;

    String code;
    String message;

    MessageEnum() {

    }

    MessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static MessageEnum codeOf(String code) {
        if (StringUtils.isBlank(code))
            return null;

        for (MessageEnum enumObj : MessageEnum.values()) {
            if (enumObj.code.equals(code.toUpperCase())) {
                return enumObj;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
