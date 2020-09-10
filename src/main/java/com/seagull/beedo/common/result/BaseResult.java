package com.seagull.beedo.common.result;

import com.seagull.beedo.common.ToString;
import com.seagull.beedo.common.enums.MessageEnum;
import com.seagull.beedo.common.enums.StatusCode;

/**
 * Created by hgs on 2017/6/25.
 */
public class BaseResult extends ToString {
    private static final long serialVersionUID = 8233015811087225744L;
    private int status;
    private String message;

    public BaseResult() {
        status = StatusCode.OK.getStatus();
        message = MessageEnum.OPERATE_SUCCESS.getMessage();
    }

    public BaseResult(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
