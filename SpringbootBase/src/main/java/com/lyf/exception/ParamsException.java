package com.lyf.exception;

public class ParamsException extends RuntimeException {
    private Integer code= 300;
    private String msg= "操作失败!";

    public ParamsException( Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ParamsException(Integer code) {
        super("操作失败");
        this.code = code;
    }

    public ParamsException( String msg) {
        super(msg);
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
