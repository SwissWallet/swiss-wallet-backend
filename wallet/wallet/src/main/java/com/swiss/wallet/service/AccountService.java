package com.swiss.wallet.service;

import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;

    public AccountService(IAccountRepository accountRepository, IUserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }
}
