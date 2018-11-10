package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    /*
    * 判断数据是否可用
    * */
    @RequestMapping(value = "/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkUserData(@PathVariable("param") String data,@PathVariable("type") int type){
        //执行查询
        TaotaoResult result = userService.checkData(data, type);
        //返回
        return result;
    }

    /*
     * 注册
     * 返回TaotaoResult
     * */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser user){
        //插入数据
        TaotaoResult result = userService.register(user);
        //返回
        return result;
    }
    /*
    * 登陆
    * 把用户存入cookie中
    * 返回TaotaoResult
    * */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username, String password, HttpServletResponse response, HttpServletRequest request){
        //查询
        TaotaoResult result = userService.login(username, password);
        //判断返回值是否为400,,如果是直接返回
        if (result.getStatus().equals(400)){
            return result;
        }
        //把token存入cookie中
        CookieUtils.setCookie(request,response,TOKEN_KEY,result.getData().toString());
        return result;
    }
    /*
     * 根据token查询用户信息
     * 返回TaotaoResult
     * */
    @RequestMapping(value = "/user/logout/{token}",method = RequestMethod.GET)
    @ResponseBody
    public Object logout(@PathVariable("token") String token, HttpServletRequest request, HttpServletResponse response,String callback){
        CookieUtils.deleteCookie(request,response,token);
        if (StringUtils.isBlank(callback)){
            return TaotaoResult.ok();
        }else {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(TaotaoResult.ok());
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
    }
    /*
     * 退出登录
     * 把cookie中的token删除
     * */
    @RequestMapping(value = "/user/token/{token}",method = RequestMethod.GET,
            //指定返回响应数据的content-type
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable("token") String token, String callback){
        TaotaoResult result = userService.getUserByToken(token);
        //判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)){//不为空
            //以js形式返回
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }
}
