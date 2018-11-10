package com.taotao.freemarker;


import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerTest {
    @Test
    public void freeMarkerTest()throws Exception{
        //1.创建一个模板文件
        //2.创建一个Configuration对象
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        //3.设置模板所在路径
        configuration.setDirectoryForTemplateLoading(new File("D:/YQH/taotao/tatao-item-web/src/main/webapp/WEB-INF/ftl/"));
        //4.设置模板的字符集，一般utf-8
        configuration.setDefaultEncoding("utf-8");
        //5.使用Configeration对象加载一个模板文件，需要指定模板文件的文件名
        Template template = configuration.getTemplate("hello.ftl");
        //6.创建一个字符集，可以是pojo也可以是map。推荐使用map
        Map data = new HashMap<>();
            data.put("hello", "hello Freemarper");
        //7.创建一个Writer对象，指定输出文件的路径及文件名
        FileWriter out = new FileWriter("E:/学习/13天学习/out/hello.text");
        //8使用模板对象的process方法输出文件--指定输出路径和内容
        template.process(data,out);
        //9.关闭流
        out.close();
    }
}
