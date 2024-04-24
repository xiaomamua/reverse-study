package com.demo.taxi.dto.exector;

import com.demo.taxi.def.TaskType;
import java.io.Serializable;
import lombok.Data;

@Data
public class TaskInfo implements Serializable {

  private String name;
  private TaskType type;
  private String token;
  private String userName;
}
