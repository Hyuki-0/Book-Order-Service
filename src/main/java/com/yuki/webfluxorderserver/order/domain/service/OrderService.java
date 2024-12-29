package com.yuki.webfluxorderserver.order.domain.service;

import com.yuki.webfluxorderserver.book.web.client.BookClient;
import com.yuki.webfluxorderserver.order.domain.entity.Book;
import com.yuki.webfluxorderserver.order.domain.entity.Order;
import com.yuki.webfluxorderserver.order.domain.entity.OrderStatus;
import com.yuki.webfluxorderserver.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final BookClient bookClient;

  public Flux<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  public Mono<Order> submitOrder(String isbn, int quantity) {
    return bookClient.getBookByIsbn(isbn)
        .map(book -> buildAccpetedOrder(book, quantity))
        .defaultIfEmpty(
            buildRejectedOrder(isbn, quantity)
        )
        .flatMap(orderRepository::save);
  }

  public static Order buildAccpetedOrder(Book book, int quantity) {
    return Order.of(book.isbn(), book.title() + " - " + book.author(),
        book.price(), quantity, OrderStatus.ACCEPTED);
  }
  public static Order buildRejectedOrder(String isbn, int quantity) {
    return Order.of(isbn, null, null, quantity, OrderStatus.REJECTED);
  }
}
