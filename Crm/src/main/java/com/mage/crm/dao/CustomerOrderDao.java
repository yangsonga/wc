package com.mage.crm.dao;

import com.mage.crm.vo.CustomerOrder;

import java.util.List;
import java.util.Map;

public interface CustomerOrderDao {

    List<CustomerOrder> queryOrdersByCid(Integer cid);

    Map<String, Object> queryOrderInfoById(Integer orderId);
}
