package com.taotao.cart.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private ItemService itemService;
    //设置购物车key
    @Value("${CART_KEY}")
    private String CART_KEY;
    //购物车商品有效期,默认为7天
    @Value("${CART_EXPIER}")
    private Integer CART_EXPIER;

    /*
    *根据购物车的key，从cookie中取购物车商品列表
    */
    private List<TbItem> getCartItemList(HttpServletRequest request){
        //根据购物车的key查询
        String json = CookieUtils.getCookieValue(request,CART_KEY,true);
        if (StringUtils.isBlank(json)){//为空
            return new ArrayList<>();
        }
        //不为空,返回商品的集合
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }
    /*
    *把商品加入购物车
    *参数：
    *    itemId
    *    number
    * */
    @RequestMapping(value = "/cart/add/{itemId}")
    public String addItemCart(@PathVariable("itemId") Long itemId,
                                    @RequestParam(defaultValue = "1") Integer number,
                                    HttpServletRequest request, HttpServletResponse response){
        //取购物车商品列表
        List<TbItem> cartItemList = getCartItemList(request);
        //判断购物车是否为空
        // 不为空
        if (cartItemList!=null ||cartItemList.size()!=0) {
            //判断商品在cookie购物车中是否存在
            for (TbItem tbItem : cartItemList) {
                if (tbItem.getId().equals(itemId)) { //如果商品存在
                    //如果存在数量相加
                    tbItem.setNum(tbItem.getNum() + number);
                    //把购物车写入cookie
                    CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);
                    return "cartSuccess";
                }
            }
        }
        //为空
        //添加一个新的商品到cookie中
        //需要调用服务取商品信息
        TbItem item3 = itemService.getItemById(itemId);
        //设置购买数量
        item3.setNum(number);
        //取一张图片
        String image = item3.getImage();
        //判断取出的图片是否为空
        if (StringUtils.isNotBlank(image)){//不为空
            //转换成一个数组
            String[] images = image.split(",");
            //取第一张图片
            item3.setImage(images[0]);
        }
        //把新的商品插入购物车中
        cartItemList.add(item3);
        //把购物车写入cookie
        //CART_EXPIER #购物车商品有效期,默认为7天
        CookieUtils.setCookie(request,response,CART_KEY,JsonUtils.objectToJson(cartItemList),CART_EXPIER,true);
        //返回添加成功页面
        return "cartSuccess";
    }

    /*
    * 查看购物车
    * */
    @RequestMapping(value = "/cart/cart")
    public String Cart(HttpServletRequest request){
        //从cookie中取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);
        request.setAttribute("cartList",cartItemList);
        return "cart";
    }

    /*
    * 在购物车修改商品数量
    * 参数：
    *     itemId
    *     num修改后数量
    * */
    @RequestMapping(value = "/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult cartUpdateNum(@PathVariable Long itemId, @PathVariable("num") Integer number,
                                      HttpServletRequest request, HttpServletResponse response){
        //从cookie中获取购物车列表
        List<TbItem> cartList = getCartItemList(request);
        //取出该商品的信息
        for (TbItem tbItem : cartList){
            if (tbItem.getId().equals(itemId)){
                //修改商品数量
                tbItem.setNum(number);
                //退出循环
                break;
            }
        }
        //把购物车存到cookie中
        CookieUtils.setCookie(request,response,CART_KEY,JsonUtils.objectToJson(cartList),true);
        //重新刷新购物车
        return TaotaoResult.ok();
    }

    /*
    * 购物车订单删除
    * 参数：
    *      itemId
    * */
    @RequestMapping(value = "/cart/delete/{itemId}")
    public String deleteItemById(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
        // 从cookie中获取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);
       //找到对应的商品
        for (TbItem tbItem : cartItemList){
            if (tbItem.getId().equals(itemId)){
                //删除cookie中对应的商品
                cartItemList.remove(tbItem);
                //退出循环
                break;
            }
        }
        CookieUtils.setCookie(request,response,CART_KEY,JsonUtils.objectToJson(cartItemList),true);
        //redirect:重定向
        return "redirect:/cart/cart.html";
    }
}
