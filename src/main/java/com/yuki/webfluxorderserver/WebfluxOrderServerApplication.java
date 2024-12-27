package com.yuki.webfluxorderserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WebfluxOrderServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebfluxOrderServerApplication.class, args);
  }

}
