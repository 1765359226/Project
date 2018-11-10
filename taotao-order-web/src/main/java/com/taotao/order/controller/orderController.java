package com.taotao.order.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class orderController {
    @Value("${CART_KEY}")
    private String CART_KEY;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    @Autowired
    private OrderService orderService;
    /*
    * 从cookie中获取购物车列表
    *
    * */
    public List<TbItem> getCartItemList(HttpServletRequest request){
        //获取购物车数据
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);
        //转换成TbItem集合类型
        List<TbItem> cartItemList = JsonUtils.jsonToList(json, TbItem.class);
        //返回
        return cartItemList;
    }
    /*
    *  从登陆拦截器放行，显示订单
    * */
    @RequestMapping(value = "/order/order-cart")
    public String showOrderCart(HttpServletRequest request){
        //用户必须是登录状态
        //获取用户id
        TbUser tbUser = (TbUser) request.getAttribute("tbUser");
        System.out.println(tbUser.getUsername());
        //根据用户信息获取收货地址，使用静态数据
        //把收货地址列表取出传递给页面
        // 从cookie中取购物车商品列表到页面中展示
        List<TbItem> cartItemList = getCartItemList(request);
        //返回逻辑视图
        request.setAttribute("cartList",cartItemList);
        return "order-cart";
    }
    /*
    * 订单提交完成页面
    *
    * */
    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, Model model){
        //生成订单
        TaotaoResult result = orderService.createOrder(orderInfo);
        //返回逻辑视图
        model.addAttribute("orderId",result.getData().toString());
        model.addAttribute("payment",orderInfo.getPayment());
        //预计送达时间，预计三天后送达
        DateTime dateTime = new DateTime();//当前时间
        dateTime = dateTime.plusDays(3);//三天后的时间
        model.addAttribute("date",dateTime.toString("yyyy-MM-dd"));
        return "success";
    }
}
