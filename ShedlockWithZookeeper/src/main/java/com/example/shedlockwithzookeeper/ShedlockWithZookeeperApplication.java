package com.example.shedlockwithzookeeper;

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

@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5s")
@CustomEnableEmbeddedZooKeeper
public class ShedlockWithZookeeperApplication {

    @Bean
    public LockProvider lockProvider(org.apache.curator.framework.CuratorFramework client) {
        return new ZookeeperCuratorLockProvider(client);
    }

    @Scheduled(cron = "0/1 * * * * ?")
    @SchedulerLock(name = "scheduledTask", lockAtMostFor = "1s", lockAtLeastFor = "1s")
    public void scheduledTask() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " scheduledTask run..." + new Date());
    }

    @Scheduled(cron = "0/1 * * * * ?")
    @SchedulerLock(name = "scheduledTask", lockAtMostFor = "1s", lockAtLeastFor = "1s")
    public void scheduledTask2() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " scheduledTask2 run..." + new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(ShedlockWithZookeeperApplication.class, args);
    }

}
