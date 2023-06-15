package com.cqrs.query.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
    public List<Product> getAll(){
        return repository.findAll().stream()

                .collect(Collectors.toList());
    }

    @KafkaListener(topics = "products", groupId = "products1_group")
    public void processProductEvent(String event) {

        System.out.println("Getting event " + event);

        ProductEvent productEvent = null;
        try {
            productEvent = new ObjectMapper().readValue(event, ProductEvent.class);

            System.out.println(productEvent);

            switch (productEvent.getType()) {
                case "ProductCreated":

                    this.repository.save(productEvent.getProduct());
                    break;

                default:
                    break;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
