package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: chatroom
 * @description: redis配置
 * @author: louweiwei
 * @create: 2020-04-21 16:50
 **/
@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisConfiguration {

    List<String> nodes;

    /**cluster*/
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisClusterConfiguration config = new RedisClusterConfiguration(nodes);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
        return jedisConnectionFactory;
    }

    /**As our repository extends CrudRepository, it provides basic CRUD and finder operations.
     * The thing we need in between to glue things together is the corresponding Spring configuration,
     * shown in the following example*/
    @Bean
    public RedisTemplate<?, ?> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<byte[], byte[]>();
        template.setConnectionFactory(jedisConnectionFactory);
        return template;
    }

}
