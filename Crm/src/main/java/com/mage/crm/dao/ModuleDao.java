package com.mage.crm.dao;

import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleDao {

    List<ModuleDto> queryAllModuleDtos();

    Module queryModuleById(Integer moduleId);

    List<Module> queryModulesByParams(ModuleQuery moduleQuery);

    List<Module> queryModulesByGrade(Integer grade);

    Module queryModuleByPid(Integer parentId);

    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);

    Module queryModuleByOptValue(String optValue);

    int insert(Module module);

    int update(Module module);

    List<Module> querySubModulesByPid(Integer id);

    int delete(List<Integer> ids);
}
