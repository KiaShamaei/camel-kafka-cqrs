package com.cqrs.query.demo;

import lombok.Data;

@Data
public class ProductEvent {
    private String type;
    private Product product;
}

