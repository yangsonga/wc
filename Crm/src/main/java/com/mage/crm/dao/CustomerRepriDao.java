package com.mage.crm.dao;

import com.mage.crm.vo.CustomerReprieve;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CustomerRepriDao {
    List<CustomerReprieve> customerReprieveByLossId(Integer lossId);

    int insertReprive(CustomerReprieve customerReprieve);

    @Update("UPDATE t_customer_reprieve SET is_valid = 0 WHERE id = #{id}")
    int deleteReprive(Integer id);
}
