package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.ModuleDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.RoleDao;
import com.mage.crm.dao.UserRoleDao;
import com.mage.crm.query.RoleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.StringUtil;
import com.mage.crm.vo.Module;
import com.mage.crm.vo.Permission;
import com.mage.crm.vo.Role;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService {
    @Resource
    private RoleDao roleDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private ModuleDao moduleDao;

    public List<Role> queryAllRoles() {
        return roleDao.queryAllRoles();
    }

    public Map<String, Object> queryRolesByParams(RoleQuery roleQuery) {
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getRows());
        List<Role> roleList = roleDao.queryRolesByParams(roleQuery.getRoleName());
        PageInfo<Role> rolePageInfo = new PageInfo<>(roleList);
        Map<String, Object> map = new HashMap<>();
        map.put("rows",rolePageInfo.getList());
        map.put("total",rolePageInfo.getTotal());
        return map;
    }

    public void insert(Role role) {
        //必要的参数判断
        AssertUtil.isTrue(StringUtil.isEmpty(role.getRoleName()),"角色名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(role.getRoleRemark()),"角色备注不能为空");
        //插入的角色名不能相同
        AssertUtil.isTrue(roleDao.queryRoleByRoleName(role.getRoleName())!=null,"角色名已存在");
        //额外的数据添加
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        AssertUtil.isTrue(roleDao.insert(role)<1,"角色插入失败");
    }

    public void update(Role role) {
        //必要的参数判断
        AssertUtil.isTrue(StringUtil.isEmpty(role.getRoleName()),"角色名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(role.getRoleRemark()),"角色备注不能为空");
        //修改的角色名不能相同,但是如果是自己就可以修改
        Role u = roleDao.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(u!=null&&!u.getId().equals(role.getId()),"角色名已存在");
        //修改role中的数据
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleDao.update(role)<1,"角色修改失败");
    }

    public void delete(Integer id) {
        AssertUtil.isTrue(roleDao.delete(id)<1,"角色删除失败");
        //删除用户角色表对应的记录
        AssertUtil.isTrue(userRoleDao.deleteRolesByRoleId(id)<1,"用户角色级联删除失败");
    }

    public void addPermission(Integer rid, Integer[] moduleIds) {
        //必要的参数判断
        AssertUtil.isTrue(rid==null||roleDao.queryRoleById(rid)==null,"角色不存在");
        //查询该角色之前有什么权限
       int count = permissionDao.queryPermissionCountByRid(rid);
       if(count>0){
           //把之前该角色的权限在权限表中删除
           AssertUtil.isTrue(permissionDao.deletePermissionsByRid(rid)<count,"角色权限更新失败");
       }
       List<Permission> permissionList=null;
       //将moduleIds中的权限添加到permission表中
        if(moduleIds!=null&&moduleIds.length>0){
            //实例化List<Permission>
            permissionList = new ArrayList<>();
            Module module = null;
            for(int i=0;i<moduleIds.length;i++){
                module = moduleDao.queryModuleById(moduleIds[i]);
                Permission permission = new Permission();
                if(module!=null){
                    permission.setAclValue(module.getOptValue());
                }
                //设置permission的数据
                permission.setModuleId(module.getId());
                permission.setRoleId(rid);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permissionList.add(permission);
            }
         AssertUtil.isTrue(permissionDao.insertBatch(permissionList)<moduleIds.length,"角色权限更新失败");
        }
    }
}
