package com.seagull.beedo.common.enums;

/**
 * Created by hgs on 2017/6/25.
 */
public enum StatusCode {
    /**
     * ▪ 200 OK
     * ▪ 201 Created(请求已经被实现，而且有一个新的资源已经依据请求的需要而建立)
     * ▪ 202 Accepted(服务器已接受请求，但尚未处理)
     * ▪ 204 No Content(服务器成功处理了请求，但不需要返回任何实体内容)
     * ▪ 205 Reset Content(服务器成功处理了请求，且没有返回任何内容)
     *
     * ▪ 400 Bad Request(请求参数有误)
     * ▪ 401 Unauthorized(当前请求需要用户验证)
     * ▪ 403 Forbidden(服务器已经理解请求，但是拒绝执行它)
     * ▪ 404 Not Found(请求失败，请求所希望得到的资源未被在服务器上发现)
     * ▪ 405 Method Not Allowed(请求行中指定的请求方法不能被用于请求相应的资源)
     * ▪ 406 Not Acceptable(请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体)
     * ▪ 408 Request Timeout
     * ▪ 409 Conflict(由于和被请求的资源的当前状态之间存在冲突，请求无法完成)
     * ▪ 410 Gone(被请求的资源在服务器上已经不再可用，而且没有任何已知的转发地址)
     * ▪ 422 Unprocessable Entity(请求格式正确,含有语义错误)
     *
     */
    OK(200),Created(201),Accepted(202),NoContent(204),ResetContent(205),
    BadRequest(400),Unauthorized(401),Forbidden(403),NotFound(404),MethodNotAllowed(405),NotAcceptable(406),RequestTimeout(408),
    Conflict(409),Gone(410),UnprocessableEntity(422),SysException(500);
    private int status;
    StatusCode(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return ""+status;
    }
}
