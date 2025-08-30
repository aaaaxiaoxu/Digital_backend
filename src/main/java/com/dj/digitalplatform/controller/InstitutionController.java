package com.dj.digitalplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.digitalplatform.common.BaseResponse;
import com.dj.digitalplatform.common.DeleteRequest;
import com.dj.digitalplatform.common.ResultUtils;
import com.dj.digitalplatform.exception.BusinessException;
import com.dj.digitalplatform.exception.ErrorCode;
import com.dj.digitalplatform.exception.ThrowUtils;
import com.dj.digitalplatform.model.dto.institution.InstitutionAddRequest;
import com.dj.digitalplatform.model.dto.institution.InstitutionQueryRequest;
import com.dj.digitalplatform.model.dto.institution.InstitutionUpdateRequest;
import com.dj.digitalplatform.model.entity.User;
import com.dj.digitalplatform.model.entity.Institution;
import com.dj.digitalplatform.model.vo.InstitutionVO;
import com.dj.digitalplatform.service.UserService;
import com.dj.digitalplatform.service.institutionService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 机构管理接口
 */
@RestController
@RequestMapping("/institution")
public class InstitutionController {

    @Resource
    private institutionService institutionService;

    @Resource
    private UserService userService;

    /**
     * 创建机构
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInstitution(@RequestBody InstitutionAddRequest institutionAddRequest,
                                           HttpServletRequest request) {
        ThrowUtils.throwIf(institutionAddRequest == null, ErrorCode.PARAMS_ERROR);
        
        // 检查用户是否为管理员
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "仅管理员可以创建机构");
        
        // 参数校验
        String institutionName = institutionAddRequest.getInstitutionName();
        ThrowUtils.throwIf(institutionName == null || institutionName.trim().isEmpty(), 
                          ErrorCode.PARAMS_ERROR, "机构名称不能为空");
        
        // 创建机构实体
        Institution institution = new Institution();
        BeanUtils.copyProperties(institutionAddRequest, institution);
        
        // 保存到数据库
        boolean result = institutionService.save(institution);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建机构失败");
        
        return ResultUtils.success(institution.getId());
    }

    /**
     * 删除机构
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInstitution(@RequestBody DeleteRequest deleteRequest,
                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        
        // 检查用户是否为管理员
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "仅管理员可以删除机构");
        
        long id = deleteRequest.getId();
        // 判断是否存在
        Institution oldInstitution = institutionService.getById(id);
        ThrowUtils.throwIf(oldInstitution == null, ErrorCode.NOT_FOUND_ERROR);
        
        // 删除
        boolean result = institutionService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 更新机构
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInstitution(@RequestBody InstitutionUpdateRequest institutionUpdateRequest,
                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(institutionUpdateRequest == null || institutionUpdateRequest.getId() == null, 
                          ErrorCode.PARAMS_ERROR);
        
        // 检查用户是否为管理员
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "仅管理员可以更新机构");
        
        // 判断是否存在
        long id = institutionUpdateRequest.getId();
        Institution oldInstitution = institutionService.getById(id);
        ThrowUtils.throwIf(oldInstitution == null, ErrorCode.NOT_FOUND_ERROR);
        
        // 更新机构实体
        Institution institution = new Institution();
        BeanUtils.copyProperties(institutionUpdateRequest, institution);
        
        boolean result = institutionService.updateById(institution);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取机构（仅管理员）
     */
    @GetMapping("/get")
    public BaseResponse<Institution> getInstitutionById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        
        // 检查用户是否为管理员
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "仅管理员可以查看机构详情");
        
        Institution institution = institutionService.getById(id);
        ThrowUtils.throwIf(institution == null, ErrorCode.NOT_FOUND_ERROR);
        
        return ResultUtils.success(institution);
    }

    /**
     * 根据 id 获取机构视图对象（普通用户可访问）
     */
    @GetMapping("/get/vo")
    public BaseResponse<InstitutionVO> getInstitutionVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        
        Institution institution = institutionService.getById(id);
        ThrowUtils.throwIf(institution == null, ErrorCode.NOT_FOUND_ERROR);
        
        return ResultUtils.success(institutionService.getInstitutionVO(institution));
    }

    /**
     * 分页获取机构列表（仅管理员）
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Institution>> listInstitutionByPage(@RequestBody InstitutionQueryRequest institutionQueryRequest,
                                                               HttpServletRequest request) {
        ThrowUtils.throwIf(institutionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        
        // 检查用户是否为管理员
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "仅管理员可以查看机构列表");
        
        long current = institutionQueryRequest.getCurrent();
        long pageSize = institutionQueryRequest.getPageSize();
        
        // Set default values if invalid
        if (current <= 0) current = 1;
        if (pageSize <= 0) pageSize = 10;
        
        Page<Institution> institutionPage = institutionService.page(new Page<>(current, pageSize),
                institutionService.getQueryWrapper(institutionQueryRequest));
        
        return ResultUtils.success(institutionPage);
    }

    /**
     * 分页获取机构视图对象列表（普通用户可访问）
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<InstitutionVO>> listInstitutionVOByPage(@RequestBody InstitutionQueryRequest institutionQueryRequest) {
        ThrowUtils.throwIf(institutionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        
        long current = institutionQueryRequest.getCurrent();
        long pageSize = institutionQueryRequest.getPageSize();
        
        // Set default values if invalid
        if (current <= 0) current = 1;
        if (pageSize <= 0) pageSize = 10;
        
        Page<Institution> institutionPage = institutionService.page(new Page<>(current, pageSize),
                institutionService.getQueryWrapper(institutionQueryRequest));
        
        Page<InstitutionVO> institutionVOPage = new Page<>(current, pageSize, institutionPage.getTotal());
        List<InstitutionVO> institutionVOList = institutionService.getInstitutionVOList(institutionPage.getRecords());
        institutionVOPage.setRecords(institutionVOList);
        
        return ResultUtils.success(institutionVOPage);
    }
}
