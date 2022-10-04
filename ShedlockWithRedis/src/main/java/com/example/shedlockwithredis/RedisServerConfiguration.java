package com.example.shedlockwithredis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import redis.embedded.*;
import redis.embedded.util.Architecture;
import redis.embedded.util.OS;

@Component
public class RedisServerConfiguration implements DisposableBean, EnvironmentAware, InitializingBean {
    private Log log = LogFactory.getLog(this.getClass());
    private RedisServer redisServer;
    private RedisSentinel redisSentinel;
    private Environment environment;

    public RedisServerConfiguration() {
    }

    public int getPort() {
        int v = (Integer)this.environment.getProperty("spring.redis.port", Integer.class, 0);
        v = v == 0 ? (Integer)this.environment.getProperty("global.redis.port", Integer.class, 6379) : v;
        return v;
    }

    public int getSentinelPort() {
        int v = (Integer)this.environment.getProperty("spring.redis.sentinel.port", Integer.class, 0);
        v = v == 0 ? (Integer)this.environment.getProperty("global.redis.sentinel.port", Integer.class, 26379) : v;
        return v;
    }

    private boolean isEmbedded() {
        Boolean v = (Boolean)this.environment.getProperty("spring.redis.embedded", Boolean.class, null);
        v = v == null ? (Boolean)this.environment.getProperty("global.redis.embedded", Boolean.class, false) : v;
        return v;
    }

    private boolean isSentinel() {
        Boolean v = (Boolean)this.environment.getProperty("spring.redis.sentinel", Boolean.class, null);
        v = v == null ? (Boolean)this.environment.getProperty("global.redis.sentinel", Boolean.class, false) : v;
        return v;
    }

    public void destroy() throws Exception {
        if (this.redisSentinel != null) {
            this.redisSentinel.stop();
            this.redisSentinel = null;
        }
        if (this.redisServer != null) {
            this.redisServer.stop();
            this.redisServer = null;
        }
    }

    public void afterPropertiesSet() throws Exception {
        int port = this.getPort();
        RedisExecProvider customProvider = RedisExecProvider.defaultProvider()
                .override(OS.WINDOWS, "redis-server.exe");
        if (this.isEmbedded()) {
            RedisServerBuilder redisServerBuilder =
                    RedisServer.builder()
                            .redisExecProvider(customProvider)
                            .port(port);
            this.redisServer = redisServerBuilder.build();
            this.redisServer.start();
            if (this.log.isInfoEnabled()) {
                this.log.info("Starting local embedded redis server successfully, port is " + port);
            }
        }
        if (this.isSentinel()) {
            int sentinelPort = this.getSentinelPort();
            RedisSentinelBuilder redisSentinelServerBuilder =
                    RedisSentinel.builder()
                            .redisExecProvider(customProvider)
                            .masterPort(port)
                            .port(sentinelPort);
            this.redisSentinel = redisSentinelServerBuilder.build();
            this.redisSentinel.start();
            if (this.log.isInfoEnabled()) {
                this.log.info("Starting local embedded redis sentinal server successfully, masterPort is " + port + ", sentinelPort is " + sentinelPort);
            }
        }
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
