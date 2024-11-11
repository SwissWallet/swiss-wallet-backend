package com.swiss.wallet.repository;

import com.swiss.wallet.entity.TokenNotification;
import com.swiss.wallet.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITokenRepository extends JpaRepository<TokenNotification, Long> {
    List<TokenNotification> findAllByUser(UserEntity user);

    void deleteAllByUser(UserEntity user);
}
