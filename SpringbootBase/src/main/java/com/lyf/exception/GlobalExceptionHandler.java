package com.lyf.exception;

import com.lyf.base.Constant.CodeEnum;
import com.lyf.util.Result;
import com.lyf.model.ResultInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(value =ParamsException.class)
    @ResponseBody
    public ResultInfo exceptionHandler(ParamsException e){
        return Result.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public ResultInfo exceptionHandler(Exception e){
        log.info("发生异常！原因是:"+e);
        e.printStackTrace();
        return Result.error(CodeEnum.FAILURE.getCode(), CodeEnum.FAILURE.getMsg());
    }
}
