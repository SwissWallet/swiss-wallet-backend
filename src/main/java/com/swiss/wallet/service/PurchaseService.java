package com.swiss.wallet.service;

import com.swiss.wallet.repository.*;
import org.springframework.stereotype.Service;

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
}
