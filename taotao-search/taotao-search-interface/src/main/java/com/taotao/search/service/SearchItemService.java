package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;

import java.io.IOException;


public interface SearchItemService {

    //导入所有shuj数据到索引库中
    public TaotaoResult imporAllSearchItems() throws Exception;
    //根据搜素的条件返回搜索的结果  查询条件queryString  页码：page  每页显示行：rows
    public SearchResult search(String queryString,Integer page,Integer rows) throws Exception;
}
