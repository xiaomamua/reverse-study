package com.demo.taxi.serverproxy;

import com.demo.taxi.dto.ServerInfoDTO;
import com.demo.taxi.service.serverhealth.ServerHealthService;
import com.demo.taxi.service.serverproxy.ServerProxyService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ServerProxyServiceImpl implements ServerProxyService {

  private final ServerHealthService serverHealthService;

  private final TaxiClientProxy clientProxy;

  @Autowired
  public ServerProxyServiceImpl(ServerHealthService serverHealthService,
      TaxiClientProxy clientProxy) {
    this.serverHealthService = serverHealthService;
    this.clientProxy = clientProxy;
  }

  @Override
  public Mono<String> startTask() {
    Optional<ServerInfoDTO> serverInfoOpt = serverHealthService.allotExecutorClient();
    if (serverInfoOpt.isEmpty()) {
      return Mono.error(new RuntimeException("allot executor client failed"));
    }
    ServerInfoDTO server = serverInfoOpt.get();
    return clientProxy.startTask(server.getIp(), server.getPort())
        .doOnError(s -> serverHealthService.allotTaskFailed(server));
  }
}
