
package com.seagull.beedo.common.exception;

import com.seagull.beedo.common.enums.EnumBase;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author guosheng.huang
 * @version $Id: BaseRuntionException, v1.0 2018年12月07日 14:24 guosheng.huang Exp $
 */
public class BaseRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -5165291529756717366L;
    protected String code;
    protected String message;
    protected EnumBase errorEnum;

    public BaseRuntimeException() {
    }

    public BaseRuntimeException(String message) {
        super(message);
        this.message = message;
    }

    public BaseRuntimeException(EnumBase baseEnum) {
        super(baseEnum.message());
        this.code = baseEnum.name();
        this.message = baseEnum.message();
        this.errorEnum = baseEnum;
    }

    public BaseRuntimeException(EnumBase baseEnum, String message) {
        super(message);
        this.code = baseEnum.name();
        this.errorEnum = baseEnum;
        this.message = message;
    }

    public BaseRuntimeException(String errorCode, String message) {
        super(message);
        this.code = errorCode;
        this.message = message;
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseRuntimeException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.code = errorCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static long getSerialversionuid() {
        return 8321149154706648074L;
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public EnumBase getErrorEnum() {
        return this.errorEnum;
    }
}