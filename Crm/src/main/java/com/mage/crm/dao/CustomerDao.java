package com.mage.crm.dao;

import com.mage.crm.dto.CustomerDto;
import com.mage.crm.query.CustomerGCQuery;
import com.mage.crm.query.CustomerQuery;
import com.mage.crm.vo.Customer;
import com.mage.crm.vo.CustomerLoss;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CustomerDao {
    @Select("select id,name from t_customer where is_valid=1 and state=0")
    List<Customer> queryAllCustomers();

    List<Customer> queryCustomersByParams(CustomerQuery customerQuery);

    int insert(Customer customer);

    int update(Customer customer);

    int delete(Integer[] id);

    Customer queryCustomerById(Integer id);

    List<CustomerDto> queryCustomersContribution(CustomerGCQuery customerGCQuery);

    List<CustomerDto> queryCustomerGC();

    List<CustomerLoss> queryCustomerLoss();
}
