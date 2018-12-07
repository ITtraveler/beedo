package com.seagull.beedo.common.enums;

import lombok.Getter;

/**
 * @author guosheng.huang
 * @version $Id: ForumResultCodeEnum, v1.0 2018年11月09日 10:38 guosheng.huang Exp $
 */
@Getter
public enum BeedoResultCodeEnum implements EnumBase {
                                                     SUCCESS("SUCCESS", "操作成功"),

                                                     /** 系统异常*/
                                                     SYSTEM_ERROR("SYSTEM_ERROR", "系统异常，请联系管理员！"),
                                                     /** 404*/
                                                     PAGE_NOT_FOUND("PAGE_NOT_FOUND", "404错误"),

                                                     /** 系统繁忙*/
                                                     SYSTEM_BUSY("SYSTEM_BUSY", "系统繁忙，请稍后再试。"),

                                                     /** 客官莫急*/
                                                     DONT_WORRY_SIR("DONT_WORRY_SIR",
                                                                    "客官莫急，请稍候再试。"),

                                                     /** 外部接口调用异常*/
                                                     INTERFACE_SYSTEM_ERROR("INTERFACE_SYSTEM_ERROR",
                                                                            "外部接口调用异常，请联系管理员！"),

                                                     /** 业务连接处理超时 */
                                                     CONNECT_TIME_OUT("CONNECT_TIME_OUT",
                                                                      "系统超时，请稍后再试!"),

                                                     /** 非法请求 */
                                                     ILLEGAL_REQUEST("ILLEGAL_REQUEST", "非法请求"),

                                                     /** 系统错误 */
                                                     SYSTEM_FAILURE("SYSTEM_FAILURE",
                                                                    "系统异常，请稍后再试！"),

                                                     /** 参数为空 */
                                                     NULL_ARGUMENT("NULL_ARGUMENT", "参数为空"),

                                                     /** 登录超时  */
                                                     LOGIN_TIMEOUT("LOGIN_TIMEOUT", "登录超时"),

                                                     /** 用户ID为空  */
                                                     NULL_USER_ID("NULL_USER_ID", "用户ID为空"),

                                                     /** 参数不正确 */
                                                     ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "参数不正确"),

                                                     /** 用户未登陆*/
                                                     USER_NOT_LOGIN("USER_NOT_LOGIN", "亲，您还未登录哦~"),

                                                     /** 逻辑错误 */
                                                     LOGIC_ERROR("LOGIC_ERROR", "逻辑错误"),

                                                     /** 数据异常 */
                                                     DATA_ERROR("DATA_ERROR", "数据异常"),

                                                     /** 暂不支持的操作 */
                                                     UN_SUPPORT_OPERATER("UN_SUPPORT_OPERATER",
                                                                         "暂不支持的操作"),

                                                     /** 空结果集 */
                                                     NULL_RESULT("NULL_RESULT", "空结果集"),

                                                     /** 空结果集 */
                                                     NULL_USER("NULL_USER", "用户不存在"),

                                                     /** 空结果集 */
                                                     NULL_FORUM_USER("NULL_USER", "社区用户不存在"),

                                                     /** 操作受限 */
                                                     LIMITED_OPERATE("LIMITED_OPERATE", "操作受限"),

                                                     /** 数据库操作失败 */
                                                     DB_OPERATE_FAILED("DB_OPERATE_FAILED", "数据库操作失败");

    /** 枚举编号 */
    private String code;

    /** 枚举详情 */
    private String message;

    /**
     * 构造方法
     *
     * @param code         枚举编号
     * @param message      枚举详情
     */
    BeedoResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     *
     * @param code         枚举编号
     * @return
     */
    public static BeedoResultCodeEnum getResultCodeEnumByCode(String code) {
        for (BeedoResultCodeEnum param : values()) {
            if (param.getCode().equals(code)) {
                return param;
            }
        }
        return null;
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public Number value() {
        return null;
    }
}
