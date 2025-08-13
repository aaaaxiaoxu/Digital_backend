package com.dj.digitalplatform.controller;

import com.dj.digitalplatform.common.BaseResponse;
import com.dj.digitalplatform.common.ResultUtils;
import com.dj.digitalplatform.exception.ErrorCode;
import com.dj.digitalplatform.exception.ThrowUtils;
import com.dj.digitalplatform.model.dto.email.EmailRequest;
import com.dj.digitalplatform.model.vo.EmailVO;
import com.dj.digitalplatform.service.EmailService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/*@author LucioSun
 * version 1.0
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Resource
    private EmailService emailService;

    @PostMapping("/verification-code/send")
    public BaseResponse<EmailVO> sendVerificationCode(@RequestBody EmailRequest emailRequest) {
        ThrowUtils.throwIf(emailRequest == null || emailRequest.getEmail() == null,
            ErrorCode.PARAMS_ERROR, "Email cannot be null");
        
        EmailVO emailVO = new EmailVO();
        boolean success = emailService.sendVerificationCode(emailRequest.getEmail(), emailVO);
        ThrowUtils.throwIf(!success, ErrorCode.OPERATION_ERROR, "Failed to send verification code");
        
        return ResultUtils.success(emailVO);
    }

    @PostMapping("/verification-code/check")
    public BaseResponse<Boolean> checkVerificationCode(@RequestBody EmailRequest emailRequest) {
        ThrowUtils.throwIf(emailRequest == null || emailRequest.getEmail() == null || emailRequest.getCode() == null,
            ErrorCode.PARAMS_ERROR, "Email or code cannot be null");

        boolean success = emailService.checkVerificationCode(emailRequest.getEmail(), emailRequest.getCode());
        ThrowUtils.throwIf(!success, ErrorCode.OPERATION_ERROR, "Failed to check verification code");
        return ResultUtils.success(success);
    }
}