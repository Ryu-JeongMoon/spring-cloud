package com.example.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {

    private String productId;
    private int quantity;
    private int unitPrice;
}
