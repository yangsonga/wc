package com.mage.crm.dao;

import com.mage.crm.vo.OrderDetail;

import java.util.List;

public interface OrderDetailDao {
    List<OrderDetail> queryOrderDetailsByOrderId(Integer orderId);
}
