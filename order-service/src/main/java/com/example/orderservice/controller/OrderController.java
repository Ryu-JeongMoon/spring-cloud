package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;
    private final Environment environment;
    private final ModelMapper modelMapper;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health-check")
    public String check() {
        return String.format("It's working in order-service on Port %s", environment.getProperty("local.server.port"));
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrders(@PathVariable String userId) throws Exception {
        log.info("BEFORE RETRIEVE ORDERS DATA");
        Iterable<Order> orders = orderService.getOrdersByUserId(userId);
        List<OrderResponse> result = new ArrayList<>();
        orders.forEach(order -> result.add(modelMapper.map(order, OrderResponse.class)));

        /*
        try {
            Thread.sleep(1000);
            throw new Exception("장애 발생");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        log.info("AFTER RETRIEVE ORDERS DATA");

        return ResponseEntity.status(HttpStatus.OK)
                             .body(result);
    }

    @PostMapping("/orders/{userId}")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable String userId, @RequestBody OrderRequest orderRequest) {

        log.info("BEFORE ADD ORDERS DATA");

        OrderDto orderDto = modelMapper.map(orderRequest, OrderDto.class);
        orderDto.setUserId(userId);

        /** JPA 관련 작업 */
        orderService.createOrder(orderDto);
        OrderResponse orderResponse = modelMapper.map(orderDto, OrderResponse.class);

        /** Kafka 작업 */
/*
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderRequest.getQuantity() * orderRequest.getUnitPrice());

        */
        /** send order to kafka */

        kafkaProducer.send("example-catalog-topic", orderDto);

        /*orderProducer.send("orders", orderDto);
        OrderResponse orderResponse = modelMapper.map(orderDto, OrderResponse.class);*/

        log.info("AFTER ADD ORDERS DATA");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(orderResponse);
    }
}
