package com.ivy.heartchain.contant;

/**
 * @author ivy
 * @date 2024/4/11 21:18
 */
public interface UserConstant {
    //用户登录态键
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 用户权限
     */
    int DEFAULT_ROLE = 0;//普通用户，默认权限
    int ADMIN_ROLE = 1;//管理员
}
