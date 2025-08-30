package com.dj.digitalplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dj.digitalplatform.model.dto.program.ProgramQueryRequest;
import com.dj.digitalplatform.model.entity.Program;
import com.dj.digitalplatform.model.vo.ProgramVO;
import com.dj.digitalplatform.model.vo.ProgramWithInstitutionVO;

import java.util.List;

/**
* @author xuliwei
* @description 针对表【program(专业信息表)】的数据库操作Service
* @createDate 2025-08-29 23:20:17
*/
public interface ProgramService extends IService<Program> {

    /**
     * 获取专业视图对象
     *
     * @param program 专业实体
     * @return ProgramVO
     */
    ProgramVO getProgramVO(Program program);

    /**
     * 获取专业视图对象列表
     *
     * @param programList 专业实体列表
     * @return List<ProgramVO>
     */
    List<ProgramVO> getProgramVOList(List<Program> programList);

    /**
     * 获取专业及其机构信息视图对象
     *
     * @param program 专业实体
     * @return ProgramWithInstitutionVO
     */
    ProgramWithInstitutionVO getProgramWithInstitutionVO(Program program);

    /**
     * 获取专业及其机构信息视图对象列表
     *
     * @param programList 专业实体列表
     * @return List<ProgramWithInstitutionVO>
     */
    List<ProgramWithInstitutionVO> getProgramWithInstitutionVOList(List<Program> programList);

    /**
     * 获取查询条件
     *
     * @param programQueryRequest 查询请求
     * @return QueryWrapper<Program>
     */
    QueryWrapper<Program> getQueryWrapper(ProgramQueryRequest programQueryRequest);
}
