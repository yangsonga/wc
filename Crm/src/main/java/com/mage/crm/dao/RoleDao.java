package com.mage.crm.dao;

import com.mage.crm.query.RoleQuery;
import com.mage.crm.vo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {
    List<Role> queryAllRoles();

    List<Role> queryRolesByParams(@Param("roleName") String roleName);

    int insert(Role role);

    Role queryRoleByRoleName(String roleName);

    int update(Role role);

    int delete(Integer id);

    Role queryRoleById(Integer rid);
}
