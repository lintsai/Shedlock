package com.example.shedlockwithredis;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5s")
@EnableCaching
public class ShedlockWithRedisApplication {

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private TestCacheData testCacheData;

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisServerConfiguration redisServerConfiguration(){
        return new RedisServerConfiguration();
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder.build();
    }

    @Bean
    public LockProvider lockProvider() {
        return new RedisLockProvider(redisConnectionFactory);
    }

    @Scheduled(fixedDelayString = "${scheduledTask:6}", timeUnit = TimeUnit.SECONDS)
    @SchedulerLock(name = "scheduledTask", lockAtMostFor = "${scheduledTask:6}s", lockAtLeastFor = "${scheduledTask:6}s")
    public void scheduledTask() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[1] " + Thread.currentThread().getName() + " scheduledTask run..." + testCacheData.getTestCacheData());
        testCacheData.setTestCacheData(new Date() + ", save port: " + serverPort);
        System.out.println("[2] " + Thread.currentThread().getName() + " scheduledTask run..." + testCacheData.getTestCacheData());
    }

    public static void main(String[] args) {
        SpringApplication.run(ShedlockWithRedisApplication.class, args);
    }

}
