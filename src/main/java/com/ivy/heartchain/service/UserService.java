package com.ivy.heartchain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ivy.heartchain.model.domain.User;
import com.ivy.heartchain.model.request.UserCreateRequest;
import com.ivy.heartchain.model.request.UserUpdateRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author ivy
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-04-10 16:57:51
*/
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param planetCode    星球编号
     * @param avatarUrl
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode, String avatarUrl, String username);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param request 请求
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户脱敏
     * @param originUser 起始用户
     * @return 安全用户
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销（退出登录）
     * @param request 请求
     * @return 1
     */
    int userLogout(HttpServletRequest request);

    /**
     * 用户更新
     * @param userUpdateRequest 需要更新的用户
     * @return 1
     */
    boolean updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 删除指定ID的用户
     * @param id 用户ID
     * @return 操作是否成功
     */
    boolean deleteUserById(Long id);

    /**
     * 管理员创建用户
     * @param createRequest
     * @return
     */
    boolean createUser(UserCreateRequest createRequest);

    /**
     * 根据标签搜索用户
     * @param tagNameList 用户拥有的标签
     * @return
     */
    List<User> searchUsersByTags(List<String> tagNameList) ;

    /**
     *  用户信息修改
     * @param user
     * @return
     */
    int updateUser(User user,User loginUser);

    /**
     *  获取当前用户信息
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);


    /**
     * 匹配用户
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);
}
