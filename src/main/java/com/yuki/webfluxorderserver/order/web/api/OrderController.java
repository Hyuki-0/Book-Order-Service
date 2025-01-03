package com.yuki.webfluxorderserver.order.web.api;

import com.yuki.webfluxorderserver.order.domain.entity.Order;
import com.yuki.webfluxorderserver.order.domain.service.OrderService;
import com.yuki.webfluxorderserver.order.web.dto.request.OrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  public Flux<Order> getAllOrders() {
    return orderService.getAllOrders();
  }

  @PostMapping
  public Mono<Order> submitOrder(
      @RequestBody @Valid OrderRequest orderRequest
  ) {
    return orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity());
  }
}
