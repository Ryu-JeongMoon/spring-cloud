package com.example.catalogservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogResponse implements Serializable {

    private String productId;
    private String productName;
    private int quantity;
    private int unitPrice;
    private Date createdAt;
}
