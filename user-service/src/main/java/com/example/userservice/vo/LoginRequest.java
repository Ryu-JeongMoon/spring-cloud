package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {

    @Email
    @NotNull(message = "email cannot be null")
    @Size(min = 2,
          message = "email not be less than two letters")
    private String email;

    @NotNull(message = "password cannot be null")
    @Size(min = 8,
          message = "password should be greater than 8 letters")
    private String password;

}
