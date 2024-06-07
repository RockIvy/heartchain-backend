//package com.ivy.heartchain.aspect;
//
//import com.ivy.heartchain.common.ErrorCode;
//import com.ivy.heartchain.contant.UserConstant;
//import com.ivy.heartchain.exception.BusinessException;
//import com.ivy.heartchain.model.domain.User;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 全局请求拦截器
// *
// * @author ivy
// * @date 2024/4/22 14:19
// */
//@Aspect
//@Slf4j
//@Component
//public class LogAndAuthAspect {
//    // 定义切入点，这里指的是UserController下的所有方法，排除登录和注册方法
//    @Pointcut("execution(* com.ivy.heartchain.controller.UserController.*(..)) " +
//
//            "&& !execution(* com.ivy.heartchain.controller.UserController.userLogin(..)) " +
//            "&& !execution(* com.ivy.heartchain.controller.UserController.userRegister(..))" +
//            "&& !execution(* com.ivy.heartchain.controller.UserController.updateCurrentUser(..))"
////            + "execution(* com.ivy.heartchain.controller.PostController.*(..)) " +
////            "&& !execution(* com.ivy.heartchain.controller.PostController.listPostWithUser(..)) " +
////            "&& !execution(* com.ivy.heartchain.controller.PostController.addPost(..))" +
////            "&& !execution(* com.ivy.heartchain.controller.PostController.postDoThumb(..))"
//    )
//    public void controllerLogAndAuth() {
//    }
//
//
//    // 定义前置通知，记录请求信息和进行权限验证
//    @Before("controllerLogAndAuth()")
//    public void beforeAdvice(JoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        // 记录请求内容，使用log对象进行日志记录
//        log.info("Request URL: {}", request.getRequestURL().toString());
//        log.info("HTTP Method: {}", request.getMethod());
//        log.info("IP: {}", request.getRemoteAddr());
//        log.info("Class Method: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
//
//        // 对于特定的方法，允许所有登录用户访问
//        if ("getCurrentUser".equals(joinPoint.getSignature().getName())) {
//            // 只要用户登录即可
//            if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
//                throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
//            }
//            // 直接返回，不进行后续的管理员权限验证
//            return;
//        }
//
//        // 权限验证逻辑
//        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
//        if (user == null) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
//        } else if (user.getUserRole() != UserConstant.ADMIN_ROLE) {
//            // 需要管理员权限
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无管理员权限");
//        }
//    }
//
//    // 环绕通知用于记录方法执行时间
//    @Around("controllerLogAndAuth()")
//    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
//        long startTime = System.currentTimeMillis();
//        Object result = joinPoint.proceed(); // 执行目标方法
//        long endTime = System.currentTimeMillis();
//        log.info("执行时间：{} ms", endTime - startTime);
//        return result;
//    }
//}
//
