# Shedlock
Shedlock Learn Project

- Reference:

  https://github.com/lukas-krecan/ShedLock

## ShedlockWithZookeeper

Use Shedlock with Embedded ZooKeeper implement the cluster scheduled.

"spring.cloud.zookeeper.connect-string" in properties can implement cluster with ZooKeeper.

"server.port" and "zookeeper.port" should be different, then Actuator can use.

- Reference:

  https://github.com/raonigabriel/spring-boot-embedded-zookeeper

  https://github.com/spring-cloud/spring-cloud-zookeeper

  https://github.com/lukas-krecan/ShedLock

## ZooKeeper
The Embedded ZooKeeper Server Module

- Reference:

  https://github.com/raonigabriel/spring-boot-embedded-zookeeper

## ShedlockWithApacheIgnite

Use Shedlock with Embedded Apache Ignite implement the cluster scheduled.

"ignite.cluster.connect-string" in properties can implement cluster with Apache Ignite.

Use spring cache with h2 1.4.196 version can sync data in memory with different service.

Note: Can't use new h2 version with ignite.

Use ignite cache can sync data in memory with different service.

- Reference:

  https://ignite.apache.org/docs/latest/extensions-and-integrations/spring/spring-boot

  https://ignite.apache.org/docs/latest/extensions-and-integrations/spring/spring-caching

  https://github.com/lukas-krecan/ShedLock

## ApacheIgnite
The Embedded Apache Ignite Server with cluster Module

- Reference:

  https://ignite.apache.org/docs/latest/extensions-and-integrations/spring/spring-boot

## ShedlockWithRedis

Use Shedlock with Embedded Redis implement the cluster scheduled.

Use sentinel cluster mode need set "spring.redis.sentinel=true" and "spring.redis.sentinel.*" properties.

"spring.redis.sentinel.nodes" in properties can implement sentinel cluster with Redis.

"server.port" and "spring.redis.port" should be different, then Actuator can use.

Use spring cache can sync data in memory with different service.

Redis Version "Redis for Windows 5.0.14.1"

- Reference:

  https://github.com/signalapp/embedded-redis
  
  https://github.com/caryyu/spring-embedded-redis-server

  https://github.com/tporadowski/redis/releases

  https://www.baeldung.com/spring-boot-redis-cache

  https://github.com/lukas-krecan/ShedLock

## Redis

The Embedded Redis Server Module

Redis Version "Redis for Windows 5.0.14.1"

- Reference:

  https://github.com/signalapp/embedded-redis

  https://github.com/caryyu/spring-embedded-redis-server

  https://github.com/tporadowski/redis/releases

## RedisClient
The Redis Client Module with Spring Cache Sample