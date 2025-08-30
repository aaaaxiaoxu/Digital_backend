package com.dj.digitalplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.digitalplatform.common.BaseResponse;
import com.dj.digitalplatform.common.DeleteRequest;
import com.dj.digitalplatform.common.ResultUtils;
import com.dj.digitalplatform.exception.BusinessException;
import com.dj.digitalplatform.exception.ErrorCode;
import com.dj.digitalplatform.exception.ThrowUtils;
import com.dj.digitalplatform.model.dto.program.ProgramAddRequest;
import com.dj.digitalplatform.model.dto.program.ProgramQueryRequest;
import com.dj.digitalplatform.model.dto.program.ProgramUpdateRequest;
import com.dj.digitalplatform.model.entity.Institution;
import com.dj.digitalplatform.model.entity.Program;
import com.dj.digitalplatform.model.entity.User;
import com.dj.digitalplatform.model.vo.ProgramVO;
import com.dj.digitalplatform.model.vo.ProgramWithInstitutionVO;
import com.dj.digitalplatform.service.ProgramService;
import com.dj.digitalplatform.service.UserService;
import com.dj.digitalplatform.service.institutionService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专业管理接口
 */
@RestController
@RequestMapping("/program")
public class ProgramController {

    @Resource
    private ProgramService programService;

    @Resource
    private UserService userService;

    @Resource
    private institutionService institutionService;

    /**
     * 创建专业
     */
    @PostMapping("/add")
    public BaseResponse<Long> addProgram(@RequestBody ProgramAddRequest programAddRequest,
                                       HttpServletRequest request) {
        ThrowUtils.throwIf(programAddRequest == null, ErrorCode.PARAMS_ERROR);
        
        // 检查用户是否为管理员
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "仅管理员可以创建专业");
        
        // 参数校验
        String programName = programAddRequest.getProgramName();
        ThrowUtils.throwIf(programName == null || programName.trim().isEmpty(), 
                          ErrorCode.PARAMS_ERROR, "专业名称不能为空");
        
        Long institutionId = programAddRequest.getInstitutionId();
        ThrowUtils.throwIf(institutionId == null || institutionId <= 0,
                          ErrorCode.PARAMS_ERROR, "所属机构ID不能为空");
        
        // 验证机构是否存在
        Institution institution = institutionService.getById(institutionId);
        ThrowUtils.throwIf(institution == null, ErrorCode.NOT_FOUND_ERROR, "所属机构不存在");
        
        // 创建专业实体
        Program program = new Program();
        BeanUtils.copyProperties(programAddRequest, program);
        
        // 保存到数据库
        boolean result = programService.save(program);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建专业失败");
        
