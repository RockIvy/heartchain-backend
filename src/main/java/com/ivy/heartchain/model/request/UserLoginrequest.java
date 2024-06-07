package com.ivy.heartchain.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author ivy
 * @date 2024/4/11 19:17
 */
@Data
public class UserLoginrequest implements Serializable {

    private static final long serialVersionUID = -2406654025944442247L;
    private String userAccount;
    private String userPassword;

}
