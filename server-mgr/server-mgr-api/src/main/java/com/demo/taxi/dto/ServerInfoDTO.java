package com.demo.taxi.dto;

import com.demo.taxi.def.ServerType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterReflectionForBinding({ServerInfoDTO.class})
public class ServerInfoDTO implements Serializable {

  private ServerType type;
  private String ip;
  private Integer port;

  private Integer maxRun;

}
