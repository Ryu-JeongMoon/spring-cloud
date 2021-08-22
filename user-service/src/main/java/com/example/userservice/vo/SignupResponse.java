package com.example.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignupResponse {

    private String email;
    private String nickname;
    private String userId;

    private List<OrderResponse> orders;
}
