package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//   显示页面
@Controller
public class PageController {
    @RequestMapping(value = "/")
    private String list(){
        return "index";
    }

    /*显示商品查询页面
    // url:/item-list
    @RequestMapping(value = "item-list")
    private String itemlist(){
        return "item-list";
    }*/

    //用page通用符获取请求链接
    @RequestMapping("/{page}")
    private String showPage(@PathVariable String page){//或者用 @PathVariable(value = "page") String page1
        return page;
    }

}
