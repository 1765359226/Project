package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {
    //商城页面类目---通过节点id查询该节点的子节点列表
    public List<EasyUITreeNode> getContentCategoryList(Long parentId);
    //添加内容分类----父节点id，新增节点名name
    public TaotaoResult createContentCategory(Long parentId,String name);
}
