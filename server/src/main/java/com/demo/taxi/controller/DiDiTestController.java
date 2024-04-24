package com.demo.taxi.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/didi")
public class DiDiTestController {


  @GetMapping("/sendCode")
  public Mono<String> sendCode() {
    return WebClient.builder()
        .baseUrl("http://localhost:8081")
        .build()
        .post()
        .uri("/taxi-client/login/sendCode")
        .contentType(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(String.class);
  }

  @GetMapping("/login/{code}")
  public Mono<String> login(@PathVariable String code) {
    return WebClient.builder()
        .baseUrl("http://localhost:8081")
        .build()
        .post()
        .uri("/taxi-client/login")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(code)
        .retrieve()
        .bodyToMono(String.class);
  }
}
