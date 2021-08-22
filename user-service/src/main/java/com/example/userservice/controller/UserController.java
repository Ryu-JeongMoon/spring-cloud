package com.example.userservice.controller;

import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.SignupRequest;
import com.example.userservice.vo.SignupResponse;
import com.example.userservice.vo.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final Environment env;
    private final Greeting greeting;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/health-check")
    public String check() {
        return String.format("It's working in User-service on Port %s\n" +
                             "port(server.port) = %s\n" +
                             "token secret = %s\n" +
                             "token expiration time = %s", env.getProperty("local.server.port"), env.getProperty("server.port"), env.getProperty("token.secret"), env.getProperty("token.expiration_time"));
    }

    /**
     * @Value 쓸 수도 있고, Environment 주입 받아 쓸 수도 있듬
     * 음.. Environment 가져오는게 더 편한거 같은데?!
     */
    @GetMapping("/welcome")
    public String welcome() {
        //      return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<SignupResponse> createUser(@Valid @RequestBody SignupRequest signupRequest) {

        UserDto userDto = modelMapper.map(signupRequest, UserDto.class);
        userService.createUser(userDto);
        SignupResponse signupResponse = modelMapper.map(userDto, SignupResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(signupResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<SignupResponse>> getUsers() {
        List<SignupResponse> result = new ArrayList<>();
        Iterable<User> users = userService.getUserByAll();

        users.forEach(user -> result.add(modelMapper.map(user, SignupResponse.class)));

        return ResponseEntity.status(HttpStatus.OK)
                             .body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<SignupResponse> getUser(@PathVariable String userId) {

        UserDto userDto = userService.getUserByUserId(userId);
        SignupResponse signupResponse = modelMapper.map(userDto, SignupResponse.class);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(signupResponse);
    }
}
