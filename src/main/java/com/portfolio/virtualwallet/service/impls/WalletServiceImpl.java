package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.WalletMembership;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletCreateDto;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletResponseDto;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletUpdateDto;
import com.portfolio.virtualwallet.exception.DuplicateEntityException;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.WalletNotEmptyException;
import com.portfolio.virtualwallet.mapper.WalletMapper;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.repository.WalletMembershipRepository;
import com.portfolio.virtualwallet.repository.WalletRepository;
import com.portfolio.virtualwallet.service.interfaces.WalletService;
import com.portfolio.virtualwallet.utils.AppConstants;
import com.portfolio.virtualwallet.utils.SecurityUtils;
import com.portfolio.virtualwallet.utils.WalletValidationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.User.USER_NOT_FOUND;
import static com.portfolio.virtualwallet.exception.ExceptionMessages.Wallet.WALLET_ALREADY_EXISTS;
import static com.portfolio.virtualwallet.exception.ExceptionMessages.Wallet.WALLET_NON_EMPTY;
import static com.portfolio.virtualwallet.exception.ExceptionMessages.Wallet.WALLET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMembershipRepository walletMembershipRepository;
    private final UserRepository userRepository;
    private final WalletMapper walletMapper;
    private final WalletValidationHelper walletValidationHelper;


    @Override
    @Transactional(readOnly = true)
    public List<WalletResponseDto> getMyWallets() {
        String currentUsername = SecurityUtils.getCurrentUsername();

        return walletMembershipRepository.findAllByUserUsername(currentUsername)
                .stream()
                .map(membership -> walletMapper.toDto(membership.getWallet(), membership))
                .toList();
    }

    @Override
    @Transactional
    public void initializeDefaultWallet(User user) {
        Wallet wallet = walletMapper.createDefaultWalletEntity(user);
        Wallet savedWallet = walletRepository.save(wallet);

        WalletMembership membership = walletMapper.createMembershipEntity(
                user, savedWallet, true, true, true);

        walletMembershipRepository.save(membership);
    }

    @Override
    @Transactional
    public WalletResponseDto createWallet(WalletCreateDto request) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        if (walletMembershipRepository.existsByWalletNameAndUserUsername(request.getName(), currentUsername)) {
            throw new DuplicateEntityException(WALLET_ALREADY_EXISTS);
        }

        Wallet wallet = walletMapper.toEntity(request, user);
        Wallet savedWallet = walletRepository.save(wallet);

        WalletMembership membership = walletMapper.createMembershipEntity(
                user, savedWallet, true, true, false
        );

        WalletMembership savedMembership = walletMembershipRepository.save(membership);

        return walletMapper.toDto(savedWallet, savedMembership);
    }

    @Override
    @Transactional
    public WalletResponseDto updateWallet(Long id, WalletUpdateDto request) {
        Wallet wallet = walletValidationHelper.getWalletIfOwner(id);

        wallet.setName(request.getName());
        Wallet savedWallet = walletRepository.save(wallet);

        WalletMembership membership = walletMembershipRepository
                .findByWalletIdAndUserUsername(id, SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(WALLET_NOT_FOUND));

        return walletMapper.toDto(savedWallet, membership);
    }

    @Override
    @Transactional
    public void deleteWallet(Long id) {
        Wallet wallet = walletValidationHelper.getWalletIfOwner(id);

        if (wallet.getBalance().compareTo(AppConstants.Wallet.INITIAL_BALANCE) > 0) {
            throw new WalletNotEmptyException(WALLET_NON_EMPTY);
        }

        walletMembershipRepository.deleteAllByWalletId(id);
        walletRepository.delete(wallet);
    }
}