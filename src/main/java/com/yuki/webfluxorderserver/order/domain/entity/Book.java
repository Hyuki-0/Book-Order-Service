package com.yuki.webfluxorderserver.order.domain.entity;

public record Book(
    String isbn,
    String title,
    String author,
    Double price
){}
