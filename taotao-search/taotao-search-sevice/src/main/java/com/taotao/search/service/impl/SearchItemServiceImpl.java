package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;
    @Autowired
    private SearchDao searchDao;
    /*
     *从数据库中获取数据保存到索引库中
     *  返回TaotaoResult
     * */
    @Override
    public TaotaoResult imporAllSearchItems() throws Exception{
        //1.调用mapper的方法，查询所有商品数据
        List<SearchItem> searchItemList = searchItemMapper.getSearchItemList();
        //2.通过solrj 将数据写入索引库中
              //2.1创建httpsolrserver来指定索引库-->地址
              //2.2创建solrinputdocument文档
              //2.3把数据添加到文档中----使用遍历的方法，一个个添加
        for (SearchItem searchItem : searchItemList){
            SolrInputDocument document = new SolrInputDocument();

            document.addField("id",searchItem.getId().toString());//这里是字符串需要转换
            document.addField("item_title",searchItem.getTitle());
            document.addField("item_sell_point",searchItem.getSell_point());
            document.addField("item_price",searchItem.getPrice());
            document.addField("item_image",searchItem.getImage());
            document.addField("item_category_name",searchItem.getCategory_name());
            document.addField("item_desc",searchItem.getItem_desc());
            //2.4 把文档添加到索引库中
            solrServer.add(document);
        }
             //2.5 提交
        solrServer.commit();
        return TaotaoResult.ok();
    }
    /*
     *根据搜索的条件返回搜索的结果
     * 查询条件queryString
     * 页码：page
     * 每页显示行：rows
     */
    @Override
    public SearchResult search(String queryString, Integer page, Integer rows) throws Exception{
        //1.创建solrquery对象
        SolrQuery query = new SolrQuery();
        //2.设置查询条件
        if (StringUtils.isNotBlank(queryString)){//判断查询的值是否为空
            //不为空
            query.setQuery(queryString);
        }else {
            //为空
            // 查询所有
            query.setQuery("*:*");
        }
          //2.1 设置过滤条件   设置分页
        if (page==null)page=1;
        if (rows==null)rows=60;
        query.setStart((page-1)*rows);//起始索引
        query.setRows(rows);//每页显示的条数
           //2.2设置默认搜索域
        query.set("df","item_keywords");
           //2.3设置高亮
        query.setHighlight(true);
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        query.addHighlightField("item_title");//设置高亮显示的域
        //3.调用dao的方法  返回的是SearchResult 只包含了总记录数和商品的列表
        SearchResult search = searchDao.search(query);
        //4.设置SearchReaslut的总页数
        long pageCount = 01;
        pageCount = search.getRecordCount()/rows;
        if (search.getRecordCount()%rows>0){
            pageCount++;
        }
        search.setPageCount(pageCount);
        //5.返回
        return search;
    }

}
