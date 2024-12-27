package com.yuki.webfluxorderserver.config;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@ConfigurationProperties(prefix = "polar")
@ConditionalOnProperty(value = "polar.catalog-service-uri")
public record ClientProperties(
    @NotNull
    URI catalogServiceUri
) {
    @PostConstruct
    public void init(){
        log.info("ClientProperties 등록 -> URI : {}", this.catalogServiceUri);
    }
}
