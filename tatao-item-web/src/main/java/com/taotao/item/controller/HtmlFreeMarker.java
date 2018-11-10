package com.taotao.item.controller;


import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HtmlFreeMarker {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @RequestMapping(value = "/genHtml")
    @ResponseBody
    private String genHtml() throws Exception{
        //加载springmvc中的configuration
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //指定静态页面模板
        Template template = configuration.getTemplate("hello.ftl");
        //设置输出文件路径
        FileWriter out = new FileWriter(new File("E:/学习/13天学习/out/hello.html"));
        //设置数据
        Map data = new HashMap<>();
        data.put("hello","hello freemarker");
        template.process(data,out);
        //关闭流
        out.close();
        //返回
        return  "ok";
    }

}
