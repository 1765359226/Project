package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.manager.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Destination;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    //redis缓存
    @Autowired
    private JedisClient jedisClient;
    //注入向Activemq发送商品添加信息的方法
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination destination;
    //取出商品数据在redis缓存中的前缀
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    //取出商品数据在缓存中的时间
    @Value("${TIME_EXPIRE}")
    private Integer TIME_EXPIRE;

    @Override
    //商品列表
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
//        1.设置分页的信息 使用pagehelper
        if (page==null)page=1;//如果页码为空，默认显示第一页
        if (rows==null)rows=30;//如果每页显示行数为空，默认为30行
        PageHelper.startPage(page,rows);//调用分页类
//        2.注入mapper
//        3.创建example对象 不需要设置查询条件
        TbItemExample example = new TbItemExample();
//        4.根据mapper调用查询所有数据的方法 ,查询到的总数据
        List<TbItem> list = tbItemMapper.selectByExample(example);
//        5.获取分页的信息
        PageInfo<TbItem> info = new PageInfo<>(list);//把结果集放进去，获取分页信息
//        6.封装到EasyUIDataGridResult
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) info.getTotal());//总记录数
        result.setRows(info.getList());//结果集
//        7.返回
        return result;
    }

    @Override
    //添加商品   desc为商品描述
    public TaotaoResult addItem(TbItem item, String desc) {
//        生成商品id
         final long id = IDUtils.genItemId();
//        补全item的属性
         item.setId(id);
         //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());//商品创建时间
        item.setUpdated(new Date());//商品更新时间
//        想商品表插入数据
        tbItemMapper.insert(item);
//        创建一个商品描述对应的表pojo >>TbItemDesc
        TbItemDesc tbItemDesc = new TbItemDesc();
//        补全pojo的属性
        tbItemDesc.setItemId(id);
        tbItemDesc.setCreated(new Date());//商品创建时间
        tbItemDesc.setUpdated(new Date());//商品更新时间
        tbItemDesc.setItemDesc(desc);
//        向商品描述表插入数据
        tbItemDescMapper.insert(tbItemDesc);

//      向Activemq发送商品添加信息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //发送商品id
                TextMessage textMessage = session.createTextMessage(id+"");
                System.out.print(id);
                return textMessage;
            }
        });
//        返回结果
        return TaotaoResult.ok();
    }


    /*
    * 商品详情页面
    * 根据id查询商品信息Tbitem
    *itemId
    * */
    @Override
    public TbItem getItemById(Long itemId){

        // 1.判断这个商品信息在redis缓存中是否存在
        try {
            String json = jedisClient.get(ITEM_INFO+":"+itemId+":BASE");
            if (StringUtils.isNotBlank(json)) {//不为空
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // 1. 根据id查询商品信息
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        //2.缓存中没有数据，就把数据存入缓存
            try {
                jedisClient.set(ITEM_INFO+":"+itemId+":BASE",JsonUtils.objectToJson(tbItem));
                //设置过期时间，提高缓存利用率
                jedisClient.expire(ITEM_INFO+":"+itemId+":BASE",TIME_EXPIRE);
            }catch (Exception e){
                e.printStackTrace();
            }
        return tbItem;
    }
    /*
     * 商品详情页面
     * 根据id查询商品的详情TbItemDesc
     *itemId
     * */
    @Override
    public TbItemDesc getItemDescById(Long itemId){
        //1.在缓存中判断这个商品的描述是否存在
        try {
            String json = jedisClient.get(ITEM_INFO+":"+itemId+":DESC");
            if (StringUtils.isNotBlank(json)){// 不为空
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //为空
        // 1. 根据id查询商品信息详情desc
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        //2.把取出的值存入redis中
        try {
             jedisClient.set(ITEM_INFO+":"+itemId+":DESC",JsonUtils.objectToJson(tbItemDesc));
            //设置过期时间，提高缓存利用率
            jedisClient.expire(ITEM_INFO+":"+itemId+":DESC",TIME_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tbItemDesc;
    }
}
