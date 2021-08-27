package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.entity.User;
import com.example.userservice.error.FeignErrorDecoder;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.OrderResponse;
import com.example.userservice.vo.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;
    private final FeignErrorDecoder feignErrorDecoder;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                                  .orElseThrow(() -> new UsernameNotFoundException("그런 회원 없습네다"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }

    @Override
    public Long createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        modelMapper.getConfiguration()
                   .setMatchingStrategy(STRICT);
        User user = modelMapper.map(userDto, User.class);
        user.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user).getId();
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        User user = userRepository.findByUserId(userId)
                                  .orElseThrow(() -> new UsernameNotFoundException("no!"));

        UserDto userDto = modelMapper.map(user, UserDto.class);
        //List<OrderResponse> orders = new ArrayList<>();

        String orderUrl = String.format(environment.getProperty("order-service.url"), userId);

        /** Using rest template
         ResponseEntity<List<OrderResponse>> orderList = restTemplate.exchange(orderUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<OrderResponse>>() {

         });
         List<OrderResponse> result = orderList.getBody();
         */

        /** Using feign client */
        /** try - catch 문을 이용한 error handling
         List<OrderResponse> result = null;
         try {
         result = orderServiceClient.getOrders(userId);
         } catch (FeignException fe) {
         log.error("error = {}", fe.getMessage());
         }
         */

        /** ErrorDecoder */
        /** List<OrderResponse> result = orderServiceClient.getOrders(userId); */

        /** CircuitBreaker */
        log.info("BEFORE ORDER-SERVICE CALL");
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        List<OrderResponse> result = circuitbreaker.run(() -> orderServiceClient.getOrders(userId), throwable -> new ArrayList<>());
        log.info("AFTER ORDER-SERVICE CALL");

        userDto.setOrders(result);
        return userDto;
    }

    @Override
    public Iterable<User> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserDto> getDetailsByEmail(String email) {
        return Optional.of(modelMapper.map(userRepository.findByEmail(email)
                                                         .orElseThrow(() -> new UsernameNotFoundException("No Member")), UserDto.class));
    }
}
