package com.cqrs.command.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ProductEvent {

    private String type;
    private Product product;

    
}

