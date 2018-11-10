package com.taotao.search.test;


import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

/*
* solr集群测试
*
* */
public class SolrCloudTest {
    @Test
    public void testAdd() throws Exception {
//        1.创建solrserver 集群的实现
//          指定zookeeper集群的节点列表字符串
        CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.19.128:2182,192.168.19.128:2183,192.168.19.128:2184");
//        2.设置默认搜索的collection  默认的搜索库 （不是core所对应的，是整个collection索引集合）
        cloudSolrServer.setDefaultCollection("collection2");
//        3.创建solrinputdocument文档对象
        SolrInputDocument document = new SolrInputDocument();
//        4.添加域到文档
        document.setField("id","testcloudid");
        document.setField("item_title","123");
//        5.将文档提交到索引库中
        cloudSolrServer.add(document);
//        6.提交
        cloudSolrServer.connect();
    }
}
