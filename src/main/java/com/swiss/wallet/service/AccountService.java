package com.swiss.wallet.service;

import com.swiss.wallet.entity.Account;
import com.swiss.wallet.entity.Extract;
import com.swiss.wallet.entity.TokenNotification;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.exception.ValueInvalidException;
import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IExtractRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;
    private final IExtractRepository extractRepository;
    private final NotificationService notificationService;

    public AccountService(IAccountRepository accountRepository, IUserRepository userRepository, IExtractRepository extractRepository, NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.extractRepository = extractRepository;
        this.notificationService = notificationService;
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

    @Transactional
    public void registerDeposit(String username, Double value) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
        Account account = accountRepository.findAccountByUser(user)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Account not found. Please check the User %s and try again", user.getName()))
                );
        if (value <= 0 || value == null){
            throw new ValueInvalidException(String.format("Invalid value for deposit"));
        }
        account.setValue(account.getValue() + value);

        Extract extract = new Extract();
        extract.setAccount(account);
        extract.setValue(value);
        extract.setType(Extract.Type.DEPOSIT);
        extract.setDescription(String.format("Deposit into user account username = %s", user.getUsername()));
        extract.setDate(LocalDateTime.now());
        extractRepository.save(extract);

        List<TokenNotification> tokens = notificationService.findAllByUser(user);

        tokens.stream()
                .forEach(tokenNotification -> {
                    notificationService.sendNotification(tokenNotification.getToken(), "Deposito", String.format("Foram Depositados %.0f pontos", value));
                });

    }
}
