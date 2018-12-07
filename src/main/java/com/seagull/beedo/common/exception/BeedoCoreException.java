package com.seagull.beedo.common.exception;


import com.seagull.beedo.common.enums.BeedoResultCodeEnum;
import com.seagull.beedo.common.enums.EnumBase;

/**
 * @author guosheng.huang
 * @version $Id: BeedoCoreException, v1.0 2018年11月09日 10:09 guosheng.huang Exp $
 */
public class BeedoCoreException extends BaseRuntimeException {

    private static final long serialVersionUID = 7826762501107934688L;

    /**
     *
     */
    public BeedoCoreException() {
        super();
    }

    /**
     * @param baseEnum
     * @param message
     */
    public BeedoCoreException(EnumBase baseEnum, String message) {
        super(baseEnum, message);
    }

    /**
     * @param baseEnum
     */
    public BeedoCoreException(EnumBase baseEnum) {
        super(baseEnum);
    }

    /**
     * @param errorCode
     * @param message
     * @param cause
     */
    public BeedoCoreException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
        errorEnum = BeedoResultCodeEnum.getResultCodeEnumByCode(errorCode);
    }

    /**
     * @param errorCode
     * @param message
     */
    public BeedoCoreException(String errorCode, String message) {
        super(errorCode, message);
        errorEnum = BeedoResultCodeEnum.getResultCodeEnumByCode(errorCode);
    }


    /**
     * @param message
     * @param cause
     */
    public BeedoCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public BeedoCoreException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public BeedoCoreException(Throwable cause) {
        super(cause);
    }
}
