package com.portfolio.virtualwallet.mapper;

import com.portfolio.virtualwallet.entity.dto.auth.UserRegisterDto;
import com.portfolio.virtualwallet.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRegisterDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }
}