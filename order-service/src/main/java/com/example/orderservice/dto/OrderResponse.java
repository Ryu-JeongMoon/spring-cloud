package com.example.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    private Long id;
    private String productId;
    private int quantity;
    private int unitPrice;
    private int totalPrice;
    private Date createdAt;
    private String orderId;
}
