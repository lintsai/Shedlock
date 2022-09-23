package com.example.redisclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class RedisClientApplication {

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private SampleCache sampleCache;

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder.build();
    }

    @Scheduled(fixedDelayString = "1", timeUnit = TimeUnit.SECONDS)
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[1] " + Thread.currentThread().getName() + " sampleSchedlock run..." + sampleCache.getSampleCacheData());
        sampleCache.setSampleCacheData(new Date() + ", save port: " + serverPort);
        System.out.println("[2] " + Thread.currentThread().getName() + " sampleSchedlock run..." + sampleCache.getSampleCacheData());
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisClientApplication.class, args);
    }

}
