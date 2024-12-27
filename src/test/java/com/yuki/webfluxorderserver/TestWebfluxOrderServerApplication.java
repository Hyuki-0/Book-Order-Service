package com.yuki.webfluxorderserver;

import org.springframework.boot.SpringApplication;

public class TestWebfluxOrderServerApplication {

  public static void main(String[] args) {
    SpringApplication.from(WebfluxOrderServerApplication::main).with(TestcontainersConfiguration.class).run(args);
  }

}
