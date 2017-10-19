package com.cjh;

import com.alibaba.fastjson.JSONObject;
import com.cjh.ehcache.EhCache3Simple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author Created by cjh
 * @date 2017/10/18 下午5:58
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-config.xml"})
public class EhCacheTest {
    private static Logger logger= LogManager.getLogger(GuavaCacheTest.class);

    @Resource
    private EhCache3Simple cache;

    @Test
    public void test(){

        /*第一次加载*/
        String result=cache.getEhCache();
        logger.info(JSONObject.toJSONString(result));

        /*第二次开始 直接去取本地缓存*/
        String result1=cache.getEhCache();
        logger.info(JSONObject.toJSONString(result1));

        /*删除本地缓存*/
        cache.refresh();

        /*重新拉取缓存到本地*/
        String result2=cache.getEhCache();
        logger.info(JSONObject.toJSONString(result2));

        String result3=cache.getEhCache();
        logger.info(JSONObject.toJSONString(result3));
    }
}
