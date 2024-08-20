package com.swiss.wallet.service;

import com.swiss.wallet.entity.Account;
import com.swiss.wallet.entity.Address;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.UserNotFoundException;
import com.swiss.wallet.exception.UserUniqueViolationException;
import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IAddressRepository;
import com.swiss.wallet.repository.IUserRepository;
import com.swiss.wallet.web.dto.UserAddressCreateDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final IAddressRepository addressRepository;
    private final IAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, IAddressRepository addressRepository, IAccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
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
}
