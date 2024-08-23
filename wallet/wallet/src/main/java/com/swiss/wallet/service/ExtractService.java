package com.swiss.wallet.service;


import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IExtractRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class ExtractService {

    private final IExtractRepository extractRepository;
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;

    public ExtractService(IExtractRepository extractRepository, IUserRepository userRepository, IAccountRepository accountRepository) {
        this.extractRepository = extractRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }
}
