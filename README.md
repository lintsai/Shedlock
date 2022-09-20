# Shedlock
Shedlock Learn Project

- Reference:

  https://github.com/lukas-krecan/ShedLock

## ShedlockWithZookeeper

Use Shedlock with Embedded ZooKeeper implement the cluster scheduled

"spring.cloud.zookeeper.connect-string" in properties can implement cluster with ZooKeeper.

"server.port" and "zookeeper.port" should be different, then Actuator can use

- Reference:

  https://github.com/raonigabriel/spring-boot-embedded-zookeeper

  https://github.com/spring-cloud/spring-cloud-zookeeper

  https://github.com/lukas-krecan/ShedLock

## ZooKeeper
The Embedded ZooKeeper Server Module

- Reference:

  https://github.com/raonigabriel/spring-boot-embedded-zookeeper

## ShedlockWithApacheIgnite

Use Shedlock with Embedded Apache Ignite implement the cluster scheduled

"ignite.cluster.connect-string" in properties can implement cluster with Apache Ignite.

use ignite cache can sync data in memory with different service.

- Reference:

  https://ignite.apache.org/docs/latest/extensions-and-integrations/spring/spring-boot

  https://github.com/lukas-krecan/ShedLock

## ApacheIgnite
The Embedded Apache Ignite Server with cluster Module

- Reference:

  https://ignite.apache.org/docs/latest/extensions-and-integrations/spring/spring-boot

## ShedlockWithRedis

Use Shedlock with Embedded Redis implement the cluster scheduled

"spring.redis.sentinel.nodes" in properties can implement sentinel cluster with Redis.

use spring cache can sync data in memory with different service.

- Reference:

  https://github.com/kstyrc/embedded-redis
  
  https://github.com/caryyu/spring-embedded-redis-server

  https://www.baeldung.com/spring-boot-redis-cache

  https://github.com/lukas-krecan/ShedLock

## Redis
The Embedded Redis Server Module

- Reference:

  https://github.com/kstyrc/embedded-redis

  https://github.com/caryyu/spring-embedded-redis-server