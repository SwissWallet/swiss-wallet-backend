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

@Service
public class AccountService {

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;
    private final IExtractRepository extractRepository;

    public AccountService(IAccountRepository accountRepository, IUserRepository userRepository, IExtractRepository extractRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.extractRepository = extractRepository;
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

    public void registerDeposit(String username, Double value) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
        Account account = accountRepository.findAccountByUser(user)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Account not found. Please check the User %s and try again", user.getName()))
                );
        account.setValue(value);

        Extract extract = new Extract();
        extract.setAccount(account);
        extract.setValue(value);
        extract.setType(Extract.Type.DEPOSIT);
        extract.setDescription(String.format("Deposit into user account username = %s", user.getUsername()));
        extractRepository.save(extract);

    }
}
