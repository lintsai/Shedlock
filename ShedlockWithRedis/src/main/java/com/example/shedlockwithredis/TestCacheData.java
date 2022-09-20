package com.example.shedlockwithredis;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "testCache")
public class TestCacheData {
    private String testCacheData;

    @Cacheable(cacheNames = "testCacheData")
    public String getTestCacheData(){
        return testCacheData;
    }

    @CacheEvict(cacheNames = "testCacheData", allEntries = true)
    public String setTestCacheData(String testCacheData){
        this.testCacheData = testCacheData;
        return testCacheData;
    }
}
