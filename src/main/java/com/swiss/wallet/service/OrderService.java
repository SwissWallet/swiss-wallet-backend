package com.swiss.wallet.service;

import com.swiss.wallet.entity.Order;
import com.swiss.wallet.entity.Product;
import com.swiss.wallet.entity.Status;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.repository.IOrderRepository;
import com.swiss.wallet.repository.IProductRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderService {

    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    public OrderService(IOrderRepository orderRepository, IProductRepository productRepository, IUserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Order saveOrder(Long id, Long idProduct) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
        Product product = productRepository.findById(idProduct)
                .orElseThrow(
                        () -> new ObjectNotFoundException("Product not found, Please check the product ID and try again")
                );

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setStatus(Status.ANALYSIS);
        return orderRepository.save(order);

    }

    public List<Order> findAllByUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        return orderRepository.findAllByUser(user);

    }
}
