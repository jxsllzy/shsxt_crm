package com.shsxt.crm.exception;

public class NoLoginException extends RuntimeException {

    //出异常就是报错，将msg信息存入exception父类中
    private Integer code = 300;

    private String msg = "未登录异常";

    public NoLoginException() {
        super("未登录异常");
    }

    public NoLoginException(String msg) {
        super(msg);
        this.msg = msg;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public NoLoginException(Integer code) {
        super("未登录异常!");
        this.code = code;
    }

    public NoLoginException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }




    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
