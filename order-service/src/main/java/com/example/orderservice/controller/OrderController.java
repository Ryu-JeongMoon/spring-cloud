package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;
    private final Environment environment;
    private final ModelMapper modelMapper;

    @GetMapping("/health-check")
    public String check() {
        return String.format("It's working in order-service on Port %s", environment.getProperty("local.server.port"));
    }

    @PostMapping("/orders/{userId}")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable String userId, @RequestBody OrderRequest orderRequest) {
        OrderDto orderDto = modelMapper.map(orderRequest, OrderDto.class);
        orderDto.setUserId(userId);
        orderService.createOrder(orderDto);
        OrderResponse orderResponse = modelMapper.map(orderDto, OrderResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(orderResponse);
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrders(@PathVariable String userId) {
        Iterable<Order> orders = orderService.getOrdersByUserId(userId);
        List<OrderResponse> result = new ArrayList<>();
        orders.forEach(order -> result.add(modelMapper.map(order, OrderResponse.class)));
        return ResponseEntity.status(HttpStatus.OK)
                             .body(result);
    }
}