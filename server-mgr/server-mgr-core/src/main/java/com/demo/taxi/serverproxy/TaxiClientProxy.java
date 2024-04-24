package com.demo.taxi.serverproxy;

import com.demo.taxi.http.Result;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

@Component
public class TaxiClientProxy {

  private final WebClient.Builder builder;

  public TaxiClientProxy(Builder builder) {
    this.builder = builder;
  }

  public Mono<Result<Void>> ping(@NotBlank String ip, @NotNull @Min(1) Integer port) {
    return builder.baseUrl(STR. "http://\{ ip }:\{ port }/taxi-client" ).build().get()
        .uri("/health/ping")
        .retrieve()
        .onRawStatus(code -> code != 200, resp -> Mono.error(new RuntimeException(
            STR. "心跳接口检测失败:ip=\{ ip },port=\{ port },respHttpStatus=\{ resp.statusCode()
                .value() }" ))).bodyToMono(new ParameterizedTypeReference<Result<Void>>() {
        }).timeout(Duration.ofMillis(3000));
  }

  public Mono<String> startTask(@NotBlank String ip, @NotNull @Min(1) Integer port) {
    return Mono.just("traceId");
  }
}
