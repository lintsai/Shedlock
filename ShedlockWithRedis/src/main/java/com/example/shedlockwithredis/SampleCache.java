package com.example.shedlockwithredis;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "sampleCache")
public class SampleCache {
    private String sampleCacheData;

    @Cacheable(cacheNames = "sampleCacheData")
    public String getSampleCacheData(){
        return sampleCacheData;
    }

    @CacheEvict(cacheNames = "sampleCacheData", allEntries = true)
    public String setSampleCacheData(String sampleCacheData){
        this.sampleCacheData = sampleCacheData;
        return sampleCacheData;
    }
}
