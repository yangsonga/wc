package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.UserDao;
import com.mage.crm.dao.UserRoleDao;
import com.mage.crm.dto.UserDto;
import com.mage.crm.model.UserModel;
import com.mage.crm.query.UserQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.Md5Util;
import com.mage.crm.util.StringUtil;
import com.mage.crm.vo.User;
import com.mage.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private HttpSession session;

    public UserModel login(String userName, String userPwd) {
        //通过dao查询数据库
        User user = userDao.queryUserByName(userName);
        //判断是否存在该用户
        AssertUtil.isTrue(user==null,"用户不存在");
        //判断密码是否相等
        //将参数密码加密后与数据库密码比较
        userPwd = Md5Util.enCode(userPwd);
        AssertUtil.isTrue(!user.getUserPwd().equals(userPwd),"用户密码错误");
        //通过字段isValid判断用户是否有效
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已经被注销");
        //通过上面的判断后再返回结果
        //将权限集合存入session作用域
        //通过userId查询权限
        List<String> permissions = permissionDao.queryPermissionsByUid(user.getId());
        if(permissions!=null&&permissions.size()>0){
            session.setAttribute("userPermission",permissions);
        }
        //调用方法createUserModel获取返回结果
        return createUserModel(user);
    }
    private UserModel createUserModel(User user){
        UserModel userModel = new UserModel();
        userModel.setTrueName(user.getTrueName());
        userModel.setUserName(user.getUserName());
        //将id加密
        userModel.setId(Base64Util.encode(user.getId()));
        return userModel;
    }

    public void updatePwd(String id, String oldPassword, String newPassword, String confirmPassword) {
        //参数判断
        AssertUtil.isTrue(StringUtil.isEmpty(id),"id不存在");
        AssertUtil.isTrue(StringUtil.isEmpty(newPassword),"新密码不能为空");
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码与老密码不能一致");
        AssertUtil.isTrue(!newPassword.equals(confirmPassword),"新密码与确认密码不一致");
        //参数判断过后查询数据库,user对象与参数的关系
        id = Base64Util.deCode(id);
        User user = userDao.queryUserById(id);
        AssertUtil.isTrue(user==null,"用户不存在");
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已经被注销");
        oldPassword = Md5Util.enCode(oldPassword);
        //比较传参的老密码与数据库的密码
        AssertUtil.isTrue(!user.getUserPwd().equals(oldPassword),"用户密码错误");
        //更新密码
        AssertUtil.isTrue(userDao.updatePwd(id,Md5Util.enCode(newPassword))<1,"更新失败");
    }

    public User queryUserById(String id) {
          return userDao.queryUserById(id);
    }

    public List<User> queryAllCustomerManager() {
        return userDao.queryAllCustomerManager();
    }

    public Map<String, Object> queryUsersByParams(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPage(),userQuery.getRows());
        List<UserDto> userDtoList = userDao.queryUsersByParams(userQuery);
        //判断是否查询到数据
        if(userDtoList!=null&&userDtoList.size()>0){
           for(UserDto userDto:userDtoList){
               String roleIdsStr = userDto.getRoleIdsStr();
               if(!StringUtil.isEmpty(roleIdsStr)){
                   String[] roleIdArray = roleIdsStr.split(",");
                   for(int i=0;i<roleIdArray.length;i++){
                       List<Integer> roleIds = userDto.getRoleIds();
                       roleIds.add(Integer.parseInt(roleIdArray[i]));
                   }
               }
           }
        }
        PageInfo<UserDto> userPageInfo = new PageInfo<>(userDtoList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("rows",userPageInfo.getList());
        map.put("total",userPageInfo.getTotal());
        return map;
    }

    public void insert(User user) {
        //检查参数
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUserName()),"用户名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(user.getTrueName()),"真实姓名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(user.getPhone()),"手机号码不能为空");
        //不能插入相同的用户名
        User u = userDao.queryUserByName(user.getUserName());
        AssertUtil.isTrue(u!=null,"不能添加相同的用户名");
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设置默认密码
        user.setUserPwd(Md5Util.enCode("123456"));
        AssertUtil.isTrue(userDao.insert(user)<1,"插入用户失败");
        List<Integer> roleIds = user.getRoleIds();
        if(roleIds!=null&&roleIds.size()>0){
            relateRoles(roleIds,Integer.parseInt(user.getId()));
        }
    }

    private void relateRoles(List<Integer> roleIds, int userId) {
        List<UserRole> roleList=new ArrayList<UserRole>();
        for (Integer roleId:roleIds){
            UserRole userRole = new UserRole();
            userRole.setIsValid(1);
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            roleList.add(userRole);
        }
        AssertUtil.isTrue(userRoleDao.insertBacth(roleList)<1,"用户角色添加失败");
    }

    public void update(User user) {
        //检查参数
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUserName()),"用户名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(user.getTrueName()),"真实姓名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(user.getPhone()),"手机号码不能为空");
        user.setUpdateDate(new Date());
        User u = userDao.queryUserByName(user.getUserName());
        AssertUtil.isTrue(u!=null&&!u.getId().equals(user.getId()),"不能有相同用户名");
        AssertUtil.isTrue(userDao.update(user)<1,"用户修改失败");
        List<Integer> roleIds = user.getRoleIds();
           if(roleIds!=null&&roleIds.size()>0){
               //先删除，在插入
               //先查询，原来是否有用户角色
               int count = userRoleDao.queryRoleCountsByUserId(user.getId());
               if(count>0){
                   AssertUtil.isTrue(userRoleDao.deleteRolesByUserId(user.getId())<1,"用户更新失败");
               }
               //插入
               relateRoles(roleIds,Integer.parseInt(user.getId()));
           }
    }

    public void delete(Integer userId) {
        AssertUtil.isTrue(userDao.delete(userId)<1,"用户删除失败");
        int counts = userRoleDao.queryRoleCountsByUserId(String.valueOf(userId));
        if(counts>0){
            AssertUtil.isTrue(userRoleDao.deleteRolesByUserId(String.valueOf(userId))<counts,"用户角色删除失败");
        }
    }
}
