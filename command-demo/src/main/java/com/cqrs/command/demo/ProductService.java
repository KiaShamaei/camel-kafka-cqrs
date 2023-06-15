package com.cqrs.command.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.ProducerTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;
    private final ProducerTemplate producerTemplate;

    public ProductService(ProductRepository repository,
                          KafkaTemplate<String, ProductEvent> kafkaTemplate,
                          ProducerTemplate producerTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.producerTemplate = producerTemplate;
    }
    public Product add(Product p) throws JsonProcessingException {
        var product = repository.save(p);
        ProductEvent event = new ProductEvent("ProductCreated", product);
        // Creating Object of ObjectMapper define in Jackson API
        ObjectMapper Obj = new ObjectMapper();
        // Converting the Java object into a JSON string
        String jsonEvent = Obj.writeValueAsString(event);
        // Displaying Java object into a JSON string
        System.out.println(jsonEvent);


        this.producerTemplate.asyncRequestBody("kafka:products?brokers=localhost:9092",
                jsonEvent);
        //just for test kafka
//      this.kafkaTemplate.send("products", event);
        return product;
    }
}
