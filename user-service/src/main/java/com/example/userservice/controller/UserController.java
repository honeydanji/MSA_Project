package com.example.userservice.controller;

import com.example.userservice.vo.Greeting;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    private Environment environment;

    // 필드 종속성 주입 -> 권장x
    @Autowired
    private Greeting greeting;

    // 생성자를 통한 종속성 주입 -> 권장o
    @Autowired
    public UserController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/health_check")
    public String status() {
        return "It's Working in User Service.";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greeting.getMessage();
//        return environment.getProperty("greeting.message");
    }
}
