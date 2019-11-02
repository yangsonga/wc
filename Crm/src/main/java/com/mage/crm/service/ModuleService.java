package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.ModuleDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.StringUtil;
import com.mage.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ModuleService {
    @Resource
    private ModuleDao moduleDao;
    @Resource
    private PermissionDao permissionDao;

    public List<ModuleDto> queryAllModuleDtos(Integer rid) {
        //从module表中查询到所有的moduleDto对象
        List<ModuleDto> moduleDtoList = moduleDao.queryAllModuleDtos();
        //根据rid，从perssion表中得到moduleId
        List<Integer>  moduleIdList = permissionDao.queryPermissionModuleIdsByRid(rid);
        //循环遍历moduleDtoList集合，如果ModuleDto存在moduleId,则勾选
        for (ModuleDto moduleDto:moduleDtoList) {
            //如果moduleIdList包含该moduleId,则改为选中状态
            if(moduleIdList.contains(moduleDto.getId())){
                moduleDto.setChecked(true);
            }
        }
        return moduleDtoList;
    }

    public Map<String, Object> queryModulesByParams(ModuleQuery moduleQuery) {
        PageHelper.startPage(moduleQuery.getPage(),moduleQuery.getRows());
        List<Module> moduleList = moduleDao.queryModulesByParams(moduleQuery);
        PageInfo<Module> modulePageInfo = new PageInfo<>(moduleList);
        Map<String, Object> map = new HashMap<>();
        map.put("rows",modulePageInfo.getList());
        map.put("total",modulePageInfo.getTotal());
        return map;
    }

    public List<Module> queryModulesByGrade(Integer grade) {
        AssertUtil.isTrue(grade==null,"查询等级不能为空");
         return moduleDao.queryModulesByGrade(grade);
    }

    public void insert(Module module) {
        /**
         * 1.参数校验
         *    模块名称非空
         *    层级 非空
         *    模块权限值 非空
         * 2.权限值不能重复
         * 3.每一层  模块名称不能重复
         * 4.非根级菜单 上级菜单必须存在
         * 5.设置额外字段
         *     isValid
         *     createDate
         *     updateDate
         * 6.执行添加
         */
        AssertUtil.isTrue(StringUtil.isEmpty(module.getModuleName()),"模块名称不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(module.getGrade()+""),"层级不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(module.getOptValue()),"模块权限值不能为空");
        AssertUtil.isTrue(moduleDao.queryModuleByOptValue(module.getOptValue())!=null,"权限值不能重复");
        AssertUtil.isTrue(moduleDao.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName())!=null,"该层级下已存在该模块");
        if(module.getGrade()!=0){
            AssertUtil.isTrue(moduleDao.queryModuleByPid(module.getParentId())==null,"该父级模块不存在");
        }
        //额外的数据设置
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        module.setIsValid(1);
        //插入数据库操作
        AssertUtil.isTrue(moduleDao.insert(module)<1,"模块插入失败");
    }

    public void update(Module module) {
        /**
         * 1.参数校验
         *    模块名称非空
         *    层级 非空
         *    模块权限值 非空
         *    id 记录必须存在
         * 2.权限值不能重复
         * 3.每一层  模块名称不能重复
         * 4.非根级菜单 上级菜单必须存在
         * 5.设置额外字段
         *     updateDate
         * 6.执行更新
         */
        AssertUtil.isTrue(module.getId()==null||moduleDao.queryModuleById(module.getId())==null,"待更新的模块名不存在");
        AssertUtil.isTrue(StringUtil.isEmpty(module.getModuleName()),"模块名称不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(module.getGrade()+""),"层级不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(module.getOptValue()),"模块权限值不能为空");
        //判断权限值是否重复
        Module optValueModule = moduleDao.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(optValueModule!=null&&!optValueModule.getId().equals(module.getId()),"权限值不能重复");
        //判断层级下是否存在该模块
        Module temp = moduleDao.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName());
        AssertUtil.isTrue(temp!=null&&!temp.getId().equals(module.getId()),"该层级下模块不能重复");
        if(module.getGrade()!=0){
            AssertUtil.isTrue(moduleDao.queryModuleByPid(module.getParentId())==null,"该父级模块不存在");
        }
        //设置参数
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleDao.update(module) < 1, "模块更新失败");
    }

    public void delete(Integer id) {
        //将模块删除后，也要将模块所对应的子模块删除
        AssertUtil.isTrue(id==null||moduleDao.queryModuleById(id)==null,"待删除的模块不存在");
        List<Integer> ids = new ArrayList<>();
        ids = getSubModuleIds(id, ids);
        AssertUtil.isTrue(moduleDao.delete(ids)<ids.size(),"模块删除失败");
    }

    /**
     *
     * @param id  所要删除的根id
     * @param ids 所有要删除的id集合，包括根id
     * @return
     */
    public List<Integer> getSubModuleIds(Integer id,List<Integer> ids){
        Module module = moduleDao.queryModuleById(id);
        if(module!=null){
            ids.add(module.getId());
            //查询该module的下面的子节点
            List<Module> moduleList = moduleDao.querySubModulesByPid(module.getId());
            for (Module m:moduleList) {
                 ids = getSubModuleIds(m.getId(), ids);
            }
        }
        return ids;
    }
}
