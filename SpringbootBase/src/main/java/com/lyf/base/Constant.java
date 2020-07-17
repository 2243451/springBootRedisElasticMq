package com.lyf.base;

public class Constant {

    public static final int ERROR_CODE = 300;

    public enum CodeEnum {
        // 数据操作错误定义
        SUCCESS(200, "操作成功"),
        FAILURE(300, "操作失败");

        private int code;
        private String msg;

        CodeEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}

