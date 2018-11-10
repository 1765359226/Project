package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    /*
     * 商城内容分类分类管理Controller
     * method=get
     * */
    @RequestMapping(value = "/content/category/list",method=RequestMethod.GET)
//        @RequestParam(name = "id")会映射到parentId上，，没有打开节点时赋予初始值根节点defaultValue = "0"
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name = "id",defaultValue = "0") Long parentId){

//        查询出出三个值  id、节点名Name、是否还有子节点state
        List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parentId);
        return list;
    }
    /*
     * /content/category/create
     * method=post
     * 参数：
     * parentId:新增节点的父节点id
     * name：新增节点名
     * 返回Taotaoresult  包括id
     */
    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    private TaotaoResult createContentCategory(Long parentId,String name){
        TaotaoResult result = contentCategoryService.createContentCategory(parentId, name);
        return result;
    }

}
