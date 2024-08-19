package com.swiss.wallet.repository;

import com.swiss.wallet.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAddressRepository extends JpaRepository<Address, Long> {
}
