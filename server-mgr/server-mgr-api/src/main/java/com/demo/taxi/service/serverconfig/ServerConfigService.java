package com.demo.taxi.service.serverconfig;

import com.demo.taxi.dto.ServerInfoDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServerConfigService {

  Mono<Boolean> register(ServerInfoDTO dto);

  Flux<ServerInfoDTO> getServerConfigs();
}
