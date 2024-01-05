package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    private final Environment environment;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectPostProcessor<Object> objectPostProcessor;

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
         http.csrf(AbstractHttpConfigurer::disable)
                 .headers(authorize -> authorize.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // 기본 프레임 사용 x
                 .authorizeHttpRequests(authorize -> authorize
                         .requestMatchers("/**").permitAll()
                         .requestMatchers(PathRequest.toH2Console()).permitAll()
                         .requestMatchers(new IpAddressMatcher("127.0.0.1")).permitAll())
                 .addFilter(getAuthenticationFilter());
         return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, environment);
        AuthenticationManagerBuilder managerBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
        authenticationFilter.setAuthenticationManager(authenticationManager(managerBuilder));
        return authenticationFilter;
    }

    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        return managerBuilder.build();
    }
}
