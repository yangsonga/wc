package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerDevPlanDao;
import com.mage.crm.query.CustomerDevPlanQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.CustomerDevPlan;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerDevPlanService {
    @Resource
    private CustomerDevPlanDao customerDevPlanDao;
    @Resource
    private SaleChanceService saleChanceService;

    public Map<String, Object> queryCusDevPlans(CustomerDevPlanQuery customerDevPlanQuery) {
        AssertUtil.isTrue(customerDevPlanQuery==null,"查询对象不能为空");
        PageHelper.startPage(customerDevPlanQuery.getPage(),customerDevPlanQuery.getRows());
        List<CustomerDevPlan> customerDevPlanList=customerDevPlanDao.queryCusDevPlans(customerDevPlanQuery.getSaleChanceId());
        PageInfo<CustomerDevPlan> customerDevPlanPageInfo = new PageInfo<>(customerDevPlanList);
        //创建返回的map集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",customerDevPlanPageInfo.getTotal());
        map.put("rows",customerDevPlanPageInfo.getList());
        return map;
    }

    public void insert(CustomerDevPlan customerDevPlan) {
        //再插入客户开发计划之前，先判断其对应的营销机会存不存在
        SaleChance saleChance = saleChanceService.querySaleChancesById(customerDevPlan.getSaleChanceId());
        //判断营销机会的参数
        AssertUtil.isTrue(saleChance==null,"营销机会已经不存在了");
        AssertUtil.isTrue(saleChance.getDevResult()==2,"营销机会已经完成了");
        AssertUtil.isTrue(saleChance.getDevResult()==3,"营销机会已经失败了");
        //给CustomerDevPlan中的参数设置值,一些属性不能从前台传过来
        //将该客户开发计划设置为有效
        customerDevPlan.setIsValid(1);
        customerDevPlan.setCreateDate(new Date());
        customerDevPlan.setUpdateDate(new Date());
        //调用dao层的方法，插入数据库
        AssertUtil.isTrue(customerDevPlanDao.insert(customerDevPlan)<1,"客户开发计划插入失败");
    }

    public void update(CustomerDevPlan customerDevPlan) {
        //再插入客户开发计划之前，先判断其对应的营销机会存不存在
        SaleChance saleChance = saleChanceService.querySaleChancesById(customerDevPlan.getSaleChanceId());
        //判断营销机会的参数
        AssertUtil.isTrue(saleChance==null,"营销机会已经不存在了");
        AssertUtil.isTrue(saleChance.getDevResult()==2,"营销机会已经完成了");
        AssertUtil.isTrue(saleChance.getDevResult()==3,"营销机会已经失败了");
        //设置更新时间
        customerDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(customerDevPlanDao.update(customerDevPlan)<1,"客户开发计划更新失败");
    }

    public void delete(Integer id) {
        AssertUtil.isTrue( customerDevPlanDao.delete(id)<1,"客户开发计划删除失败");
    }
}
