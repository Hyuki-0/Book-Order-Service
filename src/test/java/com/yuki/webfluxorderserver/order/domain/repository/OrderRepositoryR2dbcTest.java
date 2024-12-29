package com.yuki.webfluxorderserver.order.domain.repository;

import com.yuki.webfluxorderserver.config.DatabaseConfig;
import com.yuki.webfluxorderserver.order.domain.entity.OrderStatus;
import com.yuki.webfluxorderserver.order.domain.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Testcontainers
@Import(DatabaseConfig.class)
class OrderRepositoryR2dbcTest {

  @Container
  static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.4"));

  @Autowired
  private OrderRepository orderRepository;

  @DynamicPropertySource
  static void postgreSQLProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.r2dbc.url", OrderRepositoryR2dbcTest::r2dbcUrl);
    registry.add("spring.r2dbc.username", postgreSQL::getUsername);
    registry.add("spring.r2dbc.password", postgreSQL::getPassword);
    registry.add("spring.flyway.url", postgreSQL::getJdbcUrl);
    registry.add("spring.flyway.user", postgreSQL::getUsername);
    registry.add("spring.flyway.password", postgreSQL::getPassword);
  }

  private static String r2dbcUrl() {
    return String.format("r2dbc:postgresql://%s:%s/%s",
        postgreSQL.getHost(),
        postgreSQL.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
        postgreSQL.getDatabaseName()
    );
  }

  @Test
  void createRejectedOrder() {
    var rejectedOrder = OrderService.buildRejectedOrder("1234567890", 3);

    StepVerifier
        .create(orderRepository.save(rejectedOrder))
        .expectNextMatches(
            order -> order.status().equals(OrderStatus.REJECTED)
        ).verifyComplete();
  }

}