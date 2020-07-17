package com.lyf.aop;

import com.lyf.annotation.CheckNull;
import com.lyf.base.Constant;
import com.lyf.exception.ParamsException;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Order(2)
@Aspect
@Component
@Slf4j
public class CheckNullAspect {

    @Autowired
    HttpServletRequest request;

    @Before("@annotation(com.lyf.annotation.CheckNull) && @annotation(checkNull)")
    public void check(JoinPoint jp, CheckNull checkNull) throws Throwable {
        String[] checkNames = checkNull.value();
        Map<String, String> paramMap = getParamMap(jp);
        for (int i = 0; i < checkNames.length; i++) {
            String checkName = checkNames[i];
            if(StringUtils.isBlank(paramMap.get(checkName))
                    && StringUtils.isBlank(request.getParameter(checkName))){// 兼容通过对象获取参数形式
                throw new ParamsException(Constant.ERROR_CODE, checkName+"不能为空");
            }
        }
    }

    /**
     * 获取请求参数列表
     * @return
     */
    private Map<String, String> getParamMap(JoinPoint jp) {
        Object[] argArr = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        String[] paramNameArr = signature.getParameterNames();
        Map<String, String> paramMap = new HashMap<>();
        for (int i = 0; i < paramNameArr.length; i++) {
            paramMap.put(paramNameArr[i], ObjectUtils.nullSafeToString(argArr[i]));
        }
        return paramMap;
    }
}
