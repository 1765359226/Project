package com.taotao.order.pojo;

import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

public class OrderInfo extends TbOrder  implements Serializable {
    private List<TbOrderItem> orderItems;
    private TbOrderShipping tbOrderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getTbOrderShipping() {
        return tbOrderShipping;
    }

    public void setTbOrderShipping(TbOrderShipping tbOrderShipping) {
        this.tbOrderShipping = tbOrderShipping;
    }
}
