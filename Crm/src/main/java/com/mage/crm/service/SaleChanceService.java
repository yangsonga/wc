package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.SaleChanceDao;
import com.mage.crm.query.CustomerDevPlanQuery;
import com.mage.crm.query.SaleChanceQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.StringUtil;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService {

    @Resource
    private SaleChanceDao saleChanceDao;

    @RequestMapping("/querySaleChancesByParams")
    public Map<String, Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery) {
        //将当前页和每页大小放入PageHepler
        //pageHelper只支持使用后的下一条查询语句，而且是用limit ?,? 实现的
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getRows());
        //使用mybatis的分页功能
        List<SaleChance> saleChances = saleChanceDao.querySaleChancesByParams(saleChanceQuery);
        /*//将当前页和每页大小放入PageHepler
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getRows());*/
        PageInfo<SaleChance> saleChancePageInfo = new PageInfo<>(saleChances);
        //当前需要展示到前台的数据
        List<SaleChance> list = saleChancePageInfo.getList();
        //创建需要返回的map集合
        HashMap<String, Object> map = new HashMap<>();
        //将map集合对应成easyUI所需要的形式
        map.put("rows",list);
        map.put("total",saleChancePageInfo.getTotal());
        return map;
    }

    /**
     * 插入数据前的检测
     * 1.客户名称不能为空
     * 2.联系人不能为空
     * 3.联系电话不能为空
     * 额外添加的数据.
     *  is_valid=1
     *  createDate
     *  updateDate
     *  如果分配人不为空state=0 否在为1
     *  state 0 未分配 1 已分配
     *  开发结果 0-3代表 未开发 开发中 开发成功 开发失败
     * @param saleChance
     */
    public void insert(SaleChance saleChance) {
        //必须的三个参数的判定
        AssertUtil.isTrue(StringUtil.isEmpty(saleChance.getLinkMan()),"联系人不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(saleChance.getLinkPhone()),"联系电话不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(saleChance.getCustomerName()),"客户名称不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(saleChance.getCgjl()),"成功几率不能为空");
        //添加额外的数据
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        //如果分配人存在，state状态为1,否则为0
        if(StringUtil.isEmpty(saleChance.getAssignMan())){
            //分配人为空，状态码为0
            saleChance.setState(0);
        }else{
            //不为空，则为1
            saleChance.setState(1);
        }
        //调用saleChanceDao插入数据库
        AssertUtil.isTrue(saleChanceDao.insert(saleChance)<1,"营销机会添加失败");
    }

    public void update(SaleChance saleChance) {
        //必须的四个参数的判定
        AssertUtil.isTrue(StringUtil.isEmpty(saleChance.getLinkMan()),"联系人不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(saleChance.getLinkPhone()),"联系电话不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(saleChance.getCustomerName()),"客户名称不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(saleChance.getCgjl()),"成功几率不能为空");
        //判断该营销机会是否分配
        if(StringUtil.isEmpty(saleChance.getAssignMan())){
            //为空，则置state为0
            saleChance.setState(0);
        }else{
            //不为空，则已经分配
            saleChance.setState(1);
            //更新分配时间
            saleChance.setAssignTime(new Date());
        }
        //调用dao，进行数据库更新
        AssertUtil.isTrue(saleChanceDao.update(saleChance)<1,"营销机会更新失败");
    }

    public void delete(Integer[] id) {
        AssertUtil.isTrue(saleChanceDao.delete(id)<id.length,"营销机会删除失败");
    }

    public SaleChance querySaleChancesById(Integer id) {
        AssertUtil.isTrue(id==null,"saleChanceId不能为空");
        return saleChanceDao.querySaleChancesById(id);
    }

    public void updateSaleChanceDevResult(Integer devResult, Integer saleChanceId) {
        AssertUtil.isTrue(devResult==null,"营销结果不能为空");
        AssertUtil.isTrue(saleChanceId==null,"营销机会id不能为空");
        AssertUtil.isTrue(saleChanceDao.updateSaleChanceDevResult(devResult,saleChanceId)<1,"开发结果更新失败");
    }
}
