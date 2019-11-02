package com.mage.crm.task;

import com.mage.crm.service.CustomerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CustomerTask {
    @Resource
    private CustomerService customerService;

    //定时任务，把已经确认流失的客户找出，插入客户流失表中
    @Scheduled(cron = "0 36 20 * * ?")//每天的晚上8点触发这个任务
    public void lossTask(){
        customerService.upateCustomerLossState();
        System.out.println("我被触发了");
    }

}
