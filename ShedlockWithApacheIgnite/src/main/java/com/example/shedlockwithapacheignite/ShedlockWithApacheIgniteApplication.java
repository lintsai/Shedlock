package com.example.shedlockwithapacheignite;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.ignite.IgniteLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.*;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.springframework.boot.autoconfigure.IgniteConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5s")
@EnableCaching
public class ShedlockWithApacheIgniteApplication {

    @Value("${ignite.communicationSpi.localPort:8080}")
    private int igniteLocalPort;

    @Value("${ignite.cluster.connect-string:}")
    private String clusterConnectString;

    @Resource
    private TestCacheData testCacheData;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public IgniteConfiguration igniteConfiguration() {
        // If you provide a whole ClientConfiguration bean then configuration properties will not be used.
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName("my-ignite");
        return cfg;
    }

    @Bean
    public IgniteConfigurer nodeConfigurer() {
        return cfg -> {
            //Setting some property.
            if(!clusterConnectString.isEmpty()){
                TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
                TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
                ipFinder.setAddresses(Arrays.asList(clusterConnectString.split(",")));
                discoverySpi.setIpFinder(ipFinder);
                cfg.setDiscoverySpi(discoverySpi);
            }
            TcpCommunicationSpi communicationSpi = new TcpCommunicationSpi();
            communicationSpi.setLocalPort(igniteLocalPort);
            cfg.setCommunicationSpi(communicationSpi);
            //Other will come from `application.yml`
            cfg.setIgniteInstanceName("my-ignite");
        };
    }

    @Bean
    public SpringCacheManager cacheManager() {
        SpringCacheManager mgr = new SpringCacheManager();
        mgr.setIgniteInstanceName("my-ignite");
        // Other required configuration parameters.
        return mgr;
    }

    @Bean
    public LockProvider lockProvider(Ignite ignite) {
        return new IgniteLockProvider(ignite);
    }

    @Scheduled(fixedDelayString = "${scheduledTask:6}", timeUnit = TimeUnit.SECONDS)
    @SchedulerLock(name = "scheduledTask", lockAtMostFor = "${scheduledTask:6}s", lockAtLeastFor = "${scheduledTask:6}s")
    public void scheduledTask() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // use Spring Cache
        System.out.println("[1] " + Thread.currentThread().getName() + " scheduledTask run...spring cache: " + testCacheData.getTestCacheData());
        testCacheData.setTestCacheData(new Date() + ", save port: " + igniteLocalPort);
        System.out.println("[2] " + Thread.currentThread().getName() + " scheduledTask run...spring cache: " + testCacheData.getTestCacheData());

        // use Ignite Cache
        Ignite ignite = applicationContext.getBean("ignite", Ignite.class);
        // get the cache named "myCache" and create a near cache for it
        IgniteCache<Long, String> cache = ignite.getOrCreateCache("myCache");
        if(cache.containsKey(1L)){
            System.out.println("[1] " + Thread.currentThread().getName() + " scheduledTask run...ignite cache: " + cache.get(1L));
        }else{
            System.out.println("[1] " + Thread.currentThread().getName() + " scheduledTask run...ignite cache: null");
        }
        cache.put(1L, new Date() + ", save port: " + igniteLocalPort);
        System.out.println("[2] " + Thread.currentThread().getName() + " scheduledTask run...ignite cache: " + cache.get(1L));
    }

    public static void main(String[] args) {
        SpringApplication.run(ShedlockWithApacheIgniteApplication.class, args);
    }

}
