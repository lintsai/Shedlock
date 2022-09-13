package com.example.zookeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@CustomEnableEmbeddedZooKeeper
@SpringBootApplication
public class ZooKeeperApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZooKeeperApplication.class, args);
    }

}
