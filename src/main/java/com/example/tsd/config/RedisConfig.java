package com.example.tsd.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 使用 StringRedisSerializer 作为键和哈希键的序列化器，使用 Jackson2JsonRedisSerializer 作为值和哈希值的序列化器。
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 创建一个StringRedisSerializer对象，用于将键和哈希键序列化为字符串形式。
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 存储在Redis中的键和哈希键将会以字符串形式进行序列化和反序列化。
        redisTemplate.setKeySerializer(stringRedisSerializer); // key的序列化类型
        redisTemplate.setHashKeySerializer(stringRedisSerializer); // hash key的序列化类型

        // 用于将值和哈希值序列化为JSON格式。
        ObjectMapper objectMapper = new ObjectMapper();
        // setVisibility方法用于设置字段的可见性，设置ObjectMapper以可见方式处理所有字段。
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // enableDefaultTyping方法用于启用默认的类型信息，以便在序列化和反序列化过程中保留类型信息。
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        // 用于将值和哈希值序列化为JSON格式。要序列化的值的类型是任意对象
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(objectMapper,Object.class);


        // 我们将这个Jackson2JsonRedisSerializer序列化器设置给RedisTemplate的valueSerializer属性和hashValueSerializer属性。
        // 这样，存储在Redis中的值和哈希值将会以JSON格式进行序列化和反序列化。
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        // 调用afterPropertiesSet方法，以确保RedisTemplate的配置已经完成。
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
