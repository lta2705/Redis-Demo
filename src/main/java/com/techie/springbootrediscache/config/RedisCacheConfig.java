package com.techie.springbootrediscache.config;

import com.techie.springbootrediscache.dto.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisTemplate<String, ProductDto> redisTemplate(RedisConnectionFactory redisConnectionFactory) {;
        RedisTemplate<String, ProductDto> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Use Jackson2JsonRedisSerializer to serialize and deserialize ProductDto
        Jackson2JsonRedisSerializer<ProductDto> serializer = new Jackson2JsonRedisSerializer<>(ProductDto.class);
        template.setDefaultSerializer(serializer);

        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new Jackson2JsonRedisSerializer<>(ProductDto.class)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }
}
