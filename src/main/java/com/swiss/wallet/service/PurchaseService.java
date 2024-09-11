package com.swiss.wallet.service;

import com.swiss.wallet.entity.*;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.exception.PointInsufficientException;
import com.swiss.wallet.repository.*;
import com.swiss.wallet.web.dto.PurchaseCreateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseService {

    private final IPurchaseRepository iPurchaseRepository;
    private final IUserRepository userRepository;
    private final IProductRepository iProductRepository;
    private final IAccountRepository iAccountRepository;
    private final IExtractRepository iExtractRepository;
    private final IOrderRepository orderRepository;

    public PurchaseService(IPurchaseRepository iPurchaseRepository, IUserRepository userRepository, IProductRepository iProductRepository, IAccountRepository iAccountRepository, IExtractRepository iExtractRepository, IOrderRepository orderRepository) {
        this.iPurchaseRepository = iPurchaseRepository;
        this.userRepository = userRepository;
        this.iProductRepository = iProductRepository;
        this.iAccountRepository = iAccountRepository;
        this.iExtractRepository = iExtractRepository;
        this.orderRepository = orderRepository;
    }

    public float calcValueTotal(List<Long> productIds){
        List<Product> products = iProductRepository.findAllById(productIds);
        float total = 0.0f;
        for (Product product : products){
            total += product.getValue();
        }
        return total;
    }

    @Transactional
    public Purchase savePurchase(PurchaseCreateDto purchaseCreateDto) {

        float value = calcValueTotal(purchaseCreateDto.productIds());

        UserEntity user = userRepository.findByUsername(purchaseCreateDto.username())
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User username= %s not found", purchaseCreateDto.username()))
                );

        List<Product> products = iProductRepository.findAllById(purchaseCreateDto.productIds());
        Account account = iAccountRepository.findAccountByUser(user).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Account id = %s not found", user.getName()))
        );

        if (account.getValue() < value){
            throw new PointInsufficientException("Insufficient ponint balance");
        }
        account.setValue(account.getValue() - value);
        iAccountRepository.save(account);

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setValue(value);
        purchase.setProducts(products);
        purchase = iPurchaseRepository.save(purchase);

        Extract extract = new Extract();
        extract.setAccount(account);
        extract.setValue((double) value);
        extract.setType(Extract.Type.TRANSACTION);
        extract.setDescription("Points transaction");
        extract.setDate(LocalDateTime.now());
        iExtractRepository.save(extract);

        List<Order> orders = orderRepository.findAllByUserAndProductIn(user, products);
        orders.stream()
                .forEach(order -> order.setStatus(Status.COMPLETED));
        orderRepository.saveAll(orders);

        return purchase;
    }

    @Transactional(readOnly = true)
    public List<Purchase> findAll() {
        return iPurchaseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Purchase> findAllByUser(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User username= %s not found", username))
                );
        return iPurchaseRepository.findAllByUser(user);
    }
}
