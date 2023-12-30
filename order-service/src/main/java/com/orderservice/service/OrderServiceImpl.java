package com.orderservice.service;

import com.orderservice.dto.OrderDto;
import com.orderservice.jpa.OrderEntity;
import com.orderservice.jpa.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Override
    public OrderDto createOrder(OrderDto orderDto) {

//        log.info("[service] 수량 확인:" + orderDto.getQty());
//        log.info("[service] 단가 확인:" + orderDto.getUnitPrice());

        orderDto.setOrderId(UUID.randomUUID().toString()); // id 랜덤 생성
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice()); // 총금액 = 수량 * 단가

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // mapper 환경 설정
        OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class); // userDto 를 UserEntity.class 로 변환

        orderRepository.save(orderEntity);
        OrderDto returnValue = mapper.map(orderEntity, OrderDto.class);

        return returnValue;
    }

    @Override
    public Iterable<OrderEntity> getOrderByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {

        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto orderDto = new ModelMapper().map(orderEntity, OrderDto.class);
        return orderDto;
    }
}
