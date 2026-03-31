package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.dto.user.UserDetailsAdminDto;
import com.portfolio.virtualwallet.entity.dto.user.UserPublicResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserPublicResponseDto> searchPublicUsers(String searchTerm, Pageable pageable);

    Page<UserDetailsAdminDto> adminSearchUsers(String searchTerm, Pageable pageable);

    void toggleUserBlockStatus(Long userId, boolean isBlocked);
}