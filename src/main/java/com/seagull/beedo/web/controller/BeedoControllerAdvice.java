/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import team.seagull.common.base.Enum.StatusCode;
import team.seagull.common.base.result.CommonResult;

/**
 * ControllerAdvice
 * @author guosheng.huang
 * @version $id:BeedoControllerAdvice.java, v 0.1 2018年08月11日 20:45 tao.hu Exp $
 */
@ControllerAdvice
public class BeedoControllerAdvice{

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public Object exception(Exception exception){
        CommonResult<Object> result = new CommonResult<>();
        result.setData(exception.toString());
        result.setStatus(StatusCode.SysException.getStatus());
        result.setMessage("系统异常");
        return result;
    }
}
