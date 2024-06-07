package com.ivy.heartchain.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivy.heartchain.common.BaseResponse;
import com.ivy.heartchain.common.ErrorCode;
import com.ivy.heartchain.common.ResultUtils;
import com.ivy.heartchain.exception.BusinessException;
import com.ivy.heartchain.model.domain.User;
import com.ivy.heartchain.model.request.UserCreateRequest;
import com.ivy.heartchain.model.request.UserLoginrequest;
import com.ivy.heartchain.model.request.UserRegisterRequest;
import com.ivy.heartchain.model.request.UserUpdateRequest;
import com.ivy.heartchain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户控制器
 *
 * @author ivy
 * @date 2024/4/11 17:11
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        String userAccount = userRegisterRequest.getUserAccount();
        String avatarUrl = userRegisterRequest.getAvatarUrl();
        String username = userRegisterRequest.getUsername();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            // 这里应该是抛异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }

        long id = userService.userRegister(userAccount, userPassword, checkPassword, planetCode, avatarUrl, username);
        return ResultUtils.success(id);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginrequest userLoginrequest, HttpServletRequest request) {
        if (userLoginrequest == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        String userAccount = userLoginrequest.getUserAccount();
        String userPassword = userLoginrequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        int i = userService.userLogout(request);
        return ResultUtils.success(i);
    }

    /**
     * 获取当前用户
     *
     * @param request 前端请求
     * @return 当前用户
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User currentUser = userService.getLoginUser(request);
        long id = currentUser.getId();
        // to do 校验用户是否合法
        User user = userService.getById(id);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }


    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestBody String username, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list();
        List<User> collect = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

    /**
     * 管理员通过用户ID删除用户。此操作要求执行者具有管理员权限。
     * 通过HTTP请求中的路径变量接收要删除的用户ID，并进行权限验证。
     * 如果当前操作用户是管理员，调用Service层执行删除操作。
     *
     * @param id      用户的唯一标识ID，通过URL路径传递。
     * @return 返回一个包含操作结果的响应体，操作成功返回true，失败返回false。
     */
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = userService.deleteUserById(id);
        return ResultUtils.success(result);
    }


    /**
     * 更新用户信息的接口。
     * 接受来自前端的请求，并通过权限校验确保当前操作由管理员执行。
     * 请求体中应包含要更新的用户信息。
     *
     * @param userUpdateRequest 前端传递的包含更新信息的请求体。
     * @return 返回更新操作的结果。如果更新成功，返回true；否则返回false。
     */
    @PostMapping("/updateUser")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {

        boolean result = userService.updateUser(userUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 处理管理员创建用户的请求。
     * 此接口仅允许管理员调用，用于创建新用户。
     *
     * @param createRequest 包含用户信息的请求体，需要符合用户创建的各项要求。
     * @return 返回操作的结果，创建成功时返回true，否则返回false。
     * @throws BusinessException 如果当前操作者不具备管理员权限，则抛出无权限的业务异常。
     */
    @PostMapping("/create")
    public BaseResponse<Boolean> createUser(@RequestBody UserCreateRequest createRequest) {

        boolean result = userService.createUser(createRequest);
        return ResultUtils.success(result);
    }

    @PutMapping("/updateCurrent")
    public BaseResponse<Integer> updateCurrentUser(@RequestBody User user) {
        boolean updated = userService.updateById(user);

        if (updated) {
            return ResultUtils.success(0);

        }
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "update user error");
    }

    /**
     * 根据标签搜索用户
     * @param tagNameList 用户拥有的标签
     * @return
     */
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList){
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUsersByTags(tagNameList);
        return ResultUtils.success(userList);

    }

    /**
     *      用户信息更新
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user , HttpServletRequest request) {
        //验证参数是否为空
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //鉴权
        User loginUser = userService.getLoginUser(request);
        int result = userService.updateUser(user,loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 推荐页面
     * @param request
     * @return
     */
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
        User logininUser = userService.getLoginUser(request);
        String redisKey = String.format("sanshui:user:recommend:%s",logininUser.getId());
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //如果有缓存，直接读取
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        if (userPage != null){
            return ResultUtils.success(userPage);
        }
        //无缓存，查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum,pageSize),queryWrapper);
        //写缓存,10s过期
        try {
            valueOperations.set(redisKey,userPage,30000, TimeUnit.MILLISECONDS);
        } catch (Exception e){
            log.error("redis set key error",e);
        }
        return ResultUtils.success(userPage);
    }

    /**
     * 获取最匹配的用户
     *
     * @param num
     * @param request
     * @return
     */
    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request) {
        if (num <= 0 || num > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.matchUsers(num, user));
    }

}
