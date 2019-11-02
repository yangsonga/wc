package com.mage.crm.dao;

import com.mage.crm.dto.UserDto;
import com.mage.crm.query.UserQuery;
import com.mage.crm.vo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao {
    User queryUserByName(String userName);

    User queryUserById(String id);

    int updatePwd(@Param(value = "id") String id, @Param(value = "userPwd") String userPwd);

    @Select("SELECT u.true_name AS trueName FROM t_user u LEFT JOIN t_user_role ur ON u.id=ur.user_id\n" +
            "LEFT JOIN t_role r ON r.id=ur.role_id WHERE u.is_valid=1 AND ur.is_valid=1 AND r.is_valid=1 AND role_name='客户经理'")
    List<User> queryAllCustomerManager();

    List<UserDto> queryUsersByParams(UserQuery userQuery);


    int insert(User user);

    int update(User user);

    @Delete("delete from t_user where id=#{userId}")
    int delete(Integer userId);
}
