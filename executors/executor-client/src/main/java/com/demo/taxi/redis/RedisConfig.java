package com.demo.taxi.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisSerializationContext<String, Object> redisSerializationContext() {
    RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder = RedisSerializationContext.newSerializationContext();
    builder.key(StringRedisSerializer.UTF_8);
    builder.value(RedisSerializer.json());
    builder.hashKey(StringRedisSerializer.UTF_8);
    builder.hashValue(StringRedisSerializer.UTF_8);
    return builder.build();
  }

  @Bean
  public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(
      ReactiveRedisConnectionFactory connectionFactory) {
    RedisSerializationContext<String, Object> serializationContext = redisSerializationContext();
    return new ReactiveRedisTemplate<>(
        connectionFactory, serializationContext);
  }
}
