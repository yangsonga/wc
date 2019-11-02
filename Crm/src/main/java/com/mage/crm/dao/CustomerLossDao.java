package com.mage.crm.dao;

import com.mage.crm.query.CustomerLossQuery;
import com.mage.crm.vo.CustomerLoss;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface CustomerLossDao {
    List<CustomerLoss> queryCustomerLossesByParams(CustomerLossQuery customerLossQuery);

    Map queryCustomerLossesById(String lossId);

    @Update("update t_customer_loss set state=1,confirm_loss_time=now(),loss_reason=#{lossReason} where id=#{lossId}")
    int updateCustomerLossState(@Param("lossId") Integer lossId, @Param("lossReason") String lossReason);

    int insertBatch(List<CustomerLoss> customerLossList);
}
