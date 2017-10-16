package com.cjh;

import com.alibaba.fastjson.JSONObject;
import com.cjh.guava.GuavaCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static Logger logger= LogManager.getLogger(GuavaCacheTest.class);

    @Resource
    private GuavaCache cache;

    @Test
    public void test(){

        /*第一次加载*/
        List<String> result=cache.getGuavaCache();
        logger.info(JSONObject.toJSONString(result));

        /*第二次开始 直接去取本地缓存*/
        List<String> result1=cache.getGuavaCache();
        logger.info(JSONObject.toJSONString(result1));

        /*删除本地缓存*/
        GuavaCache.refresh();

        /*重新拉取缓存到本地*/
        List<String> result2=cache.getGuavaCache();
        logger.info(JSONObject.toJSONString(result2));

        List<String> result3=cache.getGuavaCache();
        logger.info(JSONObject.toJSONString(result3));
    }
}
