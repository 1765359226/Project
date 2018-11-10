package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/*
*展示登录和注册页面
* */
@Controller
public class PageController {
    @RequestMapping(value = "/page/register")
    public String showRegister(){
        return "register";
    }
    @RequestMapping(value = "/page/login")
    public String showLogin(String url, Model model){
        model.addAttribute("redirect",url);
        return "login";
    }

}
