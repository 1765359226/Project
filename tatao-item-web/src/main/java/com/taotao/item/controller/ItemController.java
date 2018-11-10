package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {
    /*
    * 根据商品id查询商品详情信息
    * itemId
    * */
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/item/{itemId}")
    public String showItem(@PathVariable Long itemId, Model model){
        //取商品基本信息item
        TbItem tbItem = itemService.getItemById(itemId);
        Item item = new Item(tbItem);
        //取商品详情itemdesc
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
        model.addAttribute("item",item);
        model.addAttribute("ItemDesc",tbItemDesc);
        //跳转item.jsp页面
        return "item";
    }
}
