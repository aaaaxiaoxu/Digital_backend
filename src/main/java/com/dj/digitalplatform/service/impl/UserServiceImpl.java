package com.dj.digitalplatform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.digitalplatform.exception.BusinessException;
import com.dj.digitalplatform.exception.ErrorCode;
import com.dj.digitalplatform.mapper.UserMapper;
import com.dj.digitalplatform.model.dto.user.UserQueryRequest;
import com.dj.digitalplatform.model.dto.user.UserQualificationQueryRequest;
import com.dj.digitalplatform.model.entity.User;
import com.dj.digitalplatform.model.enums.UserRoleEnum;
import com.dj.digitalplatform.model.vo.LoginUserVO;
import com.dj.digitalplatform.model.vo.UserVO;
import com.dj.digitalplatform.model.vo.UserQualificationVO;
import com.dj.digitalplatform.service.EmailService;
import com.dj.digitalplatform.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dj.digitalplatform.constant.UserConstant.USER_LOGIN_STATE;


/**
* @author XLW200420
* @description Service implementation for database operations on table【user】
* @createDate 2025-03-27 20:29:34
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private EmailService emailService;

    /**
     *
     * @param userAccount   User account
     * @param userPassword  User password
     * @param checkPassword Confirmation password
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String email, String code) {
        // 1. Validation
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Parameters cannot be empty");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User account is too short");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User password is too short");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Passwords don't match");
        }
        // 2. Check for duplicate accounts
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account already exists");
        }
        // 3. Encryption
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. Insert data
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        Boolean verified = emailService.checkVerificationCode(email, code);
        if (!verified) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Verification code error");
        }
        user.setEmail(email);
        user.setUserName("Anonymous");
        user.setUserRole(UserRoleEnum.USER.getValue());
        
        // Here save is done by the mybatis plus framework, which creates and assigns the id at the same time, so we can getid here
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Registration failed, database error");
        }
        return user.getId();
    }



    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. Validation
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Parameters cannot be empty");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account error");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Password error");
        }
        // 2. Encryption
        String encryptPassword = getEncryptPassword(userPassword);
        // Check if user exists
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // User does not exist
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User does not exist or password is incorrect");
        }

        // 3. Store user login state in session
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // Check if logged in
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // Query from database (use cache for better performance)
        Long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }


    /**
     * Get desensitized user information
     * @param user
     * @return
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }


    /**
     * Get desensitized user information
     *
     * @param user
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }


    /**
     * Get list of desensitized users
     * @param userList
     * @return
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }


    @Override
    public boolean userLogout(HttpServletRequest request) {
        // Check if logged in
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Not logged in");
        }
        // Remove login state
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Request parameters are empty");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(ObjUtil.isNotNull(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        // Use like for fuzzy search
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }



    /**
     * Get encrypted password
     * @param userPassword
     * @return
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // Salt value for password obfuscation
        final String SALT = "wyf_da_niu_niu";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

    /**
     * Update user qualification information
     * @param userId User ID
     * @param qualificationFileUrl Qualification file URL
     * @param education Education background
     * @param workplace Workplace/Study institution
     * @param city City of residence
     * @return Update result
     */
    @Override
    public boolean updateUserQualification(Long userId, String qualificationFileUrl, String education, String workplace, String city) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User ID is invalid");
        }
        
        // Check if user exists
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "User does not exist");
        }
        
        // Update user qualification information
        User updateUser = new User();
        updateUser.setId(userId);
        
        // Only update qualification file if URL is provided
        if (StrUtil.isNotBlank(qualificationFileUrl)) {
            updateUser.setQualificationFile(qualificationFileUrl);
            // Reset qualification status to pending when uploading new qualification file
            updateUser.setQualificationStatus(0);
        }
        
        // Update other information if provided
        if (StrUtil.isNotBlank(education)) {
            updateUser.setEducation(education);
        }
        if (StrUtil.isNotBlank(workplace)) {
            updateUser.setWorkplace(workplace);
        }
        if (StrUtil.isNotBlank(city)) {
            updateUser.setCity(city);
        }
        
        boolean result = this.updateById(updateUser);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Failed to update user qualification information");
        }
        
        return true;
    }

    /**
     * Get query wrapper for user qualification query
     * @param userQualificationQueryRequest Query request
     * @return QueryWrapper
     */
    @Override
    public QueryWrapper<User> getQualificationQueryWrapper(UserQualificationQueryRequest userQualificationQueryRequest) {
        if (userQualificationQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Request parameters are empty");
        }
        
        Long id = userQualificationQueryRequest.getId();
        String userAccount = userQualificationQueryRequest.getUserAccount();
        String userName = userQualificationQueryRequest.getUserName();
        Integer qualificationStatus = userQualificationQueryRequest.getQualificationStatus();
        String education = userQualificationQueryRequest.getEducation();
        String workplace = userQualificationQueryRequest.getWorkplace();
        String city = userQualificationQueryRequest.getCity();
        String sortField = userQualificationQueryRequest.getSortField();
        String sortOrder = userQualificationQueryRequest.getSortOrder();

        log.info("Query parameters: qualificationStatus={}, userAccount={}, userName={}", 
                qualificationStatus, userAccount, userName);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.eq(ObjUtil.isNotNull(qualificationStatus), "qualificationStatus", qualificationStatus);
        // Use like for fuzzy search
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(education), "education", education);
        queryWrapper.like(StrUtil.isNotBlank(workplace), "workplace", workplace);
        queryWrapper.like(StrUtil.isNotBlank(city), "city", city);
        
        // Only query users who have qualification file (have submitted qualification)
        queryWrapper.isNotNull("qualificationFile");
        
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), 
                sortOrder != null && sortOrder.equals("ascend"), sortField);
        
        log.info("Generated SQL: {}", queryWrapper.getTargetSql());
        return queryWrapper;
    }

    /**
     * Get user qualification VO
     * @param user User entity
     * @return UserQualificationVO
     */
    @Override
    public UserQualificationVO getUserQualificationVO(User user) {
        if (user == null) {
            return null;
        }
        UserQualificationVO userQualificationVO = new UserQualificationVO();
        BeanUtils.copyProperties(user, userQualificationVO);
        return userQualificationVO;
    }

    /**
     * Get list of user qualification VOs
     * @param userList User list
     * @return List of UserQualificationVO
     */
    @Override
    public List<UserQualificationVO> getUserQualificationVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserQualificationVO).collect(Collectors.toList());
    }

    /**
     * Audit user qualification
     * @param userId User ID
     * @param qualificationStatus Qualification status (0 = rejected, 1 = approved)
     * @return Audit result
     */
    @Override
    public boolean auditUserQualification(Long userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User ID is invalid");
        }
        
        // Update qualification status to 1 (approved)
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setQualificationStatus(1);
        
        boolean result = this.updateById(updateUser);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Failed to audit user qualification");
        }
        
        log.info("Admin approved user qualification: userId={}", userId);
        return true;
    }




}




