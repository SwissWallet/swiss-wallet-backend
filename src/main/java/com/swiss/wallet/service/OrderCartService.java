package com.swiss.wallet.service;

import com.swiss.wallet.entity.*;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.exception.OrderCartAlreadyPaidException;
import com.swiss.wallet.exception.PointInsufficientException;
import com.swiss.wallet.exception.ProductOutOfStockException;
import com.swiss.wallet.repository.*;
import com.swiss.wallet.utils.UtilsProduct;
import com.swiss.wallet.web.dto.OrderCartCreateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class OrderCartService {

    private final IOrderCartRepository iOrderCartRepository;
    private final IUserRepository userRepository;
    private final IProductRepository iProductRepository;
    private final IAccountRepository iAccountRepository;
    private final IExtractRepository iExtractRepository;
    private final IOrderRepository orderRepository;
    private final UtilsProduct utilsProduct;
    private static final Logger logger = LoggerFactory.getLogger(OrderCartService.class);

    public OrderCartService(IOrderCartRepository iOrderCartRepository, IUserRepository userRepository, IProductRepository iProductRepository, IAccountRepository iAccountRepository, IExtractRepository iExtractRepository, IOrderRepository orderRepository, UtilsProduct utilsProduct) {
        this.iOrderCartRepository = iOrderCartRepository;
        this.userRepository = userRepository;
        this.iProductRepository = iProductRepository;
        this.iAccountRepository = iAccountRepository;
        this.iExtractRepository = iExtractRepository;
        this.orderRepository = orderRepository;
        this.utilsProduct = utilsProduct;
    }

    public float calcValueTotal(List<Long> productIds) {
        // Usamos um Map para contar a quantidade de cada produto
        Map<Long, Integer> productCounts = new HashMap<>();

        // Contar a quantidade de cada ID de produto
        for (Long productId : productIds) {
            productCounts.put(productId, productCounts.getOrDefault(productId, 0) + 1);
        }

        // Buscar todos os produtos a partir dos IDs
        List<Product> products = iProductRepository.findAllById(productCounts.keySet());

        float total = 0.0f;

        // Calcular o valor total considerando a quantidade de cada produto
        for (Product product : products) {
            int count = productCounts.get(product.getId());
            total += product.getValue() * count; // Multiplica o valor pelo número de unidades
        }

        return total;
    }


    @Transactional
    public OrderCart saveProductsInOrderCart(Long idUser, OrderCartCreateDto orderCartCreateDto) {
        Map<Long, Integer> productCounts = new HashMap<>();
        float value = calcValueTotal(orderCartCreateDto.productIds());

        UserEntity user = userRepository.findById(idUser)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User username= %s not found", idUser)));

        orderCartCreateDto.productIds().forEach(productId -> {
            productCounts.put(productId, productCounts.getOrDefault(productId, 0) + 1);
        });

        List<Product> products = new ArrayList<>();
        productCounts.forEach((productId, count) -> {
            Product product = iProductRepository.findById(productId).orElseThrow(
                    () -> new ObjectNotFoundException("Product not found")
            );

            if (product.getStatus() == StatusProduct.OUT_OF_STOCK) {
                throw new ProductOutOfStockException(String.format("Product %s unavailable or out of stock", product.getName()));
            }

            if (product.getAmount() < count) {
                throw new ProductOutOfStockException(String.format("Not enough stock for product %s", product.getName()));
            }

            product.setAmount(product.getAmount() - count); // Decrementa a quantidade total
            product.setStatus(utilsProduct.checkAmount(product.getAmount()));
            iProductRepository.save(product);

            // Adiciona o produto à lista a quantidade correta de vezes
            for (int i = 0; i < count; i++) {
                products.add(product);
            }
        });

        OrderCart orderCart = new OrderCart();
        orderCart.setUser(user);
        orderCart.setValue(value);
        orderCart.setProducts(products);
        orderCart.setStatus(StatusOrderCart.PENDING);
        orderCart.setDateTime(LocalDateTime.now());

        List<Order> orders = orderRepository.findAllByUserAndProductIn(user, products);
        orderRepository.deleteAll(orders);

        return iOrderCartRepository.save(orderCart);
    }


    @Transactional(readOnly = true)
    public List<OrderCart> findAll() {
        return iOrderCartRepository.findByStatusAll(StatusOrderCart.PENDING);
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
                        () -> new ObjectNotFoundException(String.format("Order cart not found"))
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
                .forEach(order -> order.setStatus(StatusOrder.COMPLETED));
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

    @Transactional
    public void cancelOrderCart(Long idOrderCart){
        OrderCart orderCart = iOrderCartRepository.findById(idOrderCart)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Order Cart not found. Please check the user ID or username and try again."))
                );
        orderCart.getProducts().stream()
                .forEach(product -> {
                    product.setAmount(product.getAmount() + 1);
                    product.setStatus(utilsProduct.checkAmount(product.getAmount()));
                    iProductRepository.save(product);
                });

        iOrderCartRepository.deleteById(orderCart.getId());
    }

    @Transactional
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void checkOrderCart(){
        List<OrderCart> orderCarts = iOrderCartRepository.findAll();
        orderCarts.stream()
                .forEach(orderCart -> {
                    LocalDateTime dateTime = orderCart.getDateTime();
                    dateTime = dateTime.plusMinutes(2);
                    if (!orderCart.getDateTime().isAfter(dateTime)){
                        if (orderCart.getStatus() == StatusOrderCart.PENDING){
                        logger.info("orderlog " + orderCart.getId());
                        cancelOrderCart(orderCart.getId());
                        }
                    }
                });
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Order Cart == Running at {}", dateTime);
    }
}
