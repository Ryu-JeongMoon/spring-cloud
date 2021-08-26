package com.example.catalogservice.messagequeue;

import com.example.catalogservice.entity.Catalog;
import com.example.catalogservice.entity.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CatalogRepository catalogRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "example-catalog-topic")
    public void updateQuantity(String kafkaMessage) {

        log.info("kafka message -> {}", kafkaMessage);
        Map<Object, Object> result = new HashMap<>();

        try {
            result = objectMapper.readValue(kafkaMessage, new TypeReference<>() {});
        } catch (JsonProcessingException jx) {
            jx.printStackTrace();
        }

        Catalog catalog = catalogRepository.findByProductId(String.valueOf(result.get("productId")))
                                           .orElseThrow(() -> new RuntimeException("no available"));

        catalog.setQuantity(catalog.getQuantity() - (Integer) result.get("quantity"));
    }
}
