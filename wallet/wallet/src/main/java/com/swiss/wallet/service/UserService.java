package com.swiss.wallet.service;

import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IAddressRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final IAddressRepository addressRepository;
    private final IAccountRepository accountRepository;

    public UserService(IUserRepository userRepository, IAddressRepository addressRepository, IAccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
    }
}
