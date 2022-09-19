package com.example.apacheignite;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.springframework.boot.autoconfigure.IgniteConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class ApacheIgniteApplication {

    @Value("${ignite.communicationSpi.localPort:8080}")
    private int igniteLocalPort;

    @Value("${ignite.cluster.connect-string:}")
    private String clusterConnectString;

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

    public static void main(String[] args) {
        SpringApplication.run(ApacheIgniteApplication.class, args);
    }

}
