package com.example.shedlockwithapacheignite;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.zookeeper.curator.ZookeeperCuratorLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5s")
@CustomEnableEmbeddedZooKeeper
public class ShedlockWithZookeeperApplication {

    @Bean
    public LockProvider lockProvider(org.apache.curator.framework.CuratorFramework client) {
        return new ZookeeperCuratorLockProvider(client);
    }

    @Scheduled(fixedDelayString = "${scheduledTask:6}", timeUnit = TimeUnit.SECONDS)
    @SchedulerLock(name = "scheduledTask", lockAtMostFor = "${scheduledTask:6}s", lockAtLeastFor = "${scheduledTask:6}s")
    public void scheduledTask() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " scheduledTask run..." + new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(ShedlockWithZookeeperApplication.class, args);
    }

}
