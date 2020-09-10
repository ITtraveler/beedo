package com.seagull.beedo.common.enums;


import org.apache.commons.lang3.StringUtils;

/**
 * @author hgs
 * @version State.java, v 0.1 2017/10/24 22:19 hgs Exp $
 */
public enum StateEnum {
    ENABLE("ENABLE", 0),

    DISENABLE("DISENABLE", -1);

    private int value;

    private String code;

    StateEnum(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public static StateEnum codeOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (StateEnum enumObj : StateEnum.values()) {
            if (enumObj.code.equals(code.toUpperCase())) {
                return enumObj;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
