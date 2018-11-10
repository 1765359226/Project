package com.taotao.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
    //判断数据是否可用
    TaotaoResult checkData(String data,int type);
    //注册
    TaotaoResult register(TbUser user);
    //登陆
    TaotaoResult login(String username,String password);
    //根据token查询用户信息
    TaotaoResult getUserByToken(String token);
}
