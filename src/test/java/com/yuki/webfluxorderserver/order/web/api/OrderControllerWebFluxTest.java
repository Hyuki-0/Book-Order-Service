package com.yuki.webfluxorderserver.order.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.yuki.webfluxorderserver.order.domain.entity.Order;
import com.yuki.webfluxorderserver.order.domain.entity.OrderStatus;
import com.yuki.webfluxorderserver.order.domain.service.OrderService;
import com.yuki.webfluxorderserver.order.web.dto.request.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(OrderController.class)
class OrderControllerWebFluxTest {

  @Autowired
  private WebTestClient webClient;

  @MockitoBean
  private OrderService orderService;

  @Test
  void whenBookNotAvailableThenRejectOrder() {
    var orderRequest = new OrderRequest("1234567890", 3);
    var expectedOrder = OrderService.buildRejectedOrder(
        orderRequest.isbn(), orderRequest.quantity()
    );

    given(
        orderService.submitOrder(
            orderRequest.isbn(), orderRequest.quantity()
        )
    ).willReturn(Mono.just(expectedOrder));

    webClient
        .post()
        .uri("/orders")
        .bodyValue(orderRequest)
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(Order.class).value(
            actualVal -> {
              assertThat(actualVal).isNotNull();
              assertThat(actualVal.status()).isEqualTo(OrderStatus.REJECTED);
            }
        );
  }
}