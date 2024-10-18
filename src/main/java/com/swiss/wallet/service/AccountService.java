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
import com.swiss.wallet.web.controller.BackendClient;
import com.swiss.wallet.web.dto.BankPurchaseCreateDto;
import com.swiss.wallet.web.dto.PurchasePointsDto;
import com.swiss.wallet.web.dto.PurchaseResponseDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AccountService {

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;
    private final IExtractRepository extractRepository;
    private final NotificationService notificationService;
    private final BackendClient backendClient;

    public AccountService(IAccountRepository accountRepository, IUserRepository userRepository, IExtractRepository extractRepository, NotificationService notificationService, BackendClient backendClient) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.extractRepository = extractRepository;
        this.notificationService = notificationService;
        this.backendClient = backendClient;
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

        deposit(user, value);

        List<TokenNotification> tokens = notificationService.findAllByUser(user);

        tokens.stream()
                .forEach(tokenNotification -> {
                    notificationService.sendNotification(tokenNotification.getToken(), "Deposito", String.format("Foram Depositados %.0f pontos", value));
                });

    }

    public void purchasePoints(Long id, PurchasePointsDto purchasePointsDto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        BankPurchaseCreateDto bankPurchaseCreateDto = new BankPurchaseCreateDto(user.getUsername(), purchasePointsDto.typePayment(), purchasePointsDto.value());
        backendClient.savePurchase(bankPurchaseCreateDto);

        deposit(user, purchasePointsDto.value());
    }

    public String generatePointsPix(Long id, PurchasePointsDto purchasePointsDto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        BankPurchaseCreateDto bankPurchaseCreateDto = new BankPurchaseCreateDto(user.getUsername(), purchasePointsDto.typePayment(), purchasePointsDto.value());
        if (purchasePointsDto.points() <= 0 || purchasePointsDto.points() == null){
            throw new ValueInvalidException(String.format("Invalid value for deposit"));
        }
        return backendClient.savePurchasePix(bankPurchaseCreateDto);
    }

    public void deposit(UserEntity user, Double value){
        Account account = accountRepository.findAccountByUser(user)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Account not found. Please check the User %s and try again", user.getName()))
                );
        if (value <= 0 || value == null){
            throw new ValueInvalidException(String.format("Invalid value for deposit"));
        }
        account.setValue(account.getValue() + value);
        accountRepository.save(account);
        Extract extract = new Extract();
        extract.setAccount(account);
        extract.setValue(value);
        extract.setType(Extract.Type.DEPOSIT);
        extract.setDescription(String.format("Deposit into user account username = %s", user.getUsername()));
        extract.setDate(LocalDateTime.now());
        extractRepository.save(extract);
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void checkPayment(){
        List<PurchaseResponseDto> purchases = backendClient.findAllPaids();
        purchases.stream()
                .forEach(purchaseResponseDto ->  {
                    UserEntity user = userRepository.findById(purchaseResponseDto.user().id())
                            .orElseThrow(
                                    () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                            );
                    deposit(user, (double) purchaseResponseDto.value());
                    backendClient.updateStatusPurchase(purchaseResponseDto.id());
                });
    }
}
