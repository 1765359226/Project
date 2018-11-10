package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;//1.创建solrserver对象  由spring管理注入
    /*
    * 根据查询的条件在索引库中查询商品
    * 并返回结果集
    * */
    public SearchResult search(SolrQuery query) throws Exception {
        //返回结果的集合类型
        SearchResult searchResult = new SearchResult();
        //1.创建solrserver对象  由spring管理注入
        //2.直接执行查询  .query()
        QueryResponse response = solrServer.query(query);
        //3.获取结果集  .getResults()
        SolrDocumentList results = response.getResults();
        //4.获取RecordCount总记录数  .getNumFound()
        searchResult.setRecordCount(results.getNumFound());
        //5.遍历结果集
        List<SearchItem> list = new ArrayList<>();//定义一个集合，，先存到一个集合里

        //取高亮   .getHighlighting()
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        for (SolrDocument solrDocument : results){
            SearchItem searchItem = new SearchItem();
            searchItem.setId(Long.parseLong(solrDocument.get("id").toString()));
            searchItem.setCategory_name(solrDocument.get("item_category_name").toString());
            searchItem.setImage(solrDocument.get("item_image").toString());
            searchItem.setPrice((Long) solrDocument.get("item_price"));
            searchItem.setSell_point(solrDocument.get("item_sell_point").toString());
            //取高亮
            List<String> stringList = highlighting.get(solrDocument.get("id")).get("item_title");
            //判断高亮是否为空
            String gaoliangstr = "";
            if (stringList!=null&&stringList.size()>0){
                //有高亮
                gaoliangstr = stringList.get(0);
            }else {
                gaoliangstr = solrDocument.get("item_title").toString();
            }
            searchItem.setTitle(gaoliangstr);
//            searchItem.setTitle(solrDocument.get("item_title").toString());//取高亮
            // searchItem.setItem_desc(solrDocument.get("item_desc").toString());//搜索不用商品描述
            list.add(searchItem);
        }
        //6.把集合添加到SearchResult
        searchResult.setItemList(list);

        return searchResult;
    }
}