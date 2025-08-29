package com.dj.digitalplatform.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.digitalplatform.common.BaseResponse;
import com.dj.digitalplatform.common.ResultUtils;
import com.dj.digitalplatform.exception.BusinessException;
import com.dj.digitalplatform.exception.ErrorCode;
import com.dj.digitalplatform.exception.ThrowUtils;
import com.dj.digitalplatform.model.dto.user.UserLoginRequest;
import com.dj.digitalplatform.model.dto.user.UserQueryRequest;

import com.dj.digitalplatform.model.dto.user.UserQualificationAuditRequest;
import com.dj.digitalplatform.model.dto.user.UserQualificationQueryRequest;
import com.dj.digitalplatform.model.dto.user.UserQualificationRequest;
import com.dj.digitalplatform.model.dto.user.UserRegisterRequest;
import com.dj.digitalplatform.model.dto.user.UserUpdateRequest;
import com.dj.digitalplatform.model.entity.User;
import com.dj.digitalplatform.model.vo.LoginUserVO;
import com.dj.digitalplatform.model.vo.UserVO;
import com.dj.digitalplatform.model.vo.UserQualificationVO;
import com.dj.digitalplatform.service.UserService;
import com.dj.digitalplatform.manager.FileManager;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private FileManager fileManager;



    /**
     * User Register
     */
    @PostMapping("/register")
    public BaseResponse<UserVO> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // This is your own utility class
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        String code = userRegisterRequest.getCode();
        
        // It does not include the upload of avatars, and the upload is null
        long userId = userService.userRegister(userAccount, userPassword, checkPassword, email, code);
        User user = userService.getById(userId);
        return ResultUtils.success(userService.getUserVO(user));
    }


    /**
     * User Login
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // This is your own utility class
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }


    /**
     * Get the current user to be desensitized
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }


    /**
     * The user is logged in
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR );
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }




    /**
     * Get users based on ID (admins only)
     */
    @GetMapping("/get")
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * Get the wrapper class based on ID for the average user
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        // The logic here is that I call the above to get the user and then desensitize
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }



    /**
     * Update User
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        // Salt value, obfuscated passwords
        final String SALT = "wyf_da_niu_niu";
        String userPassword = userUpdateRequest.getUserPassword();

        BeanUtils.copyProperties(userUpdateRequest, user);
        if (userPassword != null) {
            user.setUserPassword(DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes()));
        }
//        user.setUserPassword(DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes()));
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * Pagination to get a list of user encapsulations (admins only)
     *
     * @param userQueryRequest 查询请求参数
     */
    // Its practical get request here can also be used, but here it is to receive an object, and it is more standardized to use post, and the range of parameters that can be passed by post is larger
    @PostMapping("/list/page/vo")
    // 这里的Page是mybatis为我们封装好的 ??????
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, pageSize),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    /**
     * Upload user qualification information
     */
    @PostMapping("/qualification/upload")
    public BaseResponse<Boolean> uploadUserQualification(
            @RequestParam("qualificationFile") MultipartFile qualificationFile,
            @RequestParam(value = "education", required = false) String education,
            @RequestParam(value = "workplace", required = false) String workplace,
            @RequestParam(value = "city", required = false) String city,
            HttpServletRequest request) {
        
        // Check if user is logged in
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        
        // Validate qualification file
        fileManager.validImage(qualificationFile);
        
        // Upload qualification file and get URL
        String qualificationFileUrl = fileManager.uploadImage(qualificationFile, "qualification");
        
        // Update user qualification information
        boolean result = userService.updateUserQualification(userId, qualificationFileUrl, education, workplace, city);
        
        return ResultUtils.success(result);
    }

    /**
     * Update user qualification information (without file upload)
     */
    @PostMapping("/qualification/update")
    public BaseResponse<Boolean> updateUserQualification(
            @RequestBody UserQualificationRequest userQualificationRequest,
            HttpServletRequest request) {
        
        ThrowUtils.throwIf(userQualificationRequest == null, ErrorCode.PARAMS_ERROR);
        
        // Check if user is logged in
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        
        String education = userQualificationRequest.getEducation();
        String workplace = userQualificationRequest.getWorkplace();
        String city = userQualificationRequest.getCity();
        
        // Update user qualification information (without changing qualification file)
        boolean result = userService.updateUserQualification(userId, null, education, workplace, city);
        
        return ResultUtils.success(result);
    }

    /**
     * Get users with qualification status 0 (pending/rejected) - Admin only
     */
    @PostMapping("/qualification/status0/list")
    public BaseResponse<Page<User>> listQualificationStatus0(
            @RequestBody UserQualificationQueryRequest userQualificationQueryRequest,
            HttpServletRequest request) {
        
        ThrowUtils.throwIf(userQualificationQueryRequest == null, ErrorCode.PARAMS_ERROR);
        
        // Check if user is admin
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "Only admin can access");
        
        // Force set qualification status to 0
        userQualificationQueryRequest.setQualificationStatus(0);
        
        long current = userQualificationQueryRequest.getCurrent();
        long pageSize = userQualificationQueryRequest.getPageSize();
        
        // Set default values if invalid
        if (current <= 0) current = 1;
        if (pageSize <= 0) pageSize = 10;
        
        Page<User> userPage = userService.page(new Page<>(current, pageSize),
                userService.getQualificationQueryWrapper(userQualificationQueryRequest));
        
        return ResultUtils.success(userPage);
    }

    /**
     * Get users with qualification status 1 (approved) - Admin only
     */
    @PostMapping("/qualification/status1/list")
    public BaseResponse<Page<User>> listQualificationStatus1(
            @RequestBody UserQualificationQueryRequest userQualificationQueryRequest,
            HttpServletRequest request) {
        
        ThrowUtils.throwIf(userQualificationQueryRequest == null, ErrorCode.PARAMS_ERROR);
        
        // Check if user is admin
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "Only admin can access");
        
        // Force set qualification status to 1
        userQualificationQueryRequest.setQualificationStatus(1);
        
        long current = userQualificationQueryRequest.getCurrent();
        long pageSize = userQualificationQueryRequest.getPageSize();
        
        // Set default values if invalid
        if (current <= 0) current = 1;
        if (pageSize <= 0) pageSize = 10;
        
        Page<User> userPage = userService.page(new Page<>(current, pageSize),
                userService.getQualificationQueryWrapper(userQualificationQueryRequest));
        
        return ResultUtils.success(userPage);
    }

    /**
     * Audit user qualification - Admin only (set status to 1)
     */
    @PostMapping("/qualification/audit")
    public BaseResponse<Boolean> auditUserQualification(
            @RequestBody UserQualificationAuditRequest userQualificationAuditRequest,
            HttpServletRequest request) {
        
        ThrowUtils.throwIf(userQualificationAuditRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userQualificationAuditRequest.getUserId() == null, ErrorCode.PARAMS_ERROR, "User ID cannot be null");
        
        // Check if user is admin
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR, "Only admin can audit qualifications");
        
        Long userId = userQualificationAuditRequest.getUserId();
        
        boolean result = userService.auditUserQualification(userId);
        
        return ResultUtils.success(result);
    }


}
