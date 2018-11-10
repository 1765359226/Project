package cn.itcast.httpclient;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
* 手机客户端调用功能实现的接口
*
* */
public class APIService {
//     1.不带参数的get请求的方法
    public HttpResult doGet(String url) throws Exception {
        return doGet(url,null);
    }

//      2.带参数的get请求的方法
    public HttpResult doGet(String url, Map<String,Object> map)throws Exception{
        //创建httpclient的对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建httpget  get请求
        URIBuilder uriBuilder = new URIBuilder(url);
        //遍历参数，设置参数的值
        if (map!=null){
            for (Map.Entry<String,Object> entry:map.entrySet()){
                uriBuilder.setParameter(entry.getKey(),entry.getValue().toString());
            }
        }
        URI uri = uriBuilder.build();
        HttpGet httpGet = new HttpGet(uri);
        //执行请求
        CloseableHttpResponse response = client.execute(httpGet);
        //获取响应的结果 封装到httpsult中
        Integer code = response.getStatusLine().getStatusCode();//获取状态码
        //响应内容
        String body = null;
        if (response.getEntity()!=null){
            body = EntityUtils.toString(response.getEntity(),"utf-8");
        }
        HttpResult result = new HttpResult(code, body);
        return result;
    }
//     3.不带参数的post请求的方法
    public HttpResult doPost(String url) throws Exception {
        //直接调用带参doPost的方法
        return doPost(url,null);
        }

//     4.带参数的post请求的方法
    public HttpResult doPost(String url,Map<String,Object> map) throws Exception {
        //一.创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();

        //二.创建httpPost请求对象
        HttpPost httpPost = new HttpPost(url);
        // 判断参数是否为空
        if(map!=null){
            //遍历参数map集合，设置参数列表
            List<NameValuePair> parameters = new ArrayList<>();
            for (Map.Entry<String,Object> Entry:map.entrySet()) {
                parameters.add(new BasicNameValuePair(Entry.getKey(),Entry.getValue().toString()));
            }
                //创建表单实体对象，将参数设置到表单实体中
                UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(parameters);
                httpPost.setEntity(encodedFormEntity);
            }
         //三.执行请求
        CloseableHttpResponse response = client.execute(httpPost);
        //四.获取响应结果，封装httpresult中
        Integer code = response.getStatusLine().getStatusCode();
        //五.响应内容
        //判断返回结果是否为空
        String body = null;
        if (response.getEntity()!=null){
            body = EntityUtils.toString(response.getEntity(),"utf-8");
        }
        HttpResult httpResult = new HttpResult(code, body);
        return httpResult;
        }
//     5.put、、delete
}
