package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.user.ChangePasswordDto;
import com.portfolio.virtualwallet.entity.dto.user.UserDetailsAdminDto;
import com.portfolio.virtualwallet.entity.dto.user.UserPublicResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserPublicResponseDto> searchPublicUsers(String searchTerm, Pageable pageable);

    Page<UserDetailsAdminDto> adminSearchUsers(String searchTerm, Pageable pageable);

    void toggleUserBlockStatus(Long userId, boolean isBlocked);

    void changeEmail(User currentUser, String newEmail);

    void changePhoneNumber(User currentUser, String newPhoneNumber);

    void changePassword(User currentUser, ChangePasswordDto request);
}