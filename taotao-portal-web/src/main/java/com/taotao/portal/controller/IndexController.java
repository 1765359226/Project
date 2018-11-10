package com.taotao.portal.controller;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 展示首页
 */
@Controller
public class IndexController {
    //首页打广告位内容分类id
    @Value("${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;
    //大广告为的宽
    @Value("${AD1_WIDTH}")//大屏时
    private Integer AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")//小屏时
    private Integer AD1_WIDTH_B;
    //大广告为的高
    @Value("${AD1_HEIGHT}")//大屏时
    private Integer AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")//小屏时
    private Integer AD1_HEIGHT_B;

    @Autowired
    public ContentService contentService;
    /**
     * 展示首页
     */
    //接收URL的请求http://localhost:8082/index.html
    @RequestMapping(value = "/index")
    public String showIndex(Model model){
        //根据cid查询轮播图内容列表
        List<TbContent> list = contentService.getContentByCid(AD1_CATEGORY_ID);
        //把列表转换为AD1Node列表
        List<AD1Node> ad1Nodes = new ArrayList<>();
        for (TbContent tbContent : list){
            AD1Node node = new AD1Node();
            node.setAlt(tbContent.getTitle());
            node.setHeight(AD1_HEIGHT);
            node.setHeightB(AD1_HEIGHT_B);
            node.setWidth(AD1_WIDTH);
            node.setWidthB(AD1_WIDTH_B);
            node.setSrc(tbContent.getPic());
            node.setSrcB(tbContent.getPic2());
            node.setHref(tbContent.getUrl());
            //添加到List<AD1Node>节点列表中
            ad1Nodes.add(node);
            }
        //把列表转换成json列表
        String json = JsonUtils.objectToJson(ad1Nodes);
        //把json传递给页面
        model.addAttribute("ad1", json);
        return "index";
    }
}
