package com.taotao.order.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
* 进入订单系统前进行拦截，登录拦截器
*
* */
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    @Value("${SSO_URL}")
    private String SSO_URL;
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object o) throws Exception {
        //执行handler之前执行此方法
        //1.从cookie中取token信息
        String token = CookieUtils.getCookieValue(httpServletRequest, TOKEN_KEY);
        //2.如果取不到token就跳转sso登录页面,需要把当前请求url作为参数传递给sso，sso登录成功后跳转会请求页面
        if (StringUtils.isBlank(token)){
            //取当前请求url,      跳转登录页面时作为参数传过去
            StringBuffer url = httpServletRequest.getRequestURL();
            // 跳转登录页页面
            httpServletResponse.sendRedirect(SSO_URL+"/page/login?url="+url);
            //不放行
            return false;
        }

        //3.取到token，调用sso系统的服务判断用户是否登录
        TaotaoResult taotaoResult = userService.getUserByToken(token);
        //4.如果用户未登录，即没取到用户信息，跳转到sso的登录页面
        if (taotaoResult.getStatus()!=200){
            //取当前请求url,      跳转登录页面时作为参数传过去
            StringBuffer url = httpServletRequest.getRequestURL();
            // 跳转登录页页面
            httpServletResponse.sendRedirect(SSO_URL+"/page/login?url="+url);
            //不放行
            return false;
        }
        //5.如果取到信息。放行
        //把用户信息放到request中
        TbUser tbUser = (TbUser) taotaoResult.getData();
        httpServletRequest.setAttribute("tbUser",tbUser);
        //返回值true：放行  返回false：拦截
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //handler之行之后，modelAndView返回之前
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //在ModelAndView返回之后，异常处理

    }
}
