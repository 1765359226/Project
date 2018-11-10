package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    //注入redis
    @Autowired
    private JedisClient jedisClient;
    //注入缓存过期时间
    @Value("${TIME_EXPIRE}")
    private Integer TIME_EXPIRE;
    //注入token前缀
    @Value("${USER_SESSION}")
    private String USER_SESSION;
    /*
     * 判断数据是否可用
     * */
    @Override
    public TaotaoResult checkData(String data, int type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //判断查询条件
        //1.判断用户名是否可用
        if(type==1){
            criteria.andUsernameEqualTo(data);
        //2.判断手机号是否可用
        } else if (type==2){
            criteria.andPhoneEqualTo(data);
        //3.判断邮箱是否可用
        }else if (type==3){
            criteria.andEmailEqualTo(data);
        }else {
            return TaotaoResult.build(400,"非法数据");
        }
        //执行查询
        List<TbUser> list = tbUserMapper.selectByExample(example);
        //判断查询数据是否为空
        if (list!=null&&list.size() > 0){
            //查到数据 ，返回false
            return TaotaoResult.ok(false);
        }
        //数据可用，返回true
        return TaotaoResult.ok(true);
    }

    /*
    * 注册
    * */
    @Override
    public TaotaoResult register(TbUser user) {
        //检查数据有效性
        //判断用户名是否为空
         if (StringUtils.isBlank(user.getUsername())){//为空
             return TaotaoResult.build(400,"用户名不能为空");
         }
         //判断用户名是否重复
        TaotaoResult taotaoResult = checkData(user.getUsername(), 1);
        if (!(boolean)taotaoResult.getData()){
            return TaotaoResult.build(400,"用户名不能重复");
        }
        //判断密码是否为空
        if (StringUtils.isBlank(user.getPassword())){//为空
            return TaotaoResult.build(400,"密码不能为空");
        }
        //判断手机号是否为空
        if (StringUtils.isNotBlank(user.getPhone())) {//不为空
            //判断手机号是否重复
            TaotaoResult taotaoResult1 = checkData(user.getPhone(), 2);
            if (!(boolean)taotaoResult1.getData()) {
                return TaotaoResult.build(400,"手机号不能重复");
            }
        }
        //判断邮箱是否为空
        if (StringUtils.isNotBlank(user.getEmail())){// 不为空
            //判断密码是否重复
            TaotaoResult taotaoResult2 = checkData(user.getEmail(), 3);
            if (!(boolean)taotaoResult2.getData()) {
                return TaotaoResult.build(400,"邮箱不能重复");
            }
        }
        //补全TbUser属性
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        //对密码进行md5加密
//        String md5Hash = new Md5Hash(user.getPassword().getBytes(),user.getUsername().getBytes()).toString();
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //插入数据
        tbUserMapper.insert(user);
        return TaotaoResult.ok();
    }

    /*
     * 登陆
     * 返回TaotaoResult
     * */
    @Override
    public TaotaoResult login(String username, String password) {
        //1.判断用户名密码是否为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return TaotaoResult.build(400,"用户名或密码不能为空！");
        }
        //2.判断用户名和密码是否正确
        //判断用户名是否存在
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list == null || list.size() == 0){
            return TaotaoResult.build(400,"用户名不存在");
        }

        //判断密码是否正确
        //密码要进行加密然后验证
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        TbUser tbUser = list.get(0);
        String password1 = tbUser.getPassword();
        if (!password1.equals(md5Password)){
            return TaotaoResult.build(400,"密码错误");
        }
        //设置认证主体请求
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        //认证
        try {
            subject.login(token);
            if (!subject.isAuthenticated()){
                return TaotaoResult.ok("登录失败");
            }
        }catch (Exception e){
            return TaotaoResult.ok(e.getMessage());
        }

        //3.把用户信息保存到redis缓存中
        //生成token记号。使用uuid..使用.toString是为了把UUID变成String类型
        String token1 = UUID.randomUUID().toString();
        //把用户信息保存到redis中，key就是token，value就是用户信息
        //把密码清空,再放进redis中，为了更安全
        tbUser.setPassword(null);
        jedisClient.set(USER_SESSION+":"+token,JsonUtils.objectToJson(tbUser));
        //设置key过期时间,提高缓存利用率
        jedisClient.expire(USER_SESSION+":"+token,TIME_EXPIRE);

        //6.返回登陆成功，其中要把token返回
        return TaotaoResult.ok(token);
    }

    /*
    * 根据token查询用户信息
    * 重置key，在redis的过期时间
    * 返回TaotaoResult
    * */
    @Override
    public TaotaoResult getUserByToken(String token) {
        //查询
        String json = jedisClient.get(USER_SESSION+":"+token);
        //判断json是否为空
        if (StringUtils.isBlank(json)){//为空
            //返回
            return TaotaoResult.build(400,"未登陆");
        }
        //重置key，在redis的过期时间
        jedisClient.expire(USER_SESSION+":"+token,TIME_EXPIRE);
        //把json转换成User对象
        TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
        //返回
        return TaotaoResult.ok(tbUser);
    }
}
