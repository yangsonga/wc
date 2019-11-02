package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerLossDao;
import com.mage.crm.query.CustomerLossQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.StringUtil;
import com.mage.crm.vo.CustomerLoss;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerLossService {
    @Resource
    private CustomerLossDao customerLossDao;

    public Map<String, Object> queryCustomerLossesByParams(CustomerLossQuery customerLossQuery) {
        Integer page = customerLossQuery.getPage();
        Integer rows = customerLossQuery.getRows();
        PageHelper.startPage(page,rows);
        List<CustomerLoss> customerLossList = customerLossDao.queryCustomerLossesByParams(customerLossQuery);
        PageInfo<CustomerLoss> customerLossPageInfo = new PageInfo<>(customerLossList);
        Map<String, Object> map = new HashMap<>();
        map.put("rows",customerLossPageInfo.getList());
        map.put("total",customerLossPageInfo.getTotal());
        return map;
    }

    public Map queryCustomerLossesById(String lossId) {
        AssertUtil.isTrue(StringUtil.isEmpty(lossId),"参数异常");
        Map customerLoss = customerLossDao.queryCustomerLossesById(lossId);
        return customerLoss;
    }

    public void updateCustomerLossState(Integer lossId,String lossReason) {
        AssertUtil.isTrue(null==lossId,"客户流失id不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(lossReason),"客户流失原因不能为空");
        Map map = customerLossDao.queryCustomerLossesById(lossId + "");
        AssertUtil.isTrue(map==null||map.isEmpty(),"客户流失记录不存在");
        AssertUtil.isTrue(customerLossDao.updateCustomerLossState(lossId,lossReason)<1,"客户流失失败");
    }
}
