package com.demo.taxi.controller;

import com.demo.taxi.http.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/health")
public class HealthController {

  @GetMapping("/ping")
  public Mono<Result<Void>> ping() {
    return Mono.just(Result.success());
  }

}
