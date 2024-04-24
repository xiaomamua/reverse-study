package com.demo.taxi.boot;

import com.demo.taxi.def.ServerType;
import com.demo.taxi.dto.ServerInfoDTO;
import com.demo.taxi.service.serverconfig.ServerConfigService;
import com.demo.taxi.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationBoot implements ApplicationRunner {

  private final ServerProperties env;

  private final ServerConfigService serverConfigService;

  public ApplicationBoot(ServerProperties env, ServerConfigService serverConfigService) {
    this.env = env;
    this.serverConfigService = serverConfigService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Integer port = env.getPort();
    if (port == null) {
      port = 8080;
    }
    String ip = IPUtils.getIpAddress();
    ServerInfoDTO dto = new ServerInfoDTO(ServerType.executor, ip, port);
    this.serverConfigService.register(dto).block();
  }

}
