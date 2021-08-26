package com.example.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Schema {

    private String type;
    private List<Field> fields = new ArrayList<>();
    private boolean optional;
    private String name;
}
