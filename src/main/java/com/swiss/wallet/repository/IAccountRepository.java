package com.swiss.wallet.repository;

import com.swiss.wallet.entity.Account;
import com.swiss.wallet.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByUser(UserEntity user);
}
