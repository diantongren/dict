package com.diantongren;

import com.diantongren.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = {SpringTestDemoApplication.class})
public class RedisTest {
    @Resource
    RedisUtil redisUtil;

    @Test
    public void testSet() {
        String value = "test set";
        redisUtil.set("1010", value);
    }

    @Test
    public void testSetMap() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "唱");
        map.put("2", "跳");
        map.put("3", "rap");
        map.put("4", "篮球");
        redisUtil.add("hobby", map);
    }

    @Test
    public void testGetMap() {
        Map<String, String> hobby = redisUtil.getHashEntries("g");
        System.out.println(hobby);
    }

}
