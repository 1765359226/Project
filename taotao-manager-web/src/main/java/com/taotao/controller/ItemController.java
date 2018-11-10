package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;
    /*
    * 查询商品总条数、商品列表
    * 传入页码page  和  每页显示多少行rows
    * */
    @RequestMapping(value = "/item/list",method = RequestMethod.GET)
    private EasyUIDataGridResult getItemList(Integer page,Integer rows){
//        1.引入服务,在springmvc.xml中
//        2.注入服务
//        3.调用服务方法
        EasyUIDataGridResult list = itemService.getItemList(page, rows);
        return list;
    }

    /*
    *添加商品
    *传入商品信息tbItem 和商品描述desc
    * */
    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    public TaotaoResult addItem(TbItem tbItem,String desc){
        TaotaoResult result = itemService.addItem(tbItem, desc);
        return result;
    }
}
