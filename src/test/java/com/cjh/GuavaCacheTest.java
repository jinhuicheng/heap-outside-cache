package com.cjh;

import com.alibaba.fastjson.JSONObject;
import com.cjh.guava.GuavaCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Created by cjh
 * @date 2017/10/11 上午11:38
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-config.xml"})
public class GuavaCacheTest {

    @Resource
    private GuavaCache cache;

    @Test
    public void test(){

        /*第一次加载*/
        List<String> result=cache.getGuavaCache();

        System.out.println(JSONObject.toJSONString(result));


        /*第一次之后 直接去取本地缓存*/
        List<String> result1=cache.getGuavaCache();

        System.out.println(JSONObject.toJSONString(result1));

        List<String> result2=cache.getGuavaCache();

        System.out.println(JSONObject.toJSONString(result2));

        List<String> result3=cache.getGuavaCache();

        System.out.println(JSONObject.toJSONString(result3));
    }
}
