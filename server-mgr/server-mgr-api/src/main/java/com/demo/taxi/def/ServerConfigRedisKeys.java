package com.demo.taxi.def;

public interface ServerConfigRedisKeys {

  String KEY = "server-config";

  String TASK_ALLOC_INFO_KEY = "task:";

  default String buildTaskAllocInfoKey(String ip) {
    return TASK_ALLOC_INFO_KEY + ip;
  }


}
