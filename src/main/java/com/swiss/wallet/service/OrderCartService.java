package com.swiss.wallet.service;

import com.swiss.wallet.entity.*;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.exception.OrderCartAlreadyPaidException;
import com.swiss.wallet.exception.PointInsufficientException;
import com.swiss.wallet.repository.*;
import com.swiss.wallet.web.dto.OrderCartCreateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderCartService {

    private final IOrderCartRepository iOrderCartRepository;
    private final IUserRepository userRepository;
    private final IProductRepository iProductRepository;
    private final IAccountRepository iAccountRepository;
    private final IExtractRepository iExtractRepository;
    private final IOrderRepository orderRepository;

    public OrderCartService(IOrderCartRepository iOrderCartRepository, IUserRepository userRepository, IProductRepository iProductRepository, IAccountRepository iAccountRepository, IExtractRepository iExtractRepository, IOrderRepository orderRepository) {
        this.iOrderCartRepository = iOrderCartRepository;
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
    public OrderCart saveProductsInOrderCart(Long idUser, OrderCartCreateDto orderCartCreateDto) {

        float value = calcValueTotal(orderCartCreateDto.productIds());

        UserEntity user = userRepository.findById(idUser)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User username= %s not found", idUser))
                );

        List<Product> products = iProductRepository.findAllById(orderCartCreateDto.productIds());
        Account account = iAccountRepository.findAccountByUser(user).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Account id = %s not found", user.getName()))
        );

        if (products.isEmpty()){
            throw new ObjectNotFoundException(String.format("No products added to the list"));
        }

        OrderCart orderCart = new OrderCart();
        orderCart.setUser(user);
        orderCart.setValue(value);
        orderCart.setProducts(products);
        orderCart.setStatus(StatusOrderCart.PENDING);
        return iOrderCartRepository.save(orderCart);
    }

    @Transactional(readOnly = true)
    public List<OrderCart> findAll() {
        return iOrderCartRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<OrderCart> findAllByUser(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User username= %s not found", username))
                );
        return iOrderCartRepository.findAllByUser(user);
    }

    @Transactional
    public void paymentOrderCart(Long idUser, Long idOrderCart){
        OrderCart orderCart = iOrderCartRepository.findById(idOrderCart)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Order card not found"))
                );
        if (orderCart.getStatus().name() == StatusOrderCart.PAID.name()){
            throw new OrderCartAlreadyPaidException("Order Cart already paid");
        }
        UserEntity user = userRepository.findById(idUser)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
        Account account = iAccountRepository.findAccountByUser(user)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Account not found. Please check the user ID or username and try again."))
                );

        if (account.getValue() < orderCart.getValue()){
            throw new PointInsufficientException("Insufficient ponint balance");
        }

        account.setValue(account.getValue() - orderCart.getValue());
        iAccountRepository.save(account);

        List<Order> orders = orderRepository.findAllByUserAndProductIn(user, orderCart.getProducts());
        orders.stream()
                .forEach(order -> order.setStatus(Status.COMPLETED));
        orderRepository.saveAll(orders);

        orderCart.setStatus(StatusOrderCart.PAID);
        iOrderCartRepository.save(orderCart);

        Extract extract = new Extract();
        extract.setAccount(account);
        extract.setValue((double) orderCart.getValue());
        extract.setType(Extract.Type.TRANSACTION);
        extract.setDescription(String.format("Purchase made in the %s", orderCart.getProducts().get(0).getCategory().name()));
        extract.setDate(LocalDateTime.now());
        iExtractRepository.save(extract);
    }
}
