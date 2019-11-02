package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.query.OrderDetailQuery;
import com.mage.crm.service.OrderDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/order_detail")
public class OrderDetailController extends BaseController {
    @Resource
    private OrderDetailService orderDetailService;

    @RequestMapping("/queryOrderDetailsByOrderId")
    @ResponseBody
    public Map<String,Object> queryOrderDetailsByOrderId(OrderDetailQuery orderDetailQuery){
       return orderDetailService.queryOrderDetailsByOrderId(orderDetailQuery);
    }
}
