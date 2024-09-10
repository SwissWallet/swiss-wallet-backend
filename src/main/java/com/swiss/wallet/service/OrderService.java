package com.swiss.wallet.service;

import com.swiss.wallet.entity.*;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.exception.OrderProductInavlidException;
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

        if(product.getCategory() != Category.STORE){
            throw new OrderProductInavlidException(String.format("Invalid product to order"));
        }
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

    public void deleteByIdAndUser(Long idOrder, Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        Order order = orderRepository.findByIdAndUser(idOrder, user)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Order not found. Please check the user ID or username and try again."))
                );
        orderRepository.deleteById(order.getId());
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findAllByStatus(String status) {

        return switch (status) {
            case "ANALYSIS" -> orderRepository.findAllByStatus(Status.ANALYSIS);
            case "COMPLETED" -> orderRepository.findAllByStatus(Status.COMPLETED);
            case "UNAVAILABLE" -> orderRepository.findAllByStatus(Status.UNAVAILABLE);
            case "SEPARATED" -> orderRepository.findAllByStatus(Status.SEPARATED);
            default -> null;
        };
    }

    public Order changeStatus(Long idOrder, String status) {
        Order order = orderRepository.findById(idOrder).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Order id: %s not found", idOrder))
        );
        switch (status) {
            case "ANALYSIS" -> order.setStatus(Status.ANALYSIS);
            case "SEPARATED" -> order.setStatus(Status.SEPARATED);
            case "COMPLETED" -> order.setStatus(Status.COMPLETED);
            case "UNAVAILABLE" -> order.setStatus(Status.UNAVAILABLE);
        };
        return orderRepository.save(order);
    }
}
