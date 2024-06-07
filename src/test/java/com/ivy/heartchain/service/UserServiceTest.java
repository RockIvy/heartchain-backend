package com.ivy.heartchain.service;

import com.ivy.heartchain.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;


/**
 * @author ivy
 * @date 2024/4/10 17:12
 */

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testAddUser()
    {
        User user = new User();
        user.setUsername("ania");
        user.setUserAccount("123456");
        user.setAvatarUrl("https://www.rockivy.cn/images/avatar.jpg");
        user.setGender(0);
        user.setUserPassword("123");
        user.setEmail("zxc");
        user.setPhone("vbn");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }


    @Test
    void userRegister() {
        //  a. 非空
        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "12345678";
        String planetCode = "1";
        String avatarUrl = "ashdkad";
        String username = "hfs";
        long result = userService.userRegister(userAccount,userPassword,checkPassword, planetCode, avatarUrl, username);
        Assertions.assertEquals(-1, result);
        //  b. 账户不小于 4 位
        userAccount = "yu";
        userPassword = "12345678";
        result = userService.userRegister(userAccount,userPassword,checkPassword, planetCode, avatarUrl, username);
        Assertions.assertEquals(-1, result);

        //  c. 密码不小于 8 位
        userAccount = "yupi";
        userPassword = "123";
        result = userService.userRegister(userAccount,userPassword,checkPassword, planetCode, avatarUrl, username);
        Assertions.assertEquals(-1, result);

        //  d. 账户不能重复
        userAccount = "123456";
        userPassword = "12345678";
        result = userService.userRegister(userAccount,userPassword,checkPassword, planetCode, avatarUrl, username);
        Assertions.assertEquals(-1, result);
        //  e. 账户不包含特殊字符
        userAccount = "yu pi";
        result = userService.userRegister(userAccount,userPassword,checkPassword, planetCode, avatarUrl, username);
        Assertions.assertEquals(-1, result);

        //  f. 校验密码和密码相同
        userAccount = "yupi";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount,userPassword,checkPassword, planetCode, avatarUrl, username);
        Assertions.assertTrue(result > 0);
    }

    @Test
    void testSearchUsersByTags() {
        List<String> tagNameList = Arrays.asList("java", "Python");
        List<User> userList = userService.searchUsersByTags(tagNameList);
        Assertions.assertNotNull(userList);
    }
}