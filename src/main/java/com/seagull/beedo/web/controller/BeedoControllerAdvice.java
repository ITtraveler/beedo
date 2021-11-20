/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.web.controller;

import com.github.pagehelper.util.StringUtil;
import com.seagull.beedo.common.enums.BeedoResultCodeEnum;
import com.seagull.beedo.common.enums.StatusCode;
import com.seagull.beedo.common.exception.BeedoCoreException;
import com.seagull.beedo.common.result.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ControllerAdvice
 * @author guosheng.huang
 * @version $id:BeedoControllerAdvice.java, v 0.1 2018年08月11日 20:45 guosheng.huang Exp $
 */
@ControllerAdvice
public class BeedoControllerAdvice {

    Logger logger = LoggerFactory.getLogger(BeedoControllerAdvice.class);

    /**
     * ForumCoreException异常处理
     * @param exception
     * @return
     */
    @ExceptionHandler(value = { BeedoCoreException.class })
    @ResponseBody
    public Object forumCoreException(BeedoCoreException exception) {
        logger.error("API接口调用，抛出异常", exception);
        CommonResult<Object> result = new CommonResult<>();
        result.setData(exception.toString());
        result.setStatus(StatusCode.SysException.getStatus());
        result.setMessage(StringUtil.isEmpty(exception.getMessage())
            ? BeedoResultCodeEnum.DATA_ERROR.getMessage()
            : exception.getMessage());
        return result;
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseBody
    public Object exception(Exception exception) {
        logger.error("API接口调用，抛出异常", exception);
        CommonResult<Object> result = new CommonResult<>();
        result.setData(exception.toString());
        result.setStatus(StatusCode.SysException.getStatus());
        result.setMessage("系统异常");
        return result;
    }
}
