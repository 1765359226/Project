package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImportSearchController {
    @Autowired
    private SearchItemService searchItemService;

    /*
     *从数据库中获取数据保存到索引库中
     *  返回TaotaoResult
     * */
    @RequestMapping(value = "/index/inportAll",method = RequestMethod.GET)
    public TaotaoResult imporAllSearchItems() throws Exception{
        searchItemService.imporAllSearchItems();
        return TaotaoResult.ok();
    }
}
