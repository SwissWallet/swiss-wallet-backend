package com.swiss.wallet.config;

import com.swiss.wallet.entity.Address;
import com.swiss.wallet.entity.Role;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.repository.IAddressRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IAddressRepository addressRepository;

    public AdminUserConfig(IUserRepository userRepository, PasswordEncoder passwordEncoder, IAddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        var userAdmin = userRepository.findByUsername("admin@email.com");
        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("Admin already exists");
                },
                () -> {
                    var address = new Address();
                    address.setStreet("localhost");
                    address.setNumber(8080L);
                    address.setCity("localhost");
                    address.setZipCode("localhost");
                    address.setUf("lh");
                    address = addressRepository.save(address);

                    var userEntity = new UserEntity();
                    userEntity.setUsername("admin@email.com");
                    userEntity.setPassword(passwordEncoder.encode("12345678"));
                    userEntity.setName("Admin");
                    userEntity.setRole(Role.ROLE_ADMIN);
                    userEntity.setAddress(address);
                    userRepository.save(userEntity);

                }
        );

    }
}
