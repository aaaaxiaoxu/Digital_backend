package com.dj.digitalplatform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.digitalplatform.mapper.ProgramMapper;
import com.dj.digitalplatform.model.dto.program.ProgramQueryRequest;
import com.dj.digitalplatform.model.entity.Institution;
import com.dj.digitalplatform.model.entity.Program;
import com.dj.digitalplatform.model.vo.ProgramVO;
import com.dj.digitalplatform.model.vo.ProgramWithInstitutionVO;
import com.dj.digitalplatform.service.ProgramService;
import com.dj.digitalplatform.service.institutionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author xuliwei
* @description 针对表【program(专业信息表)】的数据库操作Service实现
* @createDate 2025-08-29 23:20:17
*/
@Service
@Slf4j
public class ProgramServiceImpl extends ServiceImpl<ProgramMapper, Program>
    implements ProgramService{

    @Resource
    private institutionService institutionService;

    @Override
    public ProgramVO getProgramVO(Program program) {
        if (program == null) {
            return null;
        }
        ProgramVO programVO = new ProgramVO();
        BeanUtils.copyProperties(program, programVO);
        // 字段已经统一为institutionId，不需要特殊处理
        return programVO;
    }

    @Override
    public List<ProgramVO> getProgramVOList(List<Program> programList) {
        if (CollUtil.isEmpty(programList)) {
            return CollUtil.newArrayList();
        }
        return programList.stream().map(this::getProgramVO).collect(Collectors.toList());
    }

    @Override
    public ProgramWithInstitutionVO getProgramWithInstitutionVO(Program program) {
        if (program == null) {
            return null;
        }
        ProgramWithInstitutionVO programWithInstitutionVO = new ProgramWithInstitutionVO();
        BeanUtils.copyProperties(program, programWithInstitutionVO);
        // 字段已经统一为institutionId，不需要特殊处理
        
        // 获取机构信息
        if (program.getInstitutionId() != null) {
            Institution institution = institutionService.getById(program.getInstitutionId());
            if (institution != null) {
                programWithInstitutionVO.setInstitution(institutionService.getInstitutionVO(institution));
            }
        }
        
        return programWithInstitutionVO;
    }

    @Override
    public List<ProgramWithInstitutionVO> getProgramWithInstitutionVOList(List<Program> programList) {
        if (CollUtil.isEmpty(programList)) {
            return CollUtil.newArrayList();
        }
        return programList.stream().map(this::getProgramWithInstitutionVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<Program> getQueryWrapper(ProgramQueryRequest programQueryRequest) {
        QueryWrapper<Program> queryWrapper = new QueryWrapper<>();
        if (programQueryRequest == null) {
            log.info("ProgramQueryRequest is null, returning empty query wrapper");
            return queryWrapper;
        }

        Long id = programQueryRequest.getId();
        String programName = programQueryRequest.getProgramName();
        String programNameEnglish = programQueryRequest.getProgramNameEnglish();
        String degreeType = programQueryRequest.getDegreeType();
        String languageOfInstruction = programQueryRequest.getLanguageOfInstruction();
        String deliveryMode = programQueryRequest.getDeliveryMode();
        Long institutionId = programQueryRequest.getInstitutionId();

        log.info("Query parameters: id={}, programName={}, degreeType={}, institutionId={}", 
                id, programName, degreeType, institutionId);

        // 根据条件构建查询
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StrUtil.isNotBlank(programName), "programName", programName);
        queryWrapper.like(StrUtil.isNotBlank(programNameEnglish), "programNameEnglish", programNameEnglish);
        queryWrapper.eq(StrUtil.isNotBlank(degreeType), "degreeType", degreeType);
        queryWrapper.eq(StrUtil.isNotBlank(languageOfInstruction), "languageOfInstruction", languageOfInstruction);
        queryWrapper.eq(StrUtil.isNotBlank(deliveryMode), "deliveryMode", deliveryMode);
        queryWrapper.eq(institutionId != null && institutionId > 0, "institution_id", institutionId);
        
        // 默认按创建时间降序排序
        queryWrapper.orderByDesc("createTime");
        
        log.info("Generated SQL: {}", queryWrapper.getTargetSql());
        return queryWrapper;
    }
}




