package com.yuki.webfluxorderserver.order.domain.service;

import com.yuki.webfluxorderserver.order.domain.entity.Order;
import com.yuki.webfluxorderserver.order.domain.entity.OrderStatus;
import com.yuki.webfluxorderserver.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  public Flux<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  public Mono<Order> submitOrder(String isbn, int quantity) {
    return Mono.just(buildRejectedOrder(isbn, quantity))
        .flatMap(orderRepository::save);
  }

  public static Order buildRejectedOrder(String isbn, int quantity) {
    return Order.of(isbn, null, null, quantity, OrderStatus.REJECTED);
  }
}
