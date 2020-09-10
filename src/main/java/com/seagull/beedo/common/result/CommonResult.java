package com.seagull.beedo.common.result;

/**
 * Created by hgs on 2017/6/25.
 */
public class CommonResult<T> extends BaseResult{
    private static final long serialVersionUID = 1898627101207325629L;
    private T data;

    public CommonResult() {
    }

    public CommonResult(int status, String message) {
        super(status, message);
    }

    public CommonResult(int status, String message, T data){
        super(status, message);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
