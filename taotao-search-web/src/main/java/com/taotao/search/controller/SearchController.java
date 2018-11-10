package com.taotao.search.controller;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    @Autowired
    private SearchItemService searchItemService;
    @Value("${ITEM_ROWS}")
    private Integer ITEM_ROWS;

    /*
     *根据搜素的条件返回搜索的结果
     * 查询条件queryString
     * 页码：page
     * 每页显示行：rows
     */
    @RequestMapping(value = "/search")
    //因为前台传过来的值为q  所以要用@RequestParam(value = "q") 转换一下
    //@RequestParam(defaultValue = "1")设置page初始值为1
    private String search(@RequestParam(value = "q") String queryString,@RequestParam(defaultValue = "1") Integer page, Model model)throws Exception{
        //处理乱码
        queryString = new String(queryString.getBytes("iso-8859-1"),"utf-8");
        SearchResult result= searchItemService.search(queryString, page,ITEM_ROWS);
        //设置数据传递到jsp中
        model.addAttribute("query",queryString);//搜索关键字回显
        model.addAttribute("totalPages",result.getPageCount());//总页数
        model.addAttribute("itemList",result.getItemList());//查询列表
        model.addAttribute("page",page);//当前页
        return "search";
    }
}
