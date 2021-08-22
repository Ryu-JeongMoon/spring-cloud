package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.vo.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Long createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);

    Iterable<User> getUserByAll();

    Optional<UserDto> getDetailsByEmail(String email);
}
