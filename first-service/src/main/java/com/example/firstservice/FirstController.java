package com.example.firstservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/first-service")
@RequiredArgsConstructor
public class FirstController {

    private final Environment env;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the first-service";
    }

    @GetMapping("/message")
    public void message(@RequestHeader("first-request") String header) {
        log.info("header = {}", header);
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("server port = {}", request.getServerPort());
        return String.format("Hi there. This message is from first-service port number : %s", env.getProperty("local.server.port"));
    }
}

/**
 * 따로 설정해두지 않으면
 * Load Balancing 을 통해 번갈아가면서 랜덤 포트로 생성된 두 포트에게 한번씩 보냄!
 */