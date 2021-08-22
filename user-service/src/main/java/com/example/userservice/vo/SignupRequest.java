package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SignupRequest {

    @NotNull(message = "Email cannot be null")
    @Size(min = 2,
          message = "Email not less than two characters")
    private String email;

    @NotNull(message = "Nickname cannot be null")
    @Size(min = 2,
          message = "Nickname not less than two characters")
    private String nickname;

    @NotNull(message = "Password cannot be null")
    @Size(min = 4,
          max = 16,
          message = "Password should be between 4 and 16 letters")
    private String password;
}
