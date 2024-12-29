package com.yuki.webfluxorderserver.book.web.client;

import com.yuki.webfluxorderserver.order.domain.entity.Book;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookClient {
  private static final String BOOK_ROOT_API = "/books/";
  private final WebClient webClient;

  public Mono<Book> getBookByIsbn(String isbn) {
    return webClient.get()
        .uri(BOOK_ROOT_API + isbn)
        .retrieve() // 요청을 보내고 응답을 받는다.
        .bodyToMono(Book.class)
        .timeout(Duration.ofSeconds(3), Mono.empty())
        .onErrorResume(WebClientResponseException.NotFound.class,
            exception -> Mono.empty())
        .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
        .onErrorResume(Exception.class,
            exception -> Mono.empty());
  }
}
