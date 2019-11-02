package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerDao;
import com.mage.crm.dao.CustomerLossDao;
import com.mage.crm.dto.CustomerDto;
import com.mage.crm.query.CustomerGCQuery;
import com.mage.crm.query.CustomerQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.StringUtil;
import com.mage.crm.vo.Customer;
import com.mage.crm.vo.CustomerLoss;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerLossDao customerLossDao;

    public List<Customer> queryAllCustomers() {
        List<Customer> customerList = customerDao.queryAllCustomers();
        return customerList;
    }

    public Map<String, Object> queryCustomersByParams(CustomerQuery customerQuery) {
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getRows());
        List<Customer> customerList = customerDao.queryCustomersByParams(customerQuery);
        PageInfo<Customer> customerPageInfo = new PageInfo<>(customerList);
        Map<String, Object> map = new HashMap<>();
        map.put("rows",customerPageInfo.getList());
        map.put("total",customerPageInfo.getTotal());
        return map;
    }

    public void insert(Customer customer) {
        AssertUtil.isTrue(StringUtil.isEmpty(customer.getFr()),"法人不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(customer.getName()),"客户名称不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(customer.getPhone()),"联系电话不能为空");
        customer.setIsValid(1);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        customer.setState(0);//未分配
        customer.setKhno("KH"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        AssertUtil.isTrue(customerDao.insert(customer)<1,"客户插入失败");
    }

    public void update(Customer customer) {
        AssertUtil.isTrue(StringUtil.isEmpty(customer.getFr()),"法人不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(customer.getName()),"客户名称不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(customer.getPhone()),"联系电话不能为空");
        customer.setUpdateDate(new Date());
        AssertUtil.isTrue(customerDao.update(customer)<1,"客户信息修改失败");
    }

    public void delete(Integer[] id) {
        AssertUtil.isTrue(customerDao.delete(id)<id.length,"客户信息删除失败");
    }

    public Customer queryCustomerById(Integer id) {
        AssertUtil.isTrue(id==null,"参数异常");
         return customerDao.queryCustomerById(id);
    }

    public Map<String, Object> queryCustomersContribution(CustomerGCQuery customerGCQuery) {
        PageHelper.startPage(customerGCQuery.getPage(),customerGCQuery.getRows());
        List<CustomerDto> customerDtoList = customerDao.queryCustomersContribution(customerGCQuery);
        PageInfo<CustomerDto> customerDtoPageInfo = new PageInfo<>(customerDtoList);
        Map<String, Object> map = new HashMap<>();
        map.put("rows",customerDtoPageInfo.getList());
        map.put("total",customerDtoPageInfo.getTotal());
        return map;
    }

    public Map<String, Object> queryCustomerGC() {
        List<CustomerDto> customerDtoList = customerDao.queryCustomerGC();
        /**
         * 准备 X坐标的数组 和 Y坐标的数组
         */
        String[] levels=null;
        Integer[] counts=null;
        Map<String,Object> map = new HashMap<>();
        map.put("code",300);
        if(customerDtoList!=null&&customerDtoList.size()>0){
            levels = new String[customerDtoList.size()];
            counts = new Integer[customerDtoList.size()];
            for(int i=0;i<customerDtoList.size();i++){
                levels[i] = customerDtoList.get(i).getLevel();
                counts[i] = customerDtoList.get(i).getCount();
            }
            map.put("code",200);
        }
        map.put("levels",levels);
        map.put("counts",counts);
        return map;
    }

    public void upateCustomerLossState() {
        List<CustomerLoss> customerLossList = customerDao.queryCustomerLoss();
        //把查询到的流失客户插入到t_customer_loss表中
        //把查询到的customerLoss集合添加额外的数据
        if(customerLossList!=null&&customerLossList.size()>0){
            for(int i=0;i<customerLossList.size();i++){
                CustomerLoss customerLoss = customerLossList.get(i);
                customerLoss.setCreateDate(new Date());
                customerLoss.setUpdateDate(new Date());
                customerLoss.setIsValid(1);
                customerLoss.setState(0);
            }
        }
        AssertUtil.isTrue(customerLossDao.insertBatch(customerLossList)<1,"客户流失数据添加失败");
    }
}
