package com.demo.taxi.service.serverconfig;

import com.demo.taxi.def.ServerConfigRedisKeys;
import com.demo.taxi.dto.ServerInfoDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnBean({ReactiveRedisTemplate.class})
public class RedisServerConfigServiceImpl implements ServerConfigService {

  private final ReactiveRedisTemplate<String, Object> redis;

  public RedisServerConfigServiceImpl(ReactiveRedisTemplate<String, Object> redis) {
    this.redis = redis;
  }

  @Override
  public Mono<Boolean> register(ServerInfoDTO dto) {
    return redis.opsForZSet().add(ServerConfigRedisKeys.KEY, dto, 1);
  }

  @Override
  public Flux<ServerInfoDTO> getServerConfigs() {
    return redis.opsForZSet().scan(ServerConfigRedisKeys.KEY)
        .mapNotNull(TypedTuple::getValue)
        .cast(ServerInfoDTO.class);
  }
}
