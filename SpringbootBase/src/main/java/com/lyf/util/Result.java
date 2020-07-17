package com.lyf.util;

import com.lyf.model.ResultInfo;

public class Result {
    public static ResultInfo success(Integer code, String msg, Object result){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(code);
        resultInfo.setMsg(msg);
        resultInfo.setResult(result);
        return resultInfo;
    }

    public static ResultInfo success(String msg, Object result){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg(msg);
        resultInfo.setResult(result);
        return resultInfo;
    }

    public static ResultInfo success(Object result){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setResult(result);
        return resultInfo;
    }

    public static ResultInfo success(Integer code, String msg){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(code);
        resultInfo.setMsg(msg);
        return resultInfo;
    }

    public static ResultInfo success(String msg){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg(msg);
        return resultInfo;
    }

    public static ResultInfo success(){
        ResultInfo resultInfo = new ResultInfo();
        return resultInfo;
    }

    public static ResultInfo error(Integer code, String msg){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(code);
        resultInfo.setMsg(msg);
        return resultInfo;
    }
}
