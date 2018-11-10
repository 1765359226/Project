package com.taotao.item.listener;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ItemAddMessageListener implements MessageListener {
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${HTML_OUT_PATH}")
    private String HTML_OUT_PATH;
    @Override
    public void onMessage(Message message) {
        //从信息中取商品id
        TextMessage textMessage = (TextMessage) message;
        try {

            String text = textMessage.getText();
            System.out.print("123");
            //取出id
            Long id = Long.parseLong(text);

            //等待一秒
            Thread.sleep(1000);

            //根据商品id查询处商品信息及商品描述
            TbItem tbItem = itemService.getItemById(id);//商品信息
            Item item = new Item(tbItem);

            TbItemDesc tbItemDesc = itemService.getItemDescById(id);//商品描述

        //使用freemarker生成静态页面
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            // 1.创建模版
            //2.加载模版对象
            Template template = configuration.getTemplate("item.ftl");

            //3.准备模版需要的数据
            Map data = new HashMap<>();
            data.put(item,"item");
            data.put(tbItemDesc,"tbItemDesc");

             //4.指定输出的目录及文件名
            Writer out = new FileWriter(new File(HTML_OUT_PATH + text +".html"));
            //5.生成静态页面
            template.process(data,out);
            //关闭流
            out.close();
         } catch (Exception e) {
                e.printStackTrace();
         }
    }
}