        return ResultUtils.success(program.getId());
    }

    /**
     * 删除专业
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteProgram(@RequestBody DeleteRequest deleteRequest,
                                             HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        
        // 检查用户是否为管理员
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "仅管理员可以删除专业");
        
        long id = deleteRequest.getId();
        // 判断是否存在
        Program oldProgram = programService.getById(id);
        ThrowUtils.throwIf(oldProgram == null, ErrorCode.NOT_FOUND_ERROR);
        
        // 删除
        boolean result = programService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 更新专业
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateProgram(@RequestBody ProgramUpdateRequest programUpdateRequest,
                                             HttpServletRequest request) {
        ThrowUtils.throwIf(programUpdateRequest == null || programUpdateRequest.getId() == null, 
                          ErrorCode.PARAMS_ERROR);
        
        // 检查用户是否为管理员
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "仅管理员可以更新专业");
        
        // 判断是否存在
        long id = programUpdateRequest.getId();
        Program oldProgram = programService.getById(id);
        ThrowUtils.throwIf(oldProgram == null, ErrorCode.NOT_FOUND_ERROR);
        
        // 如果更新了机构ID，验证机构是否存在
        Long institutionId = programUpdateRequest.getInstitutionId();
        if (institutionId != null && institutionId > 0) {
            Institution institution = institutionService.getById(institutionId);
            ThrowUtils.throwIf(institution == null, ErrorCode.NOT_FOUND_ERROR, "所属机构不存在");
        }
        
        // 更新专业实体
        Program program = new Program();
        BeanUtils.copyProperties(programUpdateRequest, program);
        
        boolean result = programService.updateById(program);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取专业（仅管理员）
     */
    @GetMapping("/get")
    public BaseResponse<Program> getProgramById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        
        // 检查用户是否为管理员
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "仅管理员可以查看专业详情");
        
        Program program = programService.getById(id);
        ThrowUtils.throwIf(program == null, ErrorCode.NOT_FOUND_ERROR);
        
        return ResultUtils.success(program);
    }

    /**
     * 根据 id 获取专业视图对象（普通用户可访问）
     */
    @GetMapping("/get/vo")
    public BaseResponse<ProgramVO> getProgramVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        
        Program program = programService.getById(id);
        ThrowUtils.throwIf(program == null, ErrorCode.NOT_FOUND_ERROR);
        
        return ResultUtils.success(programService.getProgramVO(program));
    }

    /**
     * 根据 id 获取专业及其机构信息（普通用户可访问）
     */
    @GetMapping("/get/with-institution")
    public BaseResponse<ProgramWithInstitutionVO> getProgramWithInstitutionById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        
        Program program = programService.getById(id);
        ThrowUtils.throwIf(program == null, ErrorCode.NOT_FOUND_ERROR);
        
        return ResultUtils.success(programService.getProgramWithInstitutionVO(program));
    }

    /**
     * 分页获取专业列表（仅管理员）
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Program>> listProgramByPage(@RequestBody ProgramQueryRequest programQueryRequest,
                                                       HttpServletRequest request) {
        ThrowUtils.throwIf(programQueryRequest == null, ErrorCode.PARAMS_ERROR);
        
        // 检查用户是否为管理员
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "仅管理员可以查看专业列表");
        
        long current = programQueryRequest.getCurrent();
        long pageSize = programQueryRequest.getPageSize();
        
        // Set default values if invalid
        if (current <= 0) current = 1;
        if (pageSize <= 0) pageSize = 10;
        
        Page<Program> programPage = programService.page(new Page<>(current, pageSize),
                programService.getQueryWrapper(programQueryRequest));
        
        return ResultUtils.success(programPage);
    }

    /**
     * 分页获取专业视图对象列表（普通用户可访问）
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ProgramVO>> listProgramVOByPage(@RequestBody ProgramQueryRequest programQueryRequest) {
        ThrowUtils.throwIf(programQueryRequest == null, ErrorCode.PARAMS_ERROR);
        
        long current = programQueryRequest.getCurrent();
        long pageSize = programQueryRequest.getPageSize();
        
        // Set default values if invalid
        if (current <= 0) current = 1;
        if (pageSize <= 0) pageSize = 10;
        
        Page<Program> programPage = programService.page(new Page<>(current, pageSize),
                programService.getQueryWrapper(programQueryRequest));
        
        Page<ProgramVO> programVOPage = new Page<>(current, pageSize, programPage.getTotal());
        List<ProgramVO> programVOList = programService.getProgramVOList(programPage.getRecords());
        programVOPage.setRecords(programVOList);
        
        return ResultUtils.success(programVOPage);
    }

    /**
     * 分页获取专业及其机构信息列表（普通用户可访问）
     */
    @PostMapping("/list/page/with-institution")
    public BaseResponse<Page<ProgramWithInstitutionVO>> listProgramWithInstitutionByPage(@RequestBody ProgramQueryRequest programQueryRequest) {
        ThrowUtils.throwIf(programQueryRequest == null, ErrorCode.PARAMS_ERROR);
        
        long current = programQueryRequest.getCurrent();
        long pageSize = programQueryRequest.getPageSize();
        
        // Set default values if invalid
        if (current <= 0) current = 1;
        if (pageSize <= 0) pageSize = 10;
        
        Page<Program> programPage = programService.page(new Page<>(current, pageSize),
                programService.getQueryWrapper(programQueryRequest));
        
        Page<ProgramWithInstitutionVO> programWithInstitutionVOPage = new Page<>(current, pageSize, programPage.getTotal());
        List<ProgramWithInstitutionVO> programWithInstitutionVOList = programService.getProgramWithInstitutionVOList(programPage.getRecords());
        programWithInstitutionVOPage.setRecords(programWithInstitutionVOList);
        
        return ResultUtils.success(programWithInstitutionVOPage);
    }

    /**
     * 根据机构ID获取专业列表
     */
    @GetMapping("/list/by-institution/{institutionId}")
    public BaseResponse<List<ProgramVO>> getProgramsByInstitutionId(@PathVariable Long institutionId) {
        ThrowUtils.throwIf(institutionId == null || institutionId <= 0, ErrorCode.PARAMS_ERROR, "机构ID不能为空");
        
        // 验证机构是否存在
        Institution institution = institutionService.getById(institutionId);
        ThrowUtils.throwIf(institution == null, ErrorCode.NOT_FOUND_ERROR, "机构不存在");
        
        // 查询该机构下的所有专业
        ProgramQueryRequest queryRequest = new ProgramQueryRequest();
        queryRequest.setInstitutionId(institutionId);
        
        List<Program> programList = programService.list(programService.getQueryWrapper(queryRequest));
        List<ProgramVO> programVOList = programService.getProgramVOList(programList);
        
        return ResultUtils.success(programVOList);
    }
}
