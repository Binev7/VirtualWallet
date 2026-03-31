package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.user.UserDetailsAdminDto;
import com.portfolio.virtualwallet.entity.dto.user.UserPublicResponseDto;
import com.portfolio.virtualwallet.mapper.UserMapper;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.repository.specification.UserSpecification;
import com.portfolio.virtualwallet.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserPublicResponseDto> searchPublicUsers(String searchTerm, Pageable pageable) {
        Specification<User> spec = UserSpecification.searchUsers(searchTerm);

        return userRepository.findAll(spec, pageable)
                .map(userMapper::toPublicDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDetailsAdminDto> adminSearchUsers(String searchTerm, Pageable pageable) {
        Specification<User> spec = UserSpecification.searchUsers(searchTerm);

        return userRepository.findAll(spec, pageable)
                .map(userMapper::toAdminDto);
    }
}