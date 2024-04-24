package com.demo.taxi.service.serverhealth;

import com.demo.taxi.dto.ServerInfoDTO;
import java.util.Optional;
import reactor.core.publisher.Flux;

public interface ServerHealthService {

  void saveAvailableServer(ServerInfoDTO dto);

  void removeAvailableServer(ServerInfoDTO dto);

  Flux<ServerInfoDTO> getAvailableServers();

  Optional<ServerInfoDTO> allotExecutorClient();

  void allotTaskFailed(ServerInfoDTO dto);
}
