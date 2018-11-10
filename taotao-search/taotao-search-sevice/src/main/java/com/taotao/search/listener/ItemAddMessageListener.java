package com.taotao.search.listener;
import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/*
* 监听商品添加事件，同步索引库
* */
public class ItemAddMessageListener  implements MessageListener {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {

        try {
            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            long id = Long.parseLong(text);
            //根据该id查询商品数据，取出商品信息
            //等待1秒---怕商品还没有添加到数据库中，然后就查询数据
            Thread.sleep(1000);

            SearchItem list = searchItemMapper.getItemById(id);
            //创建文档对象
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id",list.getId().toString());//这里是字符串需要转换
            document.addField("item_title",list.getTitle());
            document.addField("item_sell_point",list.getSell_point());
            document.addField("item_price",list.getPrice());
            document.addField("item_image",list.getImage());
            document.addField("item_category_name",list.getCategory_name());
            document.addField("item_desc",list.getItem_desc());
            //把文档对象写入索引库
            solrServer.add(document);
            //提交
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
