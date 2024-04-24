package com.demo.taxi.controller;

import com.demo.taxi.executor.LoginExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/login")
public class LoginController {

  private final LoginExecutor loginExecutor;

  public LoginController(LoginExecutor loginExecutor) {
    this.loginExecutor = loginExecutor;
  }

  @PostMapping("/sendCode")
  public Mono<String> sendCode() {
    return loginExecutor.sendCode("");
  }

  @PostMapping
  public Mono<String> login(@RequestBody String code) {
    return loginExecutor.login(code);
  }
}
