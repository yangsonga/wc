package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerLossDao;
import com.mage.crm.dao.CustomerRepriDao;
import com.mage.crm.query.CustomerRepriQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerRepriService {
    @Resource
    private CustomerRepriDao customerRepriDao;
    @Resource
    private CustomerLossDao customerLossDao;

    public Map<String, Object> customerReprieveByLossId(CustomerRepriQuery customerRepriQuery) {
        PageHelper.startPage(customerRepriQuery.getPage(),customerRepriQuery.getRows());
       List<CustomerReprieve> customerReprieveList = customerRepriDao.customerReprieveByLossId(customerRepriQuery.getLossId());
        PageInfo<CustomerReprieve> customerReprievePageInfo = new PageInfo<>(customerReprieveList);
        Map<String, Object> map = new HashMap<>();
        map.put("rows",customerReprievePageInfo.getList());
        map.put("total",customerReprievePageInfo.getTotal());
        return map;
    }

    public void insertReprive(CustomerReprieve customerReprieve) {
        //参数判断
        AssertUtil.isTrue(null==customerReprieve.getMeasure(),"暂缓措施不能为空");
        //lossId以及该id对应的customerLoss不能为空
        Map map = customerLossDao.queryCustomerLossesById(customerReprieve.getLossId() + "");
        AssertUtil.isTrue(map==null||customerReprieve.getLossId()==null||map.isEmpty(),"客户流失记录不存在");
        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());
        AssertUtil.isTrue(customerRepriDao.insertReprive(customerReprieve)<1,"客户暂缓信息插入失败");
    }

    public void deleteReprive(Integer id) {
        AssertUtil.isTrue(null==id,"参数异常");
        AssertUtil.isTrue(customerRepriDao.deleteReprive(id)<1,"客户暂缓信息删除失败");
    }
}
