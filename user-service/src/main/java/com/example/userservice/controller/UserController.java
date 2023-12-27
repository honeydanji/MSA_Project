package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.netflix.discovery.converters.Auto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private Environment environment;
    private UserService userService;

    // 필드 종속성 주입 -> 권장x
    @Autowired
    private Greeting greeting;

    // 생성자를 통한 종속성 주입 -> 권장o
    // @AllArgsConstructor 사용해도 무방
    @Autowired
    public UserController(Environment environment, UserService userService) {
        this.environment = environment;
        this.userService = userService;
    }

    @GetMapping("/user-service/health_check")
    public String status() {
        return String.format("It's Working in User Service On PORT %s",
                environment.getProperty("local.server.port"));
    }

    @GetMapping("/user-service/welcome")
    public String welcome() {
        return greeting.getMessage();
//        return environment.getProperty("greeting.message");
    }

    @PostMapping("/user-service/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        // ModelMapper 를 생성하고 매핑 전략을 STRICT 로 설정
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // RequestUser를 UserDto로 매핑
        UserDto userDto = mapper.map(user, UserDto.class);

        // UserService를 통해 사용자 생성
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        // HTTP 상태 코드 201 Created 를 응답으로 전송
        // Status 201 : Post 로 데이터가 정상적으로 생성되었을 경우
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
}
