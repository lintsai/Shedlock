package com.example.redis;

import com.github.caryyu.spring.embedded.redisserver.RedisServerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisApplication {

    @Bean
    public RedisServerConfiguration redisServerConfiguration(){
        return new RedisServerConfiguration();
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
