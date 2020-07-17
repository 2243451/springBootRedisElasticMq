package com.lyf.base;


import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    @ModelAttribute
    public void preHandle(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());//当前项目路径
    }

}
