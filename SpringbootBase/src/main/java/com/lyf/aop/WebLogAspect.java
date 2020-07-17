package com.lyf.aop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Order(1)
@Aspect
@Component
public class WebLogAspect {

    private static Logger logger;

    /**
     * 以 controller 包下定义的所有请求为切入点
     */
    @Pointcut("execution(public * com.lyf.controller..*.*(..))")
    public void webLog() {
    }

    /**
     * 在切点之前织入
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 开始打印请求日志
        MDC.put("trace_uuid", UUID.randomUUID().toString());// 加入trace追踪
        logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 打印请求相关参数
        logger.info("========================================== Start ==========================================");
        // 打印请求 url
        logger.info("URL            : {}", request.getRequestURL().toString());
        // 打印 Http method
        logger.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        logger.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        logger.info("IP             : {}", (request.getHeader("X-Forwarded-For") == null) ? request.getRemoteAddr() : request.getHeader("X-Forwarded-For"));
        // 打印请求Cookie
        logger.info("Cookie         : {}", cookie(request.getCookies()));
        // 打印请求入参 https://blog.csdn.net/weixin_44130081/article/details/98847575
        logger.info("Request Args   : {}", Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 在切点之后织入
     *
     * @throws Throwable
     */
    @After("webLog()")
    public void doAfter() throws Throwable {
        logger.info("=========================================== End ===========================================");
        // 每个请求之间空一行
        logger.info("☜☝☞");
    }


    /**
     * 在切点异常之后织入
     *
     * @throws Throwable
     */
    @AfterThrowing(value = "webLog()", throwing = "e")
    public void doAfterThrowing(Exception e) throws Throwable {
        // 打印异常
        logger.info("{}      : {}", e.getClass().getName(), e.getMessage());
    }

    /**
     * 环绕
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 打印出参
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();// https://www.jianshu.com/p/83eb1b2bc119
        Gson gson = gsonBuilder.create();
        logger.info("Response Args  : {}", gson.toJson(result));
        // 执行耗时
        logger.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

    private String cookie(Cookie[] cookies) {
        StringBuilder stringBuilder = new StringBuilder();
        if (cookies != null && cookies.length > 0) {
            stringBuilder.append("[");
            for (int i = 0; i < cookies.length; i++) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(cookies[i].getName()).append("=").append(cookies[i].getValue());
            }
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }

    @After("@annotation(com.lyf.annotation.LogTrace)")
    public void log_trace(JoinPoint joinPoint) {
        JavaLangAccess access = SharedSecrets.getJavaLangAccess();
        Throwable throwable = new Throwable();
        int depth = access.getStackTraceDepth(throwable);
        Class clazz = joinPoint.getTarget().getClass();
        String targetName = clazz.getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String target = targetName + "." + methodName;
        logger.info("====== StackTrace: " + target + " ======");
        for (int i = depth - 1; i > 0; i--) {//从1开始不显示当前工具类调用
            StackTraceElement ste = access.getStackTraceElement(throwable, i);
//            for(int j=1;j<depth-i;j++){
//                System.out.print("\t");//缩进
//            }
            String className = ste.getClassName();
            if (className.contains("lyf") && !className.contains("$$") && !className.contains("aop")) {
                logger.info(String.format("[%s.%s:%s]", ste.getClassName(), ste.getMethodName(), ste.getLineNumber()));
            }
        }
        logger.info("====== StackTrace: " + target + " ======");
    }
}