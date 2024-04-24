package com.demo.taxi.serverhealth;

import com.demo.taxi.dto.ServerInfoDTO;
import com.demo.taxi.service.serverhealth.ServerHealthService;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ServerHealthServiceImpl implements ServerHealthService {

  /**
   * <Ip + Port>
   */
  private static final ConcurrentHashMap<ServerInfoDTO, ServerInfoDTO> AVAILABLE_SERVERS = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<ServerInfoDTO, AtomicInteger> SERVER_TASKS = new ConcurrentHashMap<>();

  @Override
  public void saveAvailableServer(ServerInfoDTO dto) {
    SERVER_TASKS.putIfAbsent(dto, new AtomicInteger(0));
  }

  @Override
  public void removeAvailableServer(ServerInfoDTO dto) {
    AVAILABLE_SERVERS.remove(dto);
  }

  @Override
  public Flux<ServerInfoDTO> getAvailableServers() {
    return Flux.fromStream(AVAILABLE_SERVERS.values().stream());
  }

  @Override
  public Optional<ServerInfoDTO> allotExecutorClient() {
    Optional<ServerInfoDTO> result = AVAILABLE_SERVERS.entrySet().stream()
        .map(entry -> new Object[]{entry,
            SERVER_TASKS.getOrDefault(entry.getKey(), new AtomicInteger(0))
        })
        .filter(s -> ((AtomicInteger) s[1]).get() < ((ServerInfoDTO) s[0]).getMaxRun())
        .sorted(Comparator.comparingInt(s -> ((AtomicInteger) s[1]).get()))
        .map(s -> (ServerInfoDTO) s[0])
        .findFirst();
    result.ifPresent(s -> {
      AtomicInteger taskCount = SERVER_TASKS.putIfAbsent(s, new AtomicInteger(1));
      if (taskCount != null) {
        taskCount.incrementAndGet();
      }
    });
    return result;
  }

  @Override
  public void allotTaskFailed(ServerInfoDTO dto) {
    AtomicInteger taskCount = SERVER_TASKS.putIfAbsent(s, new AtomicInteger(0));
    if (taskCount != null) {
      taskCount.decrementAndGet();
    }
  }

}
