package com.demo.taxi.controller;

import com.demo.taxi.dto.ServerInfoDTO;
import com.demo.taxi.service.serverconfig.ServerConfigService;
import com.demo.taxi.service.serverhealth.ServerHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/hello")
public class HelloController {

  private final WebClient.Builder clientBuilder;

  private final ServerConfigService serverConfigService;

  private final ServerHealthService serverHealthService;

  @Autowired
  public HelloController(Builder clientBuilder, ServerConfigService serverConfigService,
      ServerHealthService serverHealthService) {
    this.clientBuilder = clientBuilder;
    this.serverConfigService = serverConfigService;
    this.serverHealthService = serverHealthService;
  }

  @GetMapping
  public Flux<String> hello() {
    return clientBuilder.baseUrl("http://localhost:8081/taxi-client/hello").build().post()
        .retrieve().bodyToFlux(String.class);
  }

  @GetMapping("/hello1")
  public Flux<ServerInfoDTO> hello1() {
    return this.serverHealthService.getAvailableServers();
  }

}
