package com.taotao.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Override
    //显示商品类目
    public List<EasyUITreeNode> getItemCatList(Long parentId) {
        //根据父节点id查询子节点列表
        TbItemCatExample example = new TbItemCatExample();
        //设置查询条件,//设置parentid
        example.createCriteria().andParentIdEqualTo(parentId);
        //执行查询----根据父节点parentId，查询出节点以下的所有内容，存入在一个TbItemCat集合里。
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);

        //转换成EasyUITreeNode列表,然后return返回出去
        List<EasyUITreeNode> resultlist = new ArrayList<>();

        for (TbItemCat tbItemCat : list){
            EasyUITreeNode node = new EasyUITreeNode();
                node.setId(tbItemCat.getId());
                node.setText(tbItemCat.getName());
                //如果节点下有节点“closed”，如果没有子节点“open”
                node.setState(tbItemCat.getIsParent()?"closed":"open");

            //添加到节点列表
            resultlist.add(node);
        }
        return resultlist;
    }
}
