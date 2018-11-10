package com.taotao.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.controller.com.taotao.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/*
* 图片上传controller
* (无服务器)
* */


@RestController
public class PictureController {
    //取服务器地址
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping(value = "/pic/upload")
    public String picUpload(MultipartFile uploadFile){

        try {
//        接收上传文件
//        取扩展名
            String originalFilename = uploadFile.getOriginalFilename();//全名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);//取拓展名,,不加1是带"."的
//        上传到图片服务器
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/client.conf");//上传服务器地址
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            url = IMAGE_SERVER_URL + url;
//        响应上传图片的url
            //成功
            Map result = new HashMap<>();
            result.put("error",0);
            result.put("url",url);
            return JsonUtils.objectToJson(result);//解决浏览器兼容性问题 JsonUtils.objectToJson(result)
        } catch (Exception e) {
            e.printStackTrace();
            //异常,上传不成功
            Map result = new HashMap<>();
            result.put("error",1);
            result.put("massage","上传失败!");
            return JsonUtils.objectToJson(result);//解决浏览器兼容性问题 JsonUtils.objectToJson(result)
        }
    }
}
