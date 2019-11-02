package com.mage.crm.dao;

import com.mage.crm.vo.DataDic;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DataDicDao {
    @Select("SELECT data_dic_value as dataDicValue FROM t_datadic WHERE is_valid=1 AND data_dic_name=#{dataDicName}")
    List<DataDic> queryDataDicValueByDataDicName(String dataDicName);
}
