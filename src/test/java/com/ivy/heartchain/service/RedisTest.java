package com.ivy.heartchain.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Description:    Redis测试
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void test() {
        //ValueOperations valueOperations = redisTemplate.opsForValue();
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.set("askdj", 1, "iskdjhf");
        // 增
//        valueOperations.set("sanshuiString", "fish");
//        valueOperations.set("sanshuiInt", 1);
//        valueOperations.set("sanshuiDouble", 2.0);
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("sanshui");
//        valueOperations.set("sanshuiUser", user);
//
//        // 查
//        Object sanshui = valueOperations.get("sanshuiString");
//        Assertions.assertTrue("fish".equals((String) sanshui));
//        sanshui = valueOperations.get("sanshuiInt");
//        Assertions.assertTrue(1 == (Integer) sanshui);
//        sanshui = valueOperations.get("sanshuiDouble");
//        Assertions.assertTrue(2.0 == (Double) sanshui);
//        System.out.println(valueOperations.get("sanshuiUser"));
//        valueOperations.set("sanshuiString", "fish");

        //删
//        redisTemplate.delete("sanshuiString");
    }

}
