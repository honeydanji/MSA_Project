package com.example.userservice.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
         http.csrf(AbstractHttpConfigurer::disable) // csrf 사용 x
                .headers(authorize -> authorize.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // 기본 프레임 사용 x
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users/**").permitAll()   // 허용 url
                        .requestMatchers(PathRequest.toH2Console()).permitAll()); // h2 콘솔 허용
         return http.build();
    }

}
