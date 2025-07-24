package com.krishiconnecthub.service.impl;

import com.krishiconnecthub.dto.OrderDTO;
import com.krishiconnecthub.model.Order;
import com.krishiconnecthub.model.Product;
import com.krishiconnecthub.model.User;
import com.krishiconnecthub.repository.OrderRepository;
import com.krishiconnecthub.repository.ProductRepository;
import com.krishiconnecthub.repository.UserRepository;
import com.krishiconnecthub.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Order placeOrder(OrderDTO orderDTO) {
        User buyer = userRepository.findById(orderDTO.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found with ID: " + orderDTO.getBuyerId()));

        Product product = productRepository.findById(orderDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + orderDTO.getProductId()));

        if (product.getQuantity() < orderDTO.getQuantity()) {
            throw new IllegalStateException("Not enough stock for product: " + product.getName());
        }

        // Decrease product stock
        product.setQuantity(product.getQuantity() - orderDTO.getQuantity());
        productRepository.save(product);

        Order order = new Order();
        order.setBuyer(buyer);
        order.setProduct(product);
        order.setQuantity(orderDTO.getQuantity());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrderHistory(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }
}