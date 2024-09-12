package com.swiss.wallet.service;

import com.swiss.wallet.entity.Account;
import com.swiss.wallet.entity.Address;
import com.swiss.wallet.entity.Extract;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.*;
import com.swiss.wallet.repository.*;
import com.swiss.wallet.web.dto.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final IAddressRepository addressRepository;
    private final IAccountRepository accountRepository;
    private final IExtractRepository extractRepository;
    private final PasswordEncoder passwordEncoder;
    private final IOrderRepository orderRepository;
    private final IFavoriteRepository favoriteRepository;

    public UserService(IUserRepository userRepository, IAddressRepository addressRepository, IAccountRepository accountRepository, IExtractRepository extractRepository, PasswordEncoder passwordEncoder, IOrderRepository orderRepository, IFavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
        this.extractRepository = extractRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderRepository = orderRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public UserEntity saveUser(UserAddressCreateDto userAddressCreateDto) {

        try{
            UserEntity user = userAddressCreateDto.user().toUser();
            user.setPassword(passwordEncoder.encode(userAddressCreateDto.user().password()));
            user = userRepository.save(user);
            Address address = addressRepository.save(userAddressCreateDto.address().toAddress());
            user.setAddress(address);
            userRepository.save(user);
            Account account = new Account();
            account.setUser(user);
            accountRepository.save(account);
            return user;
        }catch (DataIntegrityViolationException ex){
            throw new UserUniqueViolationException(String.format("A user with this username= %s already exists. Please use a different username.", userAddressCreateDto.user().username()));
        }

    }

    @Transactional(readOnly = true)
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
    }

    @Transactional
    public String recoverPassword(String username) {
        String code = RandomStringUtils.randomAlphanumeric(6);

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
        user.setVerificationCode(passwordEncoder.encode(code));
        userRepository.save(user);

        return code;
    }

    @Transactional
    public void changeForgottenPassword(UserPasswordRecoveryDto passwordRecoveryDto) {
        UserEntity user = userRepository.findByUsername(passwordRecoveryDto.username())
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        if (!passwordEncoder.matches(passwordRecoveryDto.verificationCode(), user.getVerificationCode())){
            throw new VerificationCodeInvalidException("The verification code provided is invalid or expired. Please request a new code.");
        }

        user.setPassword(passwordEncoder.encode(passwordRecoveryDto.newPassword()));
        user.setVerificationCode(null);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
    }

    @Transactional
    public void changeUserPassword(UserPasswordChangeDto passwordChangeDto, Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        if (!passwordEncoder.matches(passwordChangeDto.currentPassword(), user.getPassword())){
            throw new PasswordInvalidException("The current password provided is invalid. Please try again");
        }

        if(!passwordChangeDto.newPassword().equals(passwordChangeDto.confirmPassword())){
            throw new NewPasswordInvalidException("The new password provided is invalid. Please follow the password requirements.");
        }

        user.setPassword(passwordEncoder.encode(passwordChangeDto.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void changeUserAddress(AddressCreateDto addressCreateDto, Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        Address address = addressRepository.findById(user.getAddress().getId())
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Address not found. Please check the user ID or username and try again."))
                );

        address.setZipCode(addressCreateDto.zipCode());
        address.setStreet(addressCreateDto.street());
        address.setCity(addressCreateDto.city());
        address.setUf(address.getUf());
        address.setNumber(addressCreateDto.number());
        addressRepository.save(address);
    }

    @Transactional
    public void deleteUser(Long id) {
        UserEntity user = findById(id);
        Address address = addressRepository.findById(user.getAddress().getId())
                .orElseThrow(
                () -> new ObjectNotFoundException(String.format("Address not found. Please check the user ID or username and try again."))
        );
        Account account = accountRepository.findAccountByUser(user)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Account not found. Please check the user ID or username and try again."))
                );
        extractRepository.deleteAllByAccount(account);
        orderRepository.deleteAllByUser(user);
        favoriteRepository.deleteAllByUser(user);
        accountRepository.deleteById(account.getId());
        userRepository.deleteById(id);
        addressRepository.deleteById(address.getId());
    }

    @Transactional(readOnly = true)
    public ResponseGlobalDto findByIdGlobal(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        Address address = addressRepository.findById(user.getAddress().getId())
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Address not found. Please check the user ID or username and try again."))
                );
        Account account = accountRepository.findAccountByUser(user)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Account not found. Please check the user ID or username and try again."))
                );
        List<Extract> extract = extractRepository.findAllByAccount(account);

        return ResponseGlobalDto.toResponseGlobal(user, address, account, extract);
    }
}
