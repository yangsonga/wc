package com.mage.dao;

import java.util.List;

import com.mage.po.OutType;

public interface OutTypeService {
  List<OutType> queryParentType();
  List<OutType> queryChildType(int pid);
  OutType querySpecificParent(int id);
}
