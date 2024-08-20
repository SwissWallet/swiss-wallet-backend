package com.swiss.wallet.service;

import com.swiss.wallet.entity.Account;
import com.swiss.wallet.entity.Address;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.UserNotFoundException;
import com.swiss.wallet.exception.UserUniqueViolationException;
import com.swiss.wallet.exception.VerificationCodeInvalidException;
import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IAddressRepository;
import com.swiss.wallet.repository.IUserRepository;
import com.swiss.wallet.web.dto.UserAddressCreateDto;
import com.swiss.wallet.web.dto.UserPasswordRecoveryDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final IAddressRepository addressRepository;
    private final IAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, IAddressRepository addressRepository, IAccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity saveUser(UserAddressCreateDto userAddressCreateDto) {

        Address address = addressRepository.save(userAddressCreateDto.address().toAddress());

        UserEntity user = userAddressCreateDto.user().toUser();
        if(user == null){
            throw new UserUniqueViolationException(String.format("A user with this cpf= %s already exists. Please use a different cpf.", user.getCpf()));
        }
        user.setAddress(address);
        user.setPassword(passwordEncoder.encode(userAddressCreateDto.user().password()));
        user = userRepository.save(user);


        Account account = new Account();
        account.setUser(user);
        accountRepository.save(account);

        return user;
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
    }

    //Method to generate forgotten code the password
    public String recoverPassword(String username) {
        String code = RandomStringUtils.randomAlphanumeric(6);

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
        user.setVerificationCode(passwordEncoder.encode(code));
        userRepository.save(user);

        return code;
    }

    //Method for changing a forgotten user password, passing the username, verification code and new password
    public void changeForgottenPassword(UserPasswordRecoveryDto passwordRecoveryDto) {
        UserEntity user = userRepository.findByUsername(passwordRecoveryDto.username())
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        if (!passwordEncoder.matches(passwordRecoveryDto.verificationCode(), user.getVerificationCode())){
            throw new VerificationCodeInvalidException("The verification code provided is invalid or expired. Please request a new code.");
        }

        user.setPassword(passwordEncoder.encode(passwordRecoveryDto.newPassword()));
        user.setVerificationCode(null);
        userRepository.save(user);
    }
}
