package com.example.shedlockwithzookeeper;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({CustomEmbeddedZooKeeperConfig.class})
public @interface CustomEnableEmbeddedZooKeeper {
    int port() default 2181;

    int tickTime() default 500;

    int maxConnections() default 1024;

    String zooDir() default "";
}
