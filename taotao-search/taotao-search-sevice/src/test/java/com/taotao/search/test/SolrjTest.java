package com.taotao.search.test;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;



public class SolrjTest {
    /*
    测试把数据添加到索引库
    */
    @Test
    public void add() throws Exception{
        //1、创建Solrserver  建立连接-->需要指定地址
        SolrServer solrServer = new HttpSolrServer("http://192.168.19.128:8080/solr");
        //2、创建solrinputdocument文档
        SolrInputDocument document = new SolrInputDocument();
        //3、向文档中添加域
        document.addField("id","test001");
        document.addField("item_title","测试");
        //4、将文档添加到索引库中
        solrServer.add(document);
        //5.提交
        solrServer.commit();
    }
    /*
    * 测试从索引库查询数据
    *
    * */
    @Test
    public void testquery() throws Exception {
        //1、创建Solrserver  建立连接-->需要指定地址
        SolrServer solrServer = new HttpSolrServer("http://192.168.19.128:8080/solr");
        //2.创建solrquery对象  设置过滤条件，主查询条件  排序
        SolrQuery query = new SolrQuery();
        //3.设置条件
        query.setQuery("长虹");//主条件
        query.addFilterQuery("item_price:[0 TO 3000000000]");//过滤条件
        query.set("df","item_title");//默认搜索域
        //4.执行查询
        QueryResponse response = solrServer.query(query);
        //5.获取结果集
        SolrDocumentList results = response.getResults();
        System.out.println("总计录数:"+results.getNumFound());
        //6.遍历结果集  打印
        for (SolrDocument solrDocument : results){
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
        }

    }
}
