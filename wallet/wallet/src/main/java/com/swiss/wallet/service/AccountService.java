package com.swiss.wallet.service;

import com.swiss.wallet.entity.Account;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;

    public AccountService(IAccountRepository accountRepository, IUserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Account findByUserId(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        Account account = accountRepository.findAccountByUser(user)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Account not found. Please check the User %s and try again", user.getName()))
                );
        return account;
    }
}
