package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerServeDao;
import com.mage.crm.dto.ServeTypeDto;
import com.mage.crm.query.CustomerServeQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.util.StringUtil;
import com.mage.crm.vo.CustomerServe;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServeService {
    @Resource
    private CustomerServeDao customerServeDao;

    /**
     * 必要性的检测
     * 1.服务类型
     * 2.客户
     * 3.服务请求
     * 额外数据的补录
     *  1.创建时间
     *  2.更新时间
     *  3.isvalid = 1
     *  4.state = 1   1- 5 新创建 1、已分配 2、已处理3 、已反馈4、已归档 5
     *
     * @param customerServe
     */
    public void insert(CustomerServe customerServe) {
        checkParms(customerServe.getServeType(),customerServe.getCustomer(),customerServe.getServiceRequest());
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());
        customerServe.setIsValid(1);
        customerServe.setState("1");
        AssertUtil.isTrue(customerServeDao.insert(customerServe)<1,"创建服务失败");
    }

    private void checkParms(String serveType,String customer,String serviceRequest){
        AssertUtil.isTrue(StringUtil.isEmpty(serveType),"服务类型不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(customer),"顾客不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(serviceRequest),"服务请求不能为空");
    }

    public Map<String, Object> queryCustomerServesByParams(CustomerServeQuery customerServeQuery) {
        PageHelper.startPage(customerServeQuery.getPage(),customerServeQuery.getRows());
        List<CustomerServe> customerServeList = customerServeDao.queryCustomerServesByParams(customerServeQuery.getState());
        PageInfo<CustomerServe> customerServePageInfo = new PageInfo<>(customerServeList);
        Map<String, Object> map = new HashMap<>();
        map.put("rows",customerServePageInfo.getList());
        map.put("total",customerServePageInfo.getTotal());
        return map;
    }

    public void update(CustomerServe customerServe, HttpServletRequest request) {
        //1为创建,2为服务处理，3为服务反馈，5为服务归档
        customerServe.setUpdateDate(new Date());
        //该state为前台定义好发送过来
        if("2".equals(customerServe.getState())){
            //如果为2，服务分配
            customerServe.setAssigner(CookieUtil.getCookieValue(request,"trueName"));
            customerServe.setAssignTime(new Date());
        }else if("3".equals(customerServe.getState())){
            //为3，则为服务处理
            AssertUtil.isTrue(StringUtil.isEmpty(customerServe.getServiceProce()),"处理内容不能为空");
            customerServe.setServiceProceTime(new Date());
        }else if("4".equals(customerServe.getState())){
            //服务反馈
            AssertUtil.isTrue(StringUtil.isEmpty(customerServe.getServiceProceResult()),"处理结果不能为空");
            AssertUtil.isTrue(StringUtil.isEmpty(customerServe.getMyd()),"满意度不能为空");
            //将状态修改为5，代表服务归档
            customerServe.setState("5");
        }
        AssertUtil.isTrue(customerServeDao.update(customerServe)<1,"服务更新失败");
    }

    public Map<String, Object> queryCustomerServeType() {
        List<ServeTypeDto> serveTypeDtoList = customerServeDao.queryCustomerServeType();
         String[] types=null;
        ServeTypeDto[] datas=null;
        Map<String, Object> map = new HashMap<>();
        map.put("code",300);
        if(serveTypeDtoList!=null&&serveTypeDtoList.size()>0){
            types = new String[serveTypeDtoList.size()];
            datas = new ServeTypeDto[serveTypeDtoList.size()];
           for(int i=0;i<serveTypeDtoList.size();i++){
               types[i] = serveTypeDtoList.get(i).getName();
               datas[i] = serveTypeDtoList.get(i);
           }
           map.put("code",200);
        }
        map.put("types",types);
        map.put("datas",datas);
        return map;
    }
}
