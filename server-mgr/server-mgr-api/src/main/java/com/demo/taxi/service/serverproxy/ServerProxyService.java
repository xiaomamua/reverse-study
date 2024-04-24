package com.demo.taxi.service.serverproxy;

import reactor.core.publisher.Mono;

public interface ServerProxyService {

  public Mono<String> startTask();
}