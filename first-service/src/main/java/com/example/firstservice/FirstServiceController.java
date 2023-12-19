package com.example.firstservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/first-service")
@Slf4j
public class FirstServiceController {
    Environment environment; // 환경변수(application.yml 설정 요소)

    @Autowired
    public FirstServiceController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the First service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header) {
        log.info(header);
        return "Hello World in First Service";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        // console 에서 log 확인
        log.info("Server port={}", request.getServerPort());

        // http 요청에 의한 응답으로 확인
        return String.format("Hi, there. This is a message from First Service on PORT %s",
                environment.getProperty("local.server.port"));
    }
    
}
