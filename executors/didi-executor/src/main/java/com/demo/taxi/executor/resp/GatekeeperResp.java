
package com.demo.taxi.executor.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 */
@Data
public class GatekeeperResp {

  @JsonProperty("errno")
  private Integer errno;
  @JsonProperty("error")
  private String error;
  @JsonProperty("requestid")
  private String requestId;
  @JsonProperty("traceid")
  private String traceId;
  @JsonProperty("time")
  private Date time;
  @JsonProperty("credential")
  private String credential;
  @JsonProperty("close_voice")
  private Integer closeCoice;
  @JsonProperty("roles")
  private List<Roles> roles;
  @JsonProperty("usertype")
  private Integer userType;
  @JsonProperty("sec_session_id")
  private String secSessionId;
  @JsonProperty("fallthrough_register")
  private Boolean fallThroughRegister;
  @JsonProperty("thirdparty_channels_available")
  private ThirdPartyChannelsAvailable thirdPartyChannelsAvailable;

  @Data
  public static class Roles {

    @JsonProperty("id")
    private int id;
    @JsonProperty("login_type")
    private int loginType;
    @JsonProperty("text")
    private String text;
  }

  @Data
  public static class ThirdPartyChannelsAvailable {

    @JsonProperty("whats_app")
    private Boolean whatsApp;

  }

}