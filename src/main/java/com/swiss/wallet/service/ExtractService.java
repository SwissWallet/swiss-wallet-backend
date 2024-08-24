package com.swiss.wallet.service;


import com.swiss.wallet.entity.Account;
import com.swiss.wallet.entity.Extract;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IExtractRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public List<Extract> findByUserLogged(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        Account account = accountRepository.findAccountByUser(user).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Account id = %s not found", user.getName()))
        );

        return extractRepository.findAllByAccount(account);
    }
}
