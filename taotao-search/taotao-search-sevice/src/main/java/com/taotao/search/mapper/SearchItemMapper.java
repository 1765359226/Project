package com.taotao.search.mapper;

import com.taotao.common.pojo.SearchItem;

import java.util.List;

/*
* 定义Mapper 关联查询3张表 查询出搜索时的商品数据
*
* */
public interface SearchItemMapper {
    List<SearchItem> getSearchItemList();
    SearchItem getItemById(long id);

}
