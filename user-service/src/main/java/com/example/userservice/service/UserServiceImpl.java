package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        // 해당 객체에 종속성을 주입 하기 위해서는 해당 필드들이 bean 에 등록이 된 상태여야 한다.
        // app 이 실행되는 동시에 지정한 객체들은 자동으로 bean 에 등록된다.
        // BCryptPasswordEncoder 같은 경우는 "UserServiceApplication"에서 @Bean 을 이용해 등록하였다.
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString()); // id 랜덤 생성

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // mapper 환경 설정
        UserEntity userEntity = mapper.map(userDto, UserEntity.class); // userDto 를 UserEntity.class 로 변환
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        return null;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        try {
            // 1. DB 에서 유저 정보를 가져와서 UserEntity 에 저장.
            UserEntity userEntity = userRepository.findByUserId(userId);

            // 2. UserEntity 를 UserDto 로 변환해준다.
            UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

            // 3. 주문 정보 가져오기 및 저장
            List<ResponseOrder> orders = new ArrayList<>();
            userDto.setOrders(orders);

            // 4. 주문 정보가 포함된 유저 정보 반환
            return userDto;
        } catch (Exception e) {
            // 예외 : 유저 정보가 없을 경우
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>()
        );
    }
}
