package com.demo.taxi.executor;

import reactor.core.publisher.Mono;

public interface LoginExecutor {

  public Mono<String> sendCode(String phone);

  public Mono<String> login(String code);
}
