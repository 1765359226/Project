package com.taotao.search.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
* 全局异常处理类
* 需要在springmvc.xml设置全局异常配置bean
*
* */
public class GlobalExceptionReslover implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        //1.日志写入到日志文件中
        System.out.print(e.getMessage());
        e.printStackTrace();
        //2.及时通知工作人员，发短信、发邮件（通过第三方接口发）
        System.out.print("发邮件");
        //3.给用户一个友好提示:您的网络异常！
        ModelAndView model =new ModelAndView();
        //设置试图信息
        model.setViewName("error/exception");
        //设置模型数据
        model.addObject("message","您的网络异常！");
        return model;
    }
}
