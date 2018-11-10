package com.taotao.content.service.impl;


import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//后台商城页面类目
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
//        根据父节点id查询子节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
//        设置查询条件
        example.createCriteria().andParentIdEqualTo(parentId);
//        执行查询 ------根据父节点id查询出节点下的子节点，，赋予ContentCategory对象的集合里
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);

//        转换成EasyuitreeNode集合
        List<EasyUITreeNode> resultList = new ArrayList<>();
//        遍历出每个父节点里的子节点，添加到EasyUITreeNode集合里
        for(TbContentCategory category : list){
//        转换成 EasyUITreeNode
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(category.getId());
            node.setText(category.getName());
//            判断节点下是否还有节点，如果有返回closed，没有就返回open
            node.setState(category.getIsParent()?"closed":"open");
//            添加到EasyuitreeNode集合
            resultList.add(node);
        }
        return resultList;
    }

    //添加内容分类
    @Override
    public TaotaoResult createContentCategory(Long parentId, String name) {
//        构造对象，，补全其他属性
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        tbContentCategory.setStatus(1);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setIsParent(false);//新增的节点都是叶子节点
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setUpdated(tbContentCategory.getCreated());
//        插入contentcategory表数据
        tbContentCategoryMapper.insertSelective(tbContentCategory);
        /*
        * 判断如果要添加的节点的父节点本身叶子节点，需要更新其父节点
        * */
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (parent.getIsParent()==false){
            parent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parent);//更新节点的is_parent属性为true
        }
//        返回taotaoresult 包含内容分类的id  需要主键返回
        return TaotaoResult.ok(tbContentCategory);
    }
}
