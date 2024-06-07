package com.ivy.heartchain.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author ivy
 * @date 2024/4/11 19:15
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -2406654025944442247L;
    private String userAccount;
    private String userPassword;
    private String planetCode;
    private String checkPassword;
    private String avatarUrl;
    private String username;
}
