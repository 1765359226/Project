package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
* 商品分类管理Controller
* */
@RestController
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;
    /*
    *显示商品类目
    * 传入节点 id
    */
    @RequestMapping(value = "/item/cat/list")
//    @RequestParam(name = "id")会把id映射到parentId  ，没有打开时节点时，赋一个根节点defaultValue="0"初始值为0
    public List<EasyUITreeNode> getItemCatList(@RequestParam(name = "id",defaultValue = "0") Long parentId){
//        查询出出三个值  id、节点名Name、是否还有子节点state
        List<EasyUITreeNode> list = itemCatService.getItemCatList(parentId);
        return list;
    }
}
