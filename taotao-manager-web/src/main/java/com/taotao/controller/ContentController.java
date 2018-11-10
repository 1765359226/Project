package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class ContentController {
    @Autowired
    public ContentService contentService;
    /*
     * 新增内容
     * */
    @RequestMapping(value = "/content/save",method = RequestMethod.POST)
    private TaotaoResult saveContent(TbContent tbContent){
//        1.引用服务
//        1.注入服务
//        2.调用-----返回200
        return contentService.saveContent(tbContent);
    }
}
