package com.example.scheduledcluster;

import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Role(2)
public class CustomEmbeddedZooKeeperConfig implements ImportAware {

    @Value("${zookeeper.port:2181}")
    private int port;

    @Nullable
    private AnnotationAttributes enableEmbeddedZooKeeper;

    public CustomEmbeddedZooKeeperConfig() {
    }

    @Bean(
            destroyMethod = "shutdown"
    )
    @Role(2)
    public ZooKeeperServer zookeeperServer() {
        try {
            if (this.enableEmbeddedZooKeeper == null) {
                throw new InvalidPropertyException(ZooKeeperServer.class, "enableEmbeddedZooKeeper", "Property cannot be null");
            } else {
                String zooDir = this.enableEmbeddedZooKeeper.getString("zooDir");
                int port = this.port;
                int maxConnections = (Integer)this.enableEmbeddedZooKeeper.getNumber("maxConnections");
                int tickTime = (Integer)this.enableEmbeddedZooKeeper.getNumber("tickTime");
                Path zooPath = null;
                if (!StringUtils.hasLength(zooDir)) {
                    zooPath = Files.createTempDirectory("zookeeper");
                    zooPath.toFile().deleteOnExit();
                } else {
                    zooPath = Paths.get(zooDir);
                }

                ZooKeeperServer server = new ZooKeeperServer(zooPath.toFile(), zooPath.toFile(), tickTime);
                ServerCnxnFactory factory = new NIOServerCnxnFactory();
                System.setProperty("zookeeper.maxCnxns", Integer.toString(maxConnections));
                factory.configure(new InetSocketAddress(port), maxConnections);
                factory.startup(server);
                return server;
            }
        } catch (Exception var8) {
            throw new BeanInstantiationException(CustomEmbeddedZooKeeperConfig.class, "Failed to start Zookeeper", var8);
        }
    }

    public void setImportMetadata(AnnotationMetadata importMetadata) {
        if (importMetadata == null) {
            throw new IllegalArgumentException("importMetadata cannot be null");
        } else {
            this.enableEmbeddedZooKeeper = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(CustomEnableEmbeddedZooKeeper.class.getName()));
            if (this.enableEmbeddedZooKeeper == null) {
                throw new IllegalArgumentException("@EnableEmbeddedZooKeeper is not present on importing class " + importMetadata.getClassName());
            }
        }
    }
}