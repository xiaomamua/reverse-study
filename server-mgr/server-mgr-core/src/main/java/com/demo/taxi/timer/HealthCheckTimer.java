package com.demo.taxi.timer;

import com.demo.taxi.def.ServerType;
import com.demo.taxi.dto.ServerInfoDTO;
import com.demo.taxi.serverproxy.TaxiClientProxy;
import com.demo.taxi.service.serverconfig.ServerConfigService;
import com.demo.taxi.service.serverhealth.ServerHealthService;
import io.netty.util.HashedWheelTimer;
import io.netty.util.TimerTask;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HealthCheckTimer {

  private final ServerConfigService serverConfigService;
  private final TaxiClientProxy proxy;

  private final ServerHealthService healthService;
  private static final HashedWheelTimer IDLE_CHECK_TIMER = new HashedWheelTimer(
      Thread.ofVirtual().name("health-check").factory(), 1000, TimeUnit.MILLISECONDS, 60);

  @Autowired
  public HealthCheckTimer(ServerConfigService serverConfigService, TaxiClientProxy proxy,
      ServerHealthService healthService) {
    this.serverConfigService = serverConfigService;
    this.proxy = proxy;
    this.healthService = healthService;
  }

  @PostConstruct
  public void init() {
    IDLE_CHECK_TIMER.newTimeout(checkClientHealthTask(), 1000, TimeUnit.MILLISECONDS);
    IDLE_CHECK_TIMER.start();
  }

  public TimerTask checkClientHealthTask() {
    return timeout -> {
      serverConfigService.getServerConfigs().filter(r -> r.getType() != ServerType.server)
          .subscribe((r) -> {
            timeout.timer().newTimeout(pingClientTask(r), 1, TimeUnit.MILLISECONDS);
          }, (err) -> {
            //
          }, () -> {
            timeout.timer().newTimeout(checkClientHealthTask(), 1000, TimeUnit.MILLISECONDS);
          });
    };
  }

  public TimerTask pingClientTask(ServerInfoDTO dto) {
    return timeout -> {
      long start = System.currentTimeMillis();
      proxy.ping(dto.getIp(), dto.getPort()).onErrorResume((err) -> {
        healthService.removeAvailableServer(dto);
        return Mono.empty();
      }).doOnSuccess((r) -> {
        healthService.saveAvailableServer(dto);
      }).subscribe();
    };
  }
}
