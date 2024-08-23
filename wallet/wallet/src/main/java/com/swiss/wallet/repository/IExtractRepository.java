package com.swiss.wallet.repository;

import com.swiss.wallet.entity.Account;
import com.swiss.wallet.entity.Extract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExtractRepository extends JpaRepository<Extract, Long> {
    Extract findExtractByAccount(Account account);
}
