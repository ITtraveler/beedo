/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.web.controller;

import team.seagull.common.base.Enum.MessageEnum;
import team.seagull.common.base.Enum.StatusCode;
import team.seagull.common.base.result.CommonResult;

/**
 * @author guosheng.huang
 * @version $id:BaseController.java, v 0.1 2018年08月13日 21:41 guosheng.huang Exp $
 */
public class BaseController {
    protected void retSuccess(CommonResult result) {
        result.setMessage(MessageEnum.OPERATE_SUCCESS.getMessage());
        result.setStatus(StatusCode.OK.getStatus());
    }

    protected void retFail(CommonResult result) {
        retFail(result, MessageEnum.OPERATE_FAIL.getMessage());
    }

    protected void retFail(CommonResult result, String message) {
        result.setMessage(message);
        result.setStatus(StatusCode.BadRequest.getStatus());
    }
}
