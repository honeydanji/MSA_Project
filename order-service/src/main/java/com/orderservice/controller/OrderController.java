package com.orderservice.controller;

import com.orderservice.dto.OrderDto;
import com.orderservice.jpa.OrderEntity;
import com.orderservice.service.OrderService;
import com.orderservice.vo.RequestOrder;
import com.orderservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service")
public class OrderController {
    private final Environment env;
    private final OrderService orderService;

    @Autowired
    public OrderController(Environment env, OrderService orderService) {
        this.env = env;
        this.orderService = orderService;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service On PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                    @RequestBody RequestOrder orderDetails) {
        // ModelMapper 를 생성하고 매핑 전략을 STRICT 로 설정
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);

        OrderDto createdOrder = orderService.createOrder(orderDto);

        ResponseOrder responseOrder = mapper.map(createdOrder, ResponseOrder.class);

        // HTTP 상태 코드 201 Created 를 응답으로 전송
        // Status 201 : Post 로 데이터가 정상적으로 생성되었을 경우
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {
        Iterable<OrderEntity> orderList = orderService.getOrderByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(orderEntity -> {
            result.add(new ModelMapper().map(orderEntity, ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
