package com.cjh.ehcache;


import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by cjh
 * @date 2017/10/11 下午3:34
 * @description
 */
public class EhCache3Simple {
    private static Logger LOGGER= LogManager.getLogger(EhCache3Simple.class);

    private static Cache<String,String> ehCache;


    @PostConstruct
    private void init(){

        try {
            //缓存资源池
            ResourcePoolsBuilder heap_resourcePool = ResourcePoolsBuilder.heap(10);

            ResourcePoolsBuilder on_off_heap_resourcePool = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, MemoryUnit.KB).offheap(10, MemoryUnit.MB); //heap + off heap

            ResourcePoolsBuilder off_heap_resourcePool = ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(10, MemoryUnit.MB); //off heap

            ResourcePoolsBuilder disk_resourcePool = ResourcePoolsBuilder.newResourcePoolsBuilder().disk(100, MemoryUnit.MB); //disk

            //缓存管理器配置
            CacheConfiguration config = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, off_heap_resourcePool)
                    .withSizeOfMaxObjectGraph(1000)
                    .withSizeOfMaxObjectSize(10, MemoryUnit.MB)
                    .withExpiry(Expirations.timeToLiveExpiration(Duration.of(20, TimeUnit.SECONDS)))
                    .build();

            //缓存管理器
            CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                    .withCache("getEhCache", config)
                    .build();

            cacheManager.init();


            //缓存实例
            ehCache = cacheManager.getCache("getEhCache", String.class, String.class);

            ehCache.put("ehcache", getCacheToEh());

        }catch (Exception e){
            LOGGER.error("ehcache 初始化出错：{}",e);
        }
    }

    public String getEhCache(){

        String result=ehCache.get("ehcache");

        LOGGER.info("本地获取数据："+JSONObject.toJSONString(result));

        if (result==null){
            //TODO 缓存失效 重载机制
        }

        return  result;
    }


    public  void refresh(){
        ehCache.replace("ehcache",getCacheToEh());
    }


    private String getCacheToEh(){
        List<String> list = new ArrayList<String>();
        list.add("ehcache_1");
        list.add("ehcache_2");
        list.add("ehcache_3");
        LOGGER.info("模拟数据生成  ： "+ JSONObject.toJSONString(list));
        return JSONObject.toJSONString(list);
    }

}
