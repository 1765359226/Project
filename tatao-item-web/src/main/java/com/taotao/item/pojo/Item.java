package com.taotao.item.pojo;

import com.taotao.pojo.TbItem;

public class Item extends TbItem {
    public Item(TbItem tbItem){
        //初始化属性
        this.setId(tbItem.getId());
        this.setTitle(tbItem.getTitle());
        this.setSellPoint(tbItem.getSellPoint());
        this.setPrice(tbItem.getPrice());
        this.setNum(tbItem.getNum());
        this.setBarcode(tbItem.getBarcode());
        this.setImage(tbItem.getImage());
        this.setCid(tbItem.getCid());
        this.setStatus(tbItem.getStatus());
        this.setBarcode(tbItem.getBarcode());
        this.setUpdated(tbItem.getUpdated());
    }
    //处理图片属性
    public String[] getImages(){
        //判断图片是否为空
        if (this.getImage() != null && !this.getImage().equals("")){
            //如果不为空，取出这个值
            String image = this.getImage();
            //以,逗号拆分成数组
            String[] strings = image.split(",");
            return strings;
        }
        return null;

    }
}
