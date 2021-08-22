package com.example.userservice.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {

    private String email;
    private String nickname;
    private String password;
    private String userId;
    private Date createdAt;
    private String encryptedPassword;

    private List<OrderResponse> orders;

}
