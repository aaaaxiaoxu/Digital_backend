package com.dj.digitalplatform.aop;


import com.dj.digitalplatform.annotation.AuthCheck;
import com.dj.digitalplatform.exception.BusinessException;
import com.dj.digitalplatform.exception.ErrorCode;
import com.dj.digitalplatform.model.entity.User;
import com.dj.digitalplatform.model.enums.UserRoleEnum;
import com.dj.digitalplatform.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    // Most aspects can be solved with Around advice

    /**
     * Execute interception
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // Get current logged-in user
        User loginUser = userService.getLoginUser(request); // Based on our implementation in getLoginUser that throws an error if not logged in, this provides login verification capability
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // If no permission required, allow the request to proceed
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }
        // The code below requires proper permissions to pass
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // If admin permission is required but user doesn't have admin permission, deny access
        if (!userRoleEnum.ADMIN.equals(mustRoleEnum) && !userRoleEnum.ADMIN.equals(userRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // Allow the request to proceed
        return joinPoint.proceed();
    }
}
