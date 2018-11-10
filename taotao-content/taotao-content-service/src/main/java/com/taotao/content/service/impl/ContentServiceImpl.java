package com.taotao.content.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    public TbContentMapper tbContentMapper;
    @Autowired
    public JedisClient jedisClient;//面向接口
    @Value("${INDEX_CONTENT}")
    public String INDEX_CONTENT;

    /*
     * 在后台更新前台的大广告等等，需要同步缓存到redis
     *
     * */
    @Override
    public TaotaoResult saveContent(TbContent tbContent) {
//        1.注入mapper
//        2.补全其他属性
        tbContent.setCreated(new Date());
        tbContent.setUpdated(tbContent.getCreated());
        tbContentMapper.insertSelective(tbContent);
        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT,tbContent.getCategoryId().toString());
        return TaotaoResult.ok();
    }
/*
* 商城首页大广告显示
*
* */
    @Override
    public List<TbContent> getContentByCid(Long cid) {
//        1.先查询redis缓存
//        添加缓存不能影响正常业务逻辑
        try{
            //执行查询缓存
            String json = jedisClient.hget(INDEX_CONTENT, cid + "");
            //查到，把json转成list
            if (StringUtils.isNotBlank(json)){//判断是否为空
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        2.缓存中有则直接取出数据,没有就查询数据库
        TbContentExample example = new TbContentExample();
//        3.设置查询条件
        example.createCriteria().andCategoryIdEqualTo(cid);
//        4.执行查询
        List<TbContent> list = tbContentMapper.selectByExample(example);
//        5.缓存中没有数据，就把数据存入缓存
        try{
            jedisClient.hset(INDEX_CONTENT,cid+"",JsonUtils.objectToJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
