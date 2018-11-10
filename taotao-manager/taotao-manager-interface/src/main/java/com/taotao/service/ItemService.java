package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

//商品相关处理的接口
public interface ItemService {
    //根据当前的 页码 和每页显示的 行数 进行分页查询
    public EasyUIDataGridResult getItemList(Integer page, Integer rows);
    TaotaoResult addItem(TbItem item, String desc);

    /*
    * 商品详情页面
    *根据id查询商品的信息
    * */
    TbItem getItemById(Long itemId);

    /*
     * 商品详情页面
     * 根据id查询商品的详情TbItemDesc
     *itemId
     * */
    TbItemDesc getItemDescById(Long itemId);
}
