package com.demo.taxi.controller;

import com.demo.taxi.executor.LoginExecutor;
import com.demo.taxi.http.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

  private final LoginExecutor loginExecutor;

  public TaskController(LoginExecutor loginExecutor) {
    this.loginExecutor = loginExecutor;
  }

  @PostMapping
  public Mono<String> startTask() {
    return Mono.empty();
  }

  public Mono<Result<Void>> stopTask() {
    return Mono.just(Result.success());
  }
}
