package com.example.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component // bean 에 등록시킴. (역할이 없는 경우 사용)
@Data
//@AllArgsConstructor // argument 가 존재하는 생성자
//@NoArgsConstructor // argument 가 존재하지 않는 default 생성자
public class Greeting {
    @Value("${greeting.message}")
    private String message;
}
