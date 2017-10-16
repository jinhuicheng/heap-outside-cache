package com.cjh.guava;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by cjh
 * @date 2017/10/11 上午10:06
 * @description guava 堆外缓存
 */
public class GuavaCache {

    private static Logger LOGGER= LogManager.getLogger(GuavaCache.class);

    private static final Method GUAVA_CACHE_METHOD;

    /*初始化 method 参数*/
    static {
        try {

            GUAVA_CACHE_METHOD = GuavaCache.class.getDeclaredMethod("getGuavaCacheFromInterFace");

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static LoadingCache<Params, Object> cache;

    /**
     * 初始化guava 缓存
     */
    @PostConstruct
    private void initCache() {
        TimeUnit unit=TimeUnit.HOURS;
        cache= CacheBuilder.newBuilder()
                .expireAfterWrite(8,unit)//设置缓存过期时间 定时回收
                .maximumSize(10)         //设置最大数据    基于容量回收
                .removalListener(new RemovalListener<Object, Object>() {
                    public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                        System.out.println("guava cache removed....");
                    }
                })
                .build(new CacheLoader<Params, Object>() {
                    @Override
                    public Object load(Params params) throws Exception {
                        Method method=params.method;
                        method.setAccessible(true); //禁用安全检查 java语言访问检查  ，以便提高效率
                        Object result=null;
                        result=method.invoke(GuavaCache.this); //反射调用本类中所有的method方法对应的方法

                        LOGGER.info("缓存数据存入。。。。。");

                        return result;
                    }
                });
    }

    /**
     * 堆外提供的刷新缓存的方法
     */
    public static void refresh() {
        Params params = new Params(GUAVA_CACHE_METHOD);
        cache.refresh(params);
    }

    /**
     * 对系统提供的使用缓存内数据的方法
     *
     * @return 缓存中所需数据
     */
    public List<String> getGuavaCache() {
        Params params = new Params(GUAVA_CACHE_METHOD);
        try {
            List<String> result = (List<String>) cache.get(params);
            LOGGER.info("本地获取数据："+JSONObject.toJSONString(result));
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 缓存中数据来源方法  模拟从接口获取
     *
     * @return 要存入缓存中的数据
     */
    private List<String> getGuavaCacheFromInterFace() {

        List<String> list = new ArrayList<String>();
        list.add("guava_1");
        list.add("guava_2");
        list.add("guava_3");
        LOGGER.info("模拟数据生成  ： "+ JSONObject.toJSONString(list));
        return list;
    }


    /**
     * 内部类  定义缓存交互参数
     */
    private static class Params {
        private Method method;

        public Params(Method method) {
            this.method = method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Params params = (Params) o;

            return method.equals(params.method);
        }

        @Override
        public int hashCode() {
//            return method.hashCode();
            int result = 17;
            final int prime = 31;
            result = prime * result + ((method == null) ? 0 : method.hashCode());
            return result;
        }
    }
}
