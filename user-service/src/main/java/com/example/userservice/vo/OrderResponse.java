package com.example.userservice.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OrderResponse {

    private Long id;
    private int quantity;
    private int unitPrice;
    private int totalPrice;
    private Date createdAt;
    private Long orderId;

}
