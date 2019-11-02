package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.CustomerDevPlanQuery;
import com.mage.crm.service.CustomerDevPlanService;
import com.mage.crm.service.SaleChanceService;
import com.mage.crm.vo.CustomerDevPlan;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/cus_dev_plan")
public class CustomerDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CustomerDevPlanService customerDevPlanService;

    @RequestMapping("/index")
    public String index(Integer id, Model model){
        SaleChance saleChance = saleChanceService.querySaleChancesById(id);
        model.addAttribute("saleChance",saleChance);
        //返回cus_dev_plan_detail视图
        return "cus_dev_plan_detail";
    }
    @RequestMapping("/queryCusDevPlans")
    @ResponseBody
    public Map<String,Object> queryCusDevPlans(CustomerDevPlanQuery customerDevPlanQuery){
        return customerDevPlanService.queryCusDevPlans(customerDevPlanQuery);
    }
    @RequestMapping("/insert")
    @ResponseBody
    public MessageModel insert(CustomerDevPlan customerDevPlan){
        customerDevPlanService.insert(customerDevPlan);
        return createMessageModel("客户开发计划插入成功");
    }

    @RequestMapping("/update")
    @ResponseBody
    public MessageModel update(CustomerDevPlan customerDevPlan){
        customerDevPlanService.update(customerDevPlan);
        return createMessageModel("客户开发计划更新成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public MessageModel delete(Integer id){
        customerDevPlanService.delete(id);
        return createMessageModel("客户开发计划删除成功");
    }
}
